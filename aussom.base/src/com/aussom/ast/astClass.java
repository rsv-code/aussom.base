/*
 * Copyright 2017 Austin Lehman
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aussom.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.aussom.CallStack;
import com.aussom.Engine;
import com.aussom.Environment;
import com.aussom.types.AussomBool;
import com.aussom.types.AussomDouble;
import com.aussom.types.AussomException;
import com.aussom.types.AussomException.exType;
import com.aussom.types.AussomInt;
import com.aussom.types.AussomList;
import com.aussom.types.AussomMap;
import com.aussom.types.AussomNull;
import com.aussom.types.AussomObject;
import com.aussom.types.AussomString;
import com.aussom.types.AussomType;
import com.aussom.types.Members;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class astClass extends astNode implements astNodeInt {
	// Has the class init ran yet? Init does things like link to 
	// extended classes ...
	private boolean initRan = false;
	
	// Is the class static.
	private boolean isStatic = false;

	// Is the class external.
	private boolean isExtern = false;
	
	// External class name.
	private String externClassName = "";
	
	// List of extended class names.
	private ArrayList<String> extendedClasses = new ArrayList<String>();
	
	// Constructor definition.
	private astFunctDef constructor = null;
	
	// External class reverence.
	@SuppressWarnings("rawtypes")
	private Class externClass = null;
	
	// Class members.
	private List<String> membList = new ArrayList<String>();
	private Map<String, astNode> membDefs = new ConcurrentHashMap<String, astNode>();
	
	// Class functions.
	private List<String> functList = new ArrayList<String>();
	private Map<String, astNode> functDefs = new ConcurrentHashMap<String, astNode>();
	
	// Inherited members and functions are kept track of so we can 
	// reproduce the AST later.
	private List<String> inheritedMembers = new ArrayList<String>();
	private List<String> inheritedFuncts = new ArrayList<String>();

	/**
	 * Default constructor.
	 */
	public astClass() {
		this.setType(astNodeType.CLASS);
	}
	
	/**
	 * Creates a new class.
	 * @param Name A string with the class name.
	 */
	public astClass(String Name) {
		this.setType(astNodeType.CLASS);
		this.setName(Name);
	}
	
	public void addMember(String Name, astNode Value) {
		this.membList.add(Name);
		this.membDefs.put(Name, Value);
	}
	
	public boolean containsMember(String Name) {
		return this.membDefs.containsKey(Name);
	}
	
	public astNode getMember(String Name) {
		return this.membDefs.get(Name);
	}
	
	public Map<String, astNode> getMembers() {
		return this.membDefs;
	}
	
	public void addFunction(String Name, astNode Value) {
		this.functList.add(Name);
		this.functDefs.put(Name, Value);
	}
	
	public boolean containsFunction(String Name) {
		return this.functDefs.keySet().contains(Name);
	}
	
	public astNode getFunct(String Name) {
		return this.functDefs.get(Name);
	}
	
	public void init(Environment env) throws aussomException {
		if (!this.initRan) {
			this.initRan = true;
			
			boolean foundExtern = false;
			if (this.isExtern) { foundExtern = true; }
			
			for(String className : this.extendedClasses) {
				astClass ac = env.getClassByName(className);
				
				if(ac != null) {
					if(ac.getExtern()) {
						if (ac.getName().equals("object")) {
							// Set object extern stuff to begin with if not set already.
							if (this.externClass == null) {
								this.isExtern = true;
								this.externClassName = ac.getExternClassName();
								this.externClass = ac.getExternClass();
							}
						} else {
							if(foundExtern) {
								throw new aussomException(this, "Cannot inherit from two external classes. First is '" + this.externClassName + "' and second is '" + className + "'.", env.stackTraceToString());
							}
							
							foundExtern = true;
							this.isExtern = true;
							this.externClassName = ac.getExternClassName();
							this.externClass = ac.getExternClass();
						}
					}					
				} else {
					throw new aussomException(this, "Extended class '" + className + "' not found.", env.stackTraceToString());
				}
			}
		}
	}
	
	/*
	 * Run functions
	 */
	public AussomType instantiate(Environment env) throws aussomException {
		return this.instantiate(env, true, new AussomList());
	}
	
	public AussomType instantiate(Environment env, boolean getRef, AussomList args) throws aussomException {
		// Fisrt run init if it hasn't been ran for the class.
		this.init(env);
		
		AussomObject ci;
		if (this.isExtern) {
			ci = (AussomObject) instantiateExtern();
		} else {
			ci = new AussomObject();
		}
		ci.setClassDef(this);
		
		// Instantiated inherited classes.
		this.instantiateInheritedClasses(env, ci);
		
		// Instantiate members.
		this.instantiateMembers(env, ci);
		
		/*
		 * Call constructor.
		 */
		if(this.constructor != null) {
			CallStack constStack = new CallStack();
			Environment tenv = new Environment(env.getEngine());
			tenv.setEnvironment(ci, env.getLocals(), constStack);
			AussomType ret = this.call(tenv, getRef, getName(), args);
			if (ret.isEx()) {
				return ret;
			}
		}
		
		return ci;
	}
	
	public void instantiateMembers(Environment env, AussomObject ci) throws aussomException {
		for (int i = 0; i < this.membList.size(); i++) {
			astNode cur = this.membDefs.get(this.membList.get(i));
			switch(cur.getType()) {
			case VAR:
				ci.addMember(cur.getName(), cur.eval(env));
				break;
			case BOOL:
				ci.addMember(cur.getName(), cur.eval(env));
				break;
			case INT:
				ci.addMember(cur.getName(), cur.eval(env));
				break;
			case DOUBLE:
				ci.addMember(cur.getName(), cur.eval(env));
				break;
			case STRING:
				ci.addMember(cur.getName(), cur.eval(env));
				break;
			case OBJ:
				ci.addMember(cur.getName(), cur.eval(env));
				break;
			case LIST:
				ci.addMember(cur.getName(), cur.eval(env));
				break;
			case MAP:
				ci.addMember(cur.getName(), cur.eval(env));
				break;
			case NULL:
				ci.addMember(cur.getName(), cur.eval(env));
				break;
			default:
				throw new aussomException("astClass.instantiateMembers(): Unexpected node type '" + cur.getType().name() + "' found.");
			}
		}
	}
	
	public AussomType call (Environment env, boolean getRef, String functName, AussomList args) throws aussomException {
		AussomType ret = new AussomNull();
		
		if (this.functDefs.containsKey(functName)) {
			astFunctDef fdef = (astFunctDef) this.functDefs.get(functName);
			// Local function variables and objects
			Members locals = new Members();
			Environment tenv = new Environment(env.getEngine());
			AussomObject ci = env.getClassInstance();
			
			
			if (env.getCurObj() != null && env.getCurObj() instanceof AussomObject) {
				ci = (AussomObject) env.getCurObj();
			}
			tenv.setEnvironment(ci, locals, env.getCallStack());
			if(fdef.getExtern()) {
				AussomType tmp = ((astFunctDef)this.functDefs.get(functName)).initArgs(tenv, args);
				if(!tmp.isEx()) {
					//ret = env.getClassInstance().externMod.call(functName, args);
					
					ret = fdef.callExtern(tenv, args);
					/*
					 * Check ret for exception now.
					 */
				 } else {
					 ret = tmp;
				 }
			} else {
				ret = ((astFunctDef)this.functDefs.get(functName)).call(tenv, getRef, args, this.getFileName());
			}
		} else {
			AussomException ce = new AussomException(exType.exRuntime);
			ce.setException(this.getLineNum(), "FUNCT_NOT_FOUND", "Object '" + this.getName() + "' doesn't have function '" + functName + "'.", env.getCallStack().getStackTrace());
			ret = ce;
		}
		
		return ret;
	}
	
	private void instantiateInheritedClasses(Environment env, AussomObject cobj) throws aussomException {
		for(String className : this.extendedClasses) {
			astClass ac = env.getClassByName(className);
			
			if(ac != null) {
				if(ac.getExtern()) {
					if (!ac.getName().equals("object")) {
						AussomObject ao = (AussomObject) ac.instantiate(env);
						if(ao != null) {
							cobj.setExternObject(ao.getExternObject());
						} else {
							throw new aussomException(this, "Failed to instantiate class '" + className + "', object is null.", env.stackTraceToString());
						}
					}
				}
			
				ac.instantiateMembers(env, cobj);
				
				for(String key : ac.getFuncts().keySet()) {
					// If the child most class doesn't contain the function, add it. (This allows overwrite functionality.)
					if(!this.functDefs.containsKey(key)) {
						this.functDefs.put(key, ac.getFuncts().get(key));
						this.inheritedFuncts.add(key);
					}
				}
				
			} else {
				throw new aussomException(this, "Extended class '" + className + "' not found.", env.stackTraceToString());
			}
		}
	}
	
	private AussomType instantiateExtern() throws aussomException {
		boolean primType = true;
		AussomObject obj;
		
		// Instantiate native type, or generic object.
		if (this.getName().equals("bool")) {
			obj = new AussomBool();
		} else if (this.getName().equals("int")) {
			obj = new AussomInt();
		} else if (this.getName().equals("double")) {
			obj = new AussomDouble();
		} else if (this.getName().equals("string")) {
			obj = new AussomString();
		} else if (this.getName().equals("list")) {
			obj = new AussomList();
		} else if (this.getName().equals("map")) {
			obj = new AussomMap();
		} else {
			primType = false;
			obj = new AussomObject();
		}
		
		obj.setClassDef(this);
		if (primType || this.externClass.getName().equals("com.aussom.types.AussomObject")) {
			obj.setExternObject(obj);
		} else {
			try {
		        obj.setExternObject(this.externClass.newInstance());
		    } catch (SecurityException e) {
				throw new aussomException("Instantiate extern security exception: " + e.getMessage());
			} catch (InstantiationException e) {
				throw new aussomException("Instantiate extern instantiation exception: " + e.getMessage());
			} catch (IllegalAccessException e) {
				throw new aussomException("Instantiate extern illegal access exception: " + e.getMessage());
			} catch (IllegalArgumentException e) {
				throw new aussomException("Instantiate extern illegal argument exception: " + e.getMessage());
			}
		}
		
		return obj;
	}
	
	private void loadExternClass() throws aussomException {
		ClassLoader cl = Engine.class.getClassLoader();
	    try {
	        this.externClass = cl.loadClass(this.externClassName);
	    } catch (ClassNotFoundException e) {
	    	throw new aussomException("Extern class '" + this.externClassName + "' not found.");
	    } catch (SecurityException e) {
			throw new aussomException("Extern class security exception: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new aussomException("Extern class illegal argument excetpion: " + e.getMessage());
		}
	}

	
	@Override
	public String toString() {
		return this.toString(0);
	}
	
	public String toString(int Level) {
		String rstr = "";
		rstr += getTabs(Level) + "{\n";
		rstr += this.getNodeStr(Level + 1) + ",\n";
		rstr += getTabs(Level + 1) + "\"fileName\": \"" + this.getFileName() + "\",\n";
		rstr += getTabs(Level + 1) + "\"modRef\": \"" + this.externClassName + "\",\n";
		rstr += getTabs(Level + 1) + "\"static\": \"" + this.isStatic + "\",\n";
		rstr += getTabs(Level + 1) + "\"extern\": \"" + this.isExtern + "\",\n";

		rstr += getTabs(Level + 1) + "\"definitions\": [\n";
		for(int i = 0; i < this.membList.size(); i++)
			rstr += ((astNodeInt)this.membDefs.get(this.membList.get(i))).toString(Level + 2) + ",\n";
		rstr += getTabs(Level + 1) + "],\n";

		if(this.constructor != null)
		{
			rstr += getTabs(Level + 1) + "\"constructor\":\n";
			rstr += this.constructor.toString(Level + 1) + ",\n";
		}

		rstr += getTabs(Level + 1) + "\"functionDefinitions\": [\n";
		for(int i = 0; i < this.functList.size(); i++)
			rstr += ((astNodeInt)this.functDefs.get(this.functList.get(i))).toString(Level + 2) + ",\n";
		rstr += getTabs(Level + 1) + "],\n";

		rstr += getTabs(Level) + "}";
		return rstr;
	}

	public boolean hasMain() {
		if(this.functDefs.containsKey("main"))
			return true;
		return false;
	}
	
	public void setInheritedMembers(ArrayList<String> InheritedMembers) { this.inheritedMembers = InheritedMembers; }
	public List<String> getInheritedMembers() { return this.inheritedMembers; }
	
	public void setInheritedFuncts(ArrayList<String> InheritedFuncts) { this.inheritedFuncts = InheritedFuncts; }
	public List<String> getInheritedFuncts() { return this.inheritedFuncts; }

	public boolean getStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public boolean getExtern() {
		return isExtern;
	}

	public void setExtern(boolean isExtern) {
		this.isExtern = isExtern;
	}
	
	public String getExternClassName() {
		return externClassName;
	}

	public void setExternClassName(String externClass) throws aussomException {
		this.externClassName = externClass;
		this.loadExternClass();
	}
	
	@SuppressWarnings("rawtypes")
	public void setExternClass(Class C) { this.externClass = C; }
	
	@SuppressWarnings("rawtypes")
	public Class getExternClass() { return this.externClass; }

	@Override
	public AussomType evalImpl(Environment env, boolean getref) {
		return new AussomNull();
	}

	public ArrayList<String> getExtendedClasses() {
		return extendedClasses;
	}

	public void setExtendedClasses(ArrayList<String> extendedClasses) {
		this.extendedClasses = extendedClasses;
	}

	public astFunctDef getConstructor() {
		return constructor;
	}

	public void setConstructor(astFunctDef constructor) {
		this.constructor = constructor;
	}

	public boolean instanceOf(String Name) {
		if (this.getName().equals(Name)) {
			return true;
		} else if (this.extendedClasses.contains(Name)) {
			return true;
		} else if (this.getName().equals("cnull") && Name.equals("null")) {
			return true;
		}
		return false;
	}
	
	public Map<String, astNode> getFuncts() {
		return this.functDefs;
	}
	
	@Override
	public void setName(String Name) {
		// Don't add object as extend class to object.
		if (!Name.equals("object") && !this.extendedClasses.contains("object")) {
			this.extendedClasses.add("object");
		}
		// Actually set the name.
		super.setName(Name);
	}

	public AussomType getAussomdoc() {
		// Class object
		AussomMap ret = new AussomMap();
		ret.put("type", new AussomString("class"));
		ret.put("className", new AussomString(this.getName()));
		ret.put("fileName", new AussomString(this.getFileName()));
		ret.put("lineNumber", new AussomInt(this.getLineNum()));
		ret.put("colNumber", new AussomInt(this.getColNum()));
		ret.put("isStatic", new AussomBool(this.isStatic));
		ret.put("isExtern", new AussomBool(this.isExtern));
		ret.put("externClassName", new AussomString(this.externClassName));
		AussomList extClasses = new AussomList();
		for (String str : this.extendedClasses) {
			extClasses.add(new AussomString(str));
		}
		ret.put("extendedClasses", extClasses);

		if (this.docNode != null) {
			ret.put("aussomDoc", this.docNode.getAussomdoc());
		}

		// Members
		AussomList mlist = new AussomList();
		for (String memberName : this.membList) {
			astNode mbr = this.membDefs.get(memberName);
			AussomMap mm = new AussomMap();
			mm.put("name", new AussomString(memberName));
			if (mbr.docNode == null) {
				mm.put("value", new AussomNull());
			} else {
				mm.put("value", mbr.docNode.getAussomdoc());
			}
			mlist.add(mm);
		}
		ret.put("members", mlist);

		// Functions
		AussomList flist = new AussomList();
		for (String functName : this.functList) {
			astFunctDef fun = (astFunctDef)this.functDefs.get(functName);
			flist.add(fun.getAussomdoc());
		}
		ret.put("methods", flist);

		return ret;
	}
}
