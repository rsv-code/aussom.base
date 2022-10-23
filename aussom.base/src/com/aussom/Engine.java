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

package com.aussom;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.aussom.ast.*;
import com.aussom.stdlib.Lang;
import com.aussom.stdlib.console;
import com.aussom.types.*;

/**
 * Engine object represents a Aussom interpreter instance. Then engine handles 
 * parsing files and strings, storing includes/classes and running Aussom 
 * code.
 * @author austin
 */
public class Engine {
	/**
	 * The security manager instance for this engine.
	 */
	private SecurityManagerInt secman = null;
	
	/**
	 * Flag for printing debug statements to standard out.
	 */
	private boolean debug = false;
	
	/**
	 * Stores the file names of any included Aussom code files.
	 */
	private List<String> fileNames = new ArrayList<String>();
	
	/**
	 * Flag for initialization is complete. This is set to true once 
	 * the base aussom object have been parsed. Once set to true calls to 
	 * addClass will instantiate static classes right away.
	 */
	private boolean initComplete = false;
	
	/*
	 * Main class.
	 */
	private boolean mainClassFound = false;
	private astClass mainClassDef = null;
	private AussomObject mainClassInstance = null;
	
	/*
	 * Main function.
	 */
	private boolean mainFunctFound = false;
	private astFunctDef mainFunctDef = null;
	private AussomList mainFunctArgs = new AussomList(false);
	private CallStack mainCallStack = new CallStack();
	
	/**
	 * This flag is set if the parser encounters errors. This is used 
	 * when parsing the initial source code prior to running the application. If 
	 * set to true the interpreter will fail to start.
	 */
	private boolean hasParseErrors = false;
	
	/**
	 * Allowed resource include paths. These are includes that are 
	 * located within the JAR package.
	 */
	private List<String> resourceIncludePaths = new ArrayList<String>();
	
	/**
	 * Allowed include paths.
	 */
	private List<String> includePaths = new ArrayList<String>();
	
	/**
	 * List of Aussom includes.
	 */
	private List<String> includes = new ArrayList<String>();
	
	/*
	 * Class objects storage for engine.
	 */
	private Map<String, astClass> classes = new ConcurrentHashMap<String, astClass>();
	private Map<String, AussomType> staticClasses = new ConcurrentHashMap<String, AussomType>();
	
	/**
	 * Default constructor. When called this gets an instance of the Universe object 
	 * and initializes it if not already done. It loads universe classes and instantiates 
	 * static classes. Finally it sets the initComplete flag to true.
	 * @throws Exception on failure to instantiate SecurityManagerImpl object.
	 */
	public Engine () throws Exception {
		this(new SecurityManagerImpl());
	}
	
	/**
	 * Default constructor. When called this gets an instance of the Universe object 
	 * and initializes it if not already done. It loads universe classes and instantiates 
	 * static classes. Finally it sets the initComplete flag to true.
	 * @param SecMan is a SecurityManagerImpl object for the engine.
	 * @throws Exception on init failure or failure to instantiate static classes.
	 */
	public Engine(SecurityManagerInt SecMan) throws Exception {
		this.secman = SecMan;
		
		Universe u = Universe.get();
		u.init(this);
		
		// If needed, load base classes.
		this.loadUniverseClasses();
		
		// Instantiate the static classes.
		this.instantiateStaticClasses();
		
		this.initComplete = true;
	}
	
	/**
	 * Gets the instance of the security manager for this Engine.
	 * @return A SecurityManagerInt object of the security manager.
	 */
	public SecurityManagerInt getSecurityManager() {
		return this.secman;
	}
	
	/**
	 * Adds a Aussom include to the interpreter. The include can be a standard library 
	 * language include. It can also be a file that exists in one f the includePaths 
	 * if any are set.
	 * @param Include is a String with the include to add.
	 * @throws Exception on parse failure.
	 */
	public void addInclude(String Include) throws Exception {
		if (this.debug) console.get().info("Engine.addInclude(): Include: " + Include);
		if (Lang.get().langIncludes.containsKey(Include)) {
			if (!this.includes.contains(Include)) {
				if (this.debug) console.get().info("Engine.addInclude(): Adding langInclude: " + Include);
				this.includes.add(Include);
				this.parseString(Include, Lang.get().langIncludes.get(Include));
			}
		} else {
			if (this.debug) console.get().info("Engine.addInclude(): Attempting to find in resourceIncludePaths ...");
			for (String pth : this.resourceIncludePaths) {
				List<String> resDir = Lang.get().listResourceDirectory(pth);
				String tinc = pth + Include;
				
				for (String fname : resDir) {
					if (fname.contains(tinc)) {
						if (this.debug) console.get().info("Engine.addInclude(): Include " + Include + " found in '" + fname + "'");
						this.includes.add(tinc);
						this.parseString(Include, Util.loadResource(tinc));
						return;
					}
				}
			}
			
			if (this.debug) console.get().info("Engine.addInclude(): Attempting to find in includePaths ...");
			for (String pth : this.includePaths) {
				String tinc = pth + Include;
				File f = new File(tinc);
				if (f.exists()) {
					if (!this.includes.contains(tinc)) {
						if (this.debug) console.get().info("Engine.addInclude(): Include " + Include + " found in '" + pth + "'");
						this.includes.add(tinc);
						this.parseFile(tinc);
						break;
					}
				}
			}
			
			if (this.debug) console.get().info("Engine.addInclude(): Include '" + Include + "' not found at all.");
		}
	}
	
	/**
	 * Adds an include path to the list of search paths for Aussom includes.
	 * @param Path is a String with the search path to add.
	 */
	public void addIncludePath(String Path) {
		String tinc = Path;
		if (!tinc.endsWith("/")) {
			tinc += "/";
		}
		this.includePaths.add(tinc);
	}
	
	/**
	 * Gets a list of the search include paths.
	 * @return A List of Strings with the include paths.
	 */
	public List<String> getIncludePaths() {
		return this.includePaths;
	}
	
	/**
	 * Adds an include path for a resource directory with a JAR file 
	 * to the list of resource include paths.
	 * @param Path is a String with the search resource path to add.
	 */
	public void addResourceIncludePath(String Path) {
		String tinc = Path;
		if (!tinc.endsWith("/")) {
			tinc += "/";
		}
		this.resourceIncludePaths.add(tinc);
	}
	
	/**
	 * Gets a list of the resource search include paths.
	 * @return A List of Strings with the resource include paths.
	 */
	public List<String> getResourceIncludePath() {
		return this.resourceIncludePaths;
	}
	
	/**
	 * Gets a list of current includes.
	 * @return A List of Strings with the current includes.
	 */
	public List<String> getIncludes() {
		return this.includes;
	}
	
	public void addClass(astNode TCls) throws aussomException {
		astClass Cls = (astClass)TCls;
		this.classes.put(Cls.getName(),  Cls);
		this.setClassConstructor(Cls);
		if (Cls.getStatic() && this.initComplete) {
			// Instantiate static class now.
			this.instantiateStaticClass(Cls);
		}
	}
	
	/**
	 * Gets a class instance (astClass) object from 
	 * the list of class definitions with the provided name.
	 * @param Name is a String with the class to get.
	 * @return An astClass object with the class definition or null if not found.
	 */
	public astClass getClassByName(String Name) {
		return this.classes.get(Name);
	}
	
	/**
	 * Checks to see if a class definition with the provided name exists in the engine.
	 * Note that this doesn't include static classes.
	 * @param Name is a String with the class definition to search for.
	 * @return A boolean with true for exists and false for not.
	 */
	public boolean containsClass(String Name) {
		if (this.classes.containsKey(Name)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if a static class definition with the provided name exists
	 * in the engine. 
	 * @param Name is a String with the class definition to search for.
	 * @return A boolean with true for exists and false for not.
	 */
	public boolean containsStaticClass(String Name) {
		if (this.staticClasses.containsKey(Name)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the static class object instance with the provided name.
	 * @param Name is a string with the class definition to get.
	 * @return An astClass definition for the class or null if not found.
	 */
	public AussomType getStaticClass(String Name) {
		return this.staticClasses.get(Name);
	}
	
	/**
	 * Gets a Map with the current class names and their astClass 
	 * definition objects as values.
	 * @return A Map of (String, astClass) with the current classes.
	 */
	public Map<String, astClass> getClasses() {
		return this.classes;
	}
	
	/**
	 * Sets the debug flag. If set to true the engine will print various 
	 * verbose debug statements to standard output.
	 * @param Debug is a boolean with true for debug and false for not.
	 */
	public void setDebug(boolean Debug) {
		this.debug = Debug;
	}
	
	/**
	 * Gets the debug flag.
	 * @return A boolean with true for debug and false for not.
	 */
	public boolean getDebug() {
		return this.debug;
	}
	
	/**
	 * This function loads the native type class definitions 
	 * in the Engine class set. This is need if you want to 
	 * say create a new native type using the new operator.
	 */
	public void loadUniverseClasses() {
		// Add universe classes.
		Map<String, astClass> clses = Universe.get().getClasses();
		for (String key : clses.keySet()) {
			if (!this.classes.containsKey(key)) {
				this.classes.put(key, clses.get(key));
			}
		}
	}
	
	/**
	 * The interpreter will parse the Aussom code file with the provided file name.
	 * @param FileName is a String with the Aussom code file to parse.
	 * @throws Exception on parse failure.
	 */
	public void parseFile(String FileName) throws Exception {
		this.parseString(FileName, Util.read(FileName));
	}
	
	/**
	 * The interpreter will parse the provided Aussom code string. It also 
	 * ties that code to the provided file name internally.
	 * @param FileName is a String with the file name to assign to the provided code.
	 * @param Contents is a String with the Aussom code to parse.
	 * @throws Exception on parse failure.
	 */
	public void parseString(String FileName, String Contents) throws Exception {
		Lexer scanner = new Lexer(new StringReader(Contents));
		parser p = new parser(scanner, this, FileName);
		p.parse();
		this.fileNames.add(FileName);
	}
	
	/**
	 * Runs the Aussom engine. This function goes though and identifies 
	 * the first class with a main function. If not found it will throw an exception. 
	 * If found it will call the entry point of the application (main function).
	 * @throws aussomException on failure to find main class or on parse errors.
	 */
	public void run() throws aussomException {
		if (!this.hasParseErrors) {
			if (this.debug)
				System.out.println("[debug] Running program now ...");
	
			this.mainCallStack = new CallStack();
			
			// Set the main class and function.
			if (this.setMainClassAndFunct()) {
				this.callMain();
			} else {
				throw new aussomException("Engine.run(): Failed to find main class.");
			}
		} else {
			throw new aussomException("Engine.run(): Parse errors were encountered. Not running.");
		}
	}
	
	/**
	 * Sets the constructor function reference for the 
	 * provided astClass class definition object.
	 * @param ac is a astClass object to set.
	 */
	private void setClassConstructor(astClass ac) {
		if (ac.containsFunction(ac.getName())) {
			ac.setConstructor((astFunctDef)ac.getFunct(ac.getName()));
		}
	}
	
	/**
	 * Function instantiates objects for all static class definitions. This is 
	 * called once all the base lang classes have been parsed.
	 * @throws aussomException
	 */
	private void instantiateStaticClasses() throws aussomException {
		for (String cname : this.classes.keySet()) {
			astClass ac = this.classes.get(cname);
			if (ac.getStatic()) {
				this.instantiateStaticClass(ac);
			}
		}
	}
	
	/**
	 * Instantiates a static class object with the provided class definition.
	 * @param ac is a astClass class definition object.
	 * @throws aussomException
	 */
	private void instantiateStaticClass(astClass ac) throws aussomException {
		if (this.debug)
			System.out.println("[debug] Instantiating static class: " + ac.getName());
		AussomType aci = null;
		Environment tenv = new Environment(this);
		Members locals = new Members();
		tenv.setEnvironment((AussomObject)aci, locals, this.mainCallStack);
		aci = (AussomObject) ac.instantiate(tenv, false, new AussomList());
		if(!aci.isEx()) {
			this.staticClasses.put(ac.getName(), aci);
		} else {
			throw new aussomException(((AussomException)aci).getStackTrace());
		}
	}
	
	/**
	 * Searches through list of class definitions looking for the 
	 * first one that contains a main function. Once found it sets 
	 * it's private mainClassFound and mainFunctFound variables. It 
	 * then breaks and returns true if found.
	 * @return A boolean with true if main function found and set and 
	 * false for not.
	 */
	private boolean setMainClassAndFunct() {
		boolean found = false;
		for (String cname : this.classes.keySet()) {
			if (found) break;
			astClass ac = this.classes.get(cname);
			
			if (ac.containsFunction("main")) {
				this.mainClassFound = true;
				this.mainClassDef = ac;
				this.mainFunctFound = true;
				this.mainFunctDef = (astFunctDef) ac.getFunct("main");
				found = true;
				break;
			}
		}
		return found;
	}
	
	/**
	 * This is the program entry point. This function setups up the environment, 
	 * locals and instantiates the main class. It then compiles the main function 
	 * arguments and then calls main to kick off program execution.
	 * @throws aussomException
	 */
	private void callMain() throws aussomException {
		Environment tenv = new Environment(this);
		Members locals = new Members();
		tenv.setEnvironment(null, locals, this.mainCallStack);

		AussomType tci = this.mainClassDef.instantiate(tenv, false, new AussomList());
		if(!tci.isEx())
		{
			this.mainClassInstance = (AussomObject) tci;
			tenv.setClassInstance(this.mainClassInstance);
			/*
			 * Main is expecting a list of args, but the function is expecting
			 * a list as well, so list inside of list.
			 */
			AussomList margs = new AussomList();
			margs.add(this.mainFunctArgs);

			/*
			 * Call main.
			 */
			AussomType ret = new AussomNull();
			ret = this.mainClassDef.call(tenv, false, "main", margs);
			if(ret.isEx()) {
				AussomException ex = (AussomException)ret;
				System.err.println(((AussomTypeInt)ex).str());
			}
		} else {
			AussomException ex = (AussomException)tci;
			System.err.println(ex.toString());
		}
	}
	
	/**
	 * Instantiates a new object with the provided class name.
	 * @param Name is a String with the class name to instantiate.
	 * @return A newly intsantiated AussomObject.
	 * @throws aussomException if class not found.
	 */
	public AussomObject instantiateObject(String Name) throws aussomException {
		if (this.classes.containsKey(Name)) {
			Environment tenv = new Environment(this);
			Members locals = new Members();
			tenv.setEnvironment(this.mainClassInstance, locals, this.mainCallStack);
			
			return (AussomObject) this.classes.get(Name).instantiate(tenv);
		} else {
			throw new aussomException("Attempting to instantiate object of type '" + Name + "' but class not found!");
		}
	}
	
	/**
	 * Sets the parse error flag. If set prior to run being called, run will 
	 * throw an exception because of the parse error. This is called by the 
	 * Aussom parser generated from aussom.cup.
	 */
	public void setParseError() {
		this.hasParseErrors = true;
	}
	
	/**
	 * Obligatory toString method.
	 * @return A String representing the engine includes and classes.
	 */
	@Override
	public String toString() {
		String rstr = "";

		rstr += "Parser loaded the following aussom files ...\n";
		for(int i = 0; i < this.includes.size(); i++) {
			rstr += "INCLUDE={'" + this.includes.get(i) + "'}\n";
		}
		rstr += "\n";

		rstr += "loadClassList found the following classes ...\n";
		for(String className : this.classes.keySet()) {
			rstr += "CLASS={" + className + "}\n";
			rstr += this.classes.get(className).toString();
		}
		rstr += "\n";

		return rstr;
	}
}
