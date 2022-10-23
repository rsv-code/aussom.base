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
import com.aussom.Util;
import com.aussom.ast.aussomException;
import com.aussom.stdlib.console;

public class AussomBool extends AussomObject implements AussomTypeInt, AussomTypeObjectInt {
	private boolean value = false;
	
	public AussomBool() {
		this.setType(cType.cBool);
		
		// Setup linkage for string object.
		this.setExternObject(this);
		try {
			this.setClassDef(Universe.get().getClassDef("bool"));
		} catch (aussomException e) {
			console.get().err("AussomBool(): Unexpected exception getting class definition: " + e.getMessage());
		}
	}
	
	public AussomBool(boolean Value) {
		this();
		this.setValue(Value);
	}

	public boolean getValue() {
		return this.value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
	
	@Override
	public String toString(int Level) {
		String rstr = "";
		rstr += AussomType.getTabs(Level) + "{\n";
		rstr += AussomType.getTabs(Level + 1) + "\"type\": \"" + this.getType().name() + "\",\n";
		rstr += AussomType.getTabs(Level + 1) + "\"value\": ";
		if (this.value) rstr += "true";
		else rstr += "false";
		rstr += "\n";
		rstr += AussomType.getTabs(Level) + "}";
		return rstr;
	}

	@Override
	public String str() {
		if (this.value) return new String("true");
		return new String("false");
	}
	
	public String str(int Level) {
		return this.str();
	}
	
	public AussomType toInt(Environment env, ArrayList<AussomType> args) {
		if (this.value) return new AussomInt(1);
		return new AussomInt(0);
	}
	
	public AussomType toDouble(Environment env, ArrayList<AussomType> args) {
		if (this.value) return new AussomDouble(1.0);
		return new AussomDouble(0.0);
	}
	
	public AussomType toString(Environment env, ArrayList<AussomType> args) {
		if (this.value) return new AussomString("true");
		return new AussomString("false");
	}
	
	public AussomType compare(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(Boolean.compare(this.value, ((AussomBool)args.get(0)).getValue()));
	}
	
	public AussomType parse(Environment env, ArrayList<AussomType> args) {
		this.value = Boolean.parseBoolean(((AussomString)args.get(0)).getValue());
		return this;
	}
	
	@Override
	public AussomType toJson(Environment env, ArrayList<AussomType> args) {
		return this.toString(env, args);
	}
	
	@Override
	public AussomType pack(Environment env, ArrayList<AussomType> args) {
		ArrayList<String> parts = new ArrayList<String>();
		parts.add("\"type\":\"" + this.getClassDef().getName() + "\"");
		parts.add("\"value\":" + this.toString(env, args) + "");
		return new AussomString("{" + Util.join(parts, ",") + "}");
	}
}
