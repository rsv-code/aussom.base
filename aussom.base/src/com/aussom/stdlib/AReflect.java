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

package com.aussom.stdlib;

import java.util.ArrayList;

import com.aussom.types.AussomType;
import com.aussom.Environment;
import com.aussom.ast.astClass;
import com.aussom.ast.aussomException;
import com.aussom.types.AussomBool;
import com.aussom.types.AussomException;
import com.aussom.types.AussomList;
import com.aussom.types.AussomNull;
import com.aussom.types.AussomObject;
import com.aussom.types.AussomString;

public class AReflect {
	public static AussomType _evalStr(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("reflect.eval.string")) {
			String code = ((AussomString)args.get(0)).getValue();
			String name = ((AussomString)args.get(1)).getValue();
			try {
				env.getEngine().parseString(name, code);
				return new AussomNull();
			} catch (aussomException e) {
				return new AussomException(e.getMessage());
			} catch (Exception e) {
				return new AussomException(e.getMessage());
			}
		} else {
			return new AussomException("reflect.evalStr(): Security exception, action 'reflect.eval.string' not permitted.");
		}
	}
	
	public static AussomType _evalFile(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("reflect.eval.file")) {
			String FileName = ((AussomString)args.get(0)).getValue();
			try {
				env.getEngine().parseFile(FileName);
			} catch (Exception e) {
				return new AussomException(e.getMessage());
			}
			return new AussomNull();
		} else {
			return new AussomException("reflect.evalFile(): Security exception, action 'reflect.eval.file' not permitted.");
		}
	}
	
	
	
	public static AussomType _includeModule(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("reflect.include.module")) {
			String incFile = ((AussomString)args.get(0)).getValue().replace(".", System.getProperty("file.separator")) + ".aus";
			try {
				env.getEngine().parseFile(incFile);
			} catch (Exception e) {
				return new AussomException(e.getMessage());
			}
			return new AussomNull();
		} else {
			return new AussomException("reflect.includeModule(): Security exception, action 'reflect.include.module' not permitted.");
		}
	}
	
	public static AussomType loadedModules(Environment env, ArrayList<AussomType> args) {
		AussomList list = new AussomList();
		for (String mod : env.getEngine().getIncludes()) {
			list.add(new AussomString(mod));
		}
		return list;
	}
	
	public static AussomType loadedClasses(Environment env, ArrayList<AussomType> args) {
		AussomList list = new AussomList();
		for (String cls : env.getEngine().getClasses().keySet()) {
			list.add(new AussomString(cls));
		}
		return list;
	}
	
	public static AussomType isModuleLoaded(Environment env, ArrayList<AussomType> args) {
		String incFile = ((AussomString)args.get(0)).getValue().replace(".", System.getProperty("file.separator")) + ".aus";
		return new AussomBool(env.getEngine().getIncludes().contains(incFile));
	}
	
	public static AussomType classExists(Environment env, ArrayList<AussomType> args) {
		AussomString name = (AussomString)args.get(0);
		return new AussomBool(env.getEngine().getClasses().containsKey(name.getValue()));
	}
	
	public static AussomType getClassDef(Environment env, ArrayList<AussomType> args) {
		String ClassName = ((AussomString)args.get(0)).getValue();
		if(env.getEngine().getClasses().containsKey(ClassName)) {
			astClass tc = env.getEngine().getClasses().get(ClassName);
			astClass cc = env.getEngine().getClasses().get("rclass");
			try {
				AussomObject co = (AussomObject) cc.instantiate(env);
				AClass ccobj = (AClass)co.getExternObject();
				ccobj.setClass(tc);
				return co;
			} catch (aussomException e) {
				return new AussomException("reflect.getClassDef(): Class '" + ClassName + "'.\n");
			}
		} else {
			return new AussomException("reflect.getClassDef(): Class '" + ClassName + "' not found.");
		}
	}
	
	public static AussomType instantiate(Environment env, ArrayList<AussomType> args) {
		String ClassName = ((AussomString)args.get(0)).getValue();
		try {
			AussomObject co = env.getEngine().instantiateObject(ClassName);
			return co;
		} catch (aussomException e) {
			return new AussomException("reflect.instantiate(): Failed to instantiate class  '" + ClassName + "'.\n");
		}
	}
	
	public static AussomType invoke(Environment env, ArrayList<AussomType> args) {
		AussomObject obj = (AussomObject)args.get(0);
		String fname = ((AussomString)args.get(1)).getValue();
		AussomList alist = (AussomList) args.get(2);
		
		try {
			astClass ac = obj.getClassDef();
			Environment tenv = env.clone(obj);
			return ac.call(tenv, false, fname, alist);
		} catch (aussomException e) {
			return new AussomException(e.getAussomMessage());
		} catch (Exception e) {
			return new AussomException("reflect.invoke(): Unhandled exception occurred while calling '" + fname + "'. " + e.getMessage());
		}
	}
}
