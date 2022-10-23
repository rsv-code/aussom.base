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

package com.aussom.types;

import java.util.ArrayList;

import com.aussom.Environment;
import com.aussom.Universe;
import com.aussom.ast.astClass;
import com.aussom.ast.aussomException;
import com.aussom.stdlib.console;

public class AussomCallback extends AussomObject implements AussomTypeInt {
	private String functName = "";
	private AussomObject obj = null;
	private Environment tenv = null;
	
	public AussomCallback() {
		this.setType(cType.cCallback);
		
		// Setup linkage for string object.
		this.setExternObject(this);
		try {
			this.setClassDef(Universe.get().getClassDef("callback"));
		} catch (aussomException e) {
			console.get().err("AussomCallback(): Unexpected exception getting class definition: " + e.getMessage());
		}
	}
	
	public AussomCallback(Environment Env, AussomObject Obj, String FunctName) {
		this();
		this.tenv = Env;
		this.obj = Obj;
		this.functName = FunctName;
	}
	
	//public AussomType call(AussomList args) {
	//	return this.call(this.tenv, args);
	//}
	
	public AussomType call(Environment env, AussomList args) {
		AussomType ret = new AussomNull();
		
		try {
			ret = this.callWithException(env, args);
		} catch(aussomException e) {
			console.get().err("\n" + e.getAussomStackTrace());
			return new AussomException(e.getMessage());
		}
		
		return ret;
	}
	
	public AussomType callWithException(Environment env, AussomList args) throws aussomException {
		AussomType ret = new AussomNull();
		
		AussomObject tobj = (AussomObject) env.getCurObj();
		env.setCurObj(this.getObj());
		try {
			astClass ac = this.obj.getClassDef();
			ret = ac.call(env, false, this.getFunctName(), args);
		} catch(aussomException e) {
			env.setCurObj(tobj);
			throw e;
		}
		env.setCurObj(tobj);
		
		return ret;
	}
	
	public String getFunctName() {
		return functName;
	}

	public void setFunctName(String functName) {
		this.functName = functName;
	}

	public AussomObject getObj() {
		return obj;
	}

	public void setObj(AussomObject obj) {
		this.obj = obj;
	}

	public Environment getEnv() {
		return tenv;
	}

	public void setEnv(Environment env) {
		this.tenv = env;
	}
	
	public AussomType _call(Environment env, ArrayList<AussomType> args) {
		return this.call(env, (AussomList)args.get(0));
	}
}
