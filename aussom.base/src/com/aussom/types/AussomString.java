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

import org.json.simple.JSONValue;

import com.aussom.Environment;
import com.aussom.Universe;
import com.aussom.Util;
import com.aussom.ast.aussomException;
import com.aussom.stdlib.console;

public class AussomString extends AussomObject implements AussomTypeInt, AussomTypeObjectInt {
	private String value = "";
	
	public AussomString() {
		this.setType(cType.cString);
		
		// Setup linkage for string object.
		this.setExternObject(this);
		try {
			this.setClassDef(Universe.get().getClassDef("string"));
		} catch (aussomException e) {
			console.get().err("AussomString(): Unexpected exception getting class definition: " + e.getMessage());
		}
	}
	
	public AussomString(String Value) {
		this();
		this.value = Value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString(int Level) {
		String rstr = "";
		rstr += AussomType.getTabs(Level) + "{\n";
		rstr += AussomType.getTabs(Level + 1) + "\"type\": \"" + this.getType().name() + "\",\n";
		rstr += AussomType.getTabs(Level + 1) + "\"value\": \"" + this.value + "\"\n";
		rstr += AussomType.getTabs(Level) + "}";
		return rstr;
	}

	@Override
	public String str() {
		return this.value;
	}
	
	public String str(int Level) {
		return "\"" + this.str() + "\"";
	}
	
	public AussomType charAt(Environment env, ArrayList<AussomType> args) throws aussomException {
		int index = (int) ((AussomInt)args.get(0)).getValue();
		if (index >= 0 && index < this.value.length()) {
			return new AussomString("" + this.value.charAt(index));
		} else {
			throw new aussomException("Index " + index + " out of bounds." );
		}
	}
	
	public AussomType compare(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(this.value.compareTo(((AussomString)args.get(0)).getValue()));
	}
	
	public AussomType compareICase(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(this.value.compareToIgnoreCase(((AussomString)args.get(0)).getValue()));
	}
	
	public AussomType concat(Environment env, ArrayList<AussomType> args) {
		this.value += ((AussomString)args.get(0)).getValue();
		return this;
	}
	
	public AussomType contains(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(this.value.contains(((AussomString)args.get(0)).getValue()));
	}
	
	public AussomType endsWith(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(this.value.endsWith(((AussomString)args.get(0)).getValue()));
	}
	
	public AussomType equals(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(this.value.equals(((AussomString)args.get(0)).getValue()));
	}
	
	public AussomType equalsICase(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(this.value.equalsIgnoreCase(((AussomString)args.get(0)).getValue()));
	}
	
	public AussomType indexOf(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(this.value.indexOf(((AussomString)args.get(0)).getValue()));
	}
	
	public AussomType indexOfStart(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(this.value.indexOf(((AussomString)args.get(0)).getValue(), (int)((AussomInt)args.get(1)).getValue()));
	}
	
	public AussomType isEmpty(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(this.value.isEmpty());
	}
	
	public AussomType lastIndexOf(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(this.value.lastIndexOf(((AussomString)args.get(0)).getValue()));
	}
	
	public AussomType lastIndexOfStart(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(this.value.lastIndexOf(((AussomString)args.get(0)).getValue(), (int)((AussomInt)args.get(1)).getValue()));
	}
	
	public AussomType length(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(this.value.length());
	}
	
	public AussomType matches(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(this.value.matches(((AussomString)args.get(0)).getValue()));
	}
	
	public AussomType replace(Environment env, ArrayList<AussomType> args) {
		return new AussomString(this.value.replace(((AussomString)args.get(0)).getValue(), ((AussomString)args.get(1)).getValue()));
	}
	
	public AussomType replaceFirstRegex(Environment env, ArrayList<AussomType> args) {
		return new AussomString(this.value.replaceFirst(((AussomString)args.get(0)).getValue(), ((AussomString)args.get(1)).getValue()));
	}
	
	public AussomType replaceRegex(Environment env, ArrayList<AussomType> args) {
		return new AussomString(this.value.replaceAll(((AussomString)args.get(0)).getValue(), ((AussomString)args.get(1)).getValue()));
	}
	
	public AussomType split(Environment env, ArrayList<AussomType> args) {
		AussomList ret = new AussomList();
		boolean allowBlanks = ((AussomBool)args.get(1)).getValue();
		String parts[] = this.value.split(((AussomString)args.get(0)).getValue());
		for (String part : parts) {
			if (allowBlanks || !part.trim().equals("")) {
				ret.add(new AussomString(part));
			}
		}
		return ret;
	}
	
	public AussomType startsWith(Environment env, ArrayList<AussomType> args) {
		if (args.size() > 1) {
			return new AussomBool(this.value.startsWith(((AussomString)args.get(0)).getValue(), (int)((AussomInt)args.get(1)).getValue()));
		} else {
			return new AussomBool(this.value.startsWith(((AussomString)args.get(0)).getValue()));
		}
	}
	
	public AussomType substr(Environment env, ArrayList<AussomType> args) {
		if (args.get(1).isNull()) {
			return new AussomString(this.value.substring((int)((AussomInt)args.get(0)).getValue()));
		} else {
			return new AussomString(this.value.substring((int)((AussomInt)args.get(0)).getValue(), (int)((AussomInt)args.get(1)).getValue()));
		}
	}
	
	public AussomType toLower(Environment env, ArrayList<AussomType> args) {
		return new AussomString(this.value.toLowerCase());
	}
	
	public AussomType toUpper(Environment env, ArrayList<AussomType> args) {
		return new AussomString(this.value.toUpperCase());
	}
	
	public AussomType trim(Environment env, ArrayList<AussomType> args) {
		return new AussomString(this.value.trim());
	}
	
	@Override
	public AussomType toJson(Environment env, ArrayList<AussomType> args) {
		return new AussomString("\"" + JSONValue.escape(this.str()) + "\"");
	}
	
	@Override
	public AussomType pack(Environment env, ArrayList<AussomType> args) {
		ArrayList<String> parts = new ArrayList<String>();
		parts.add("\"type\":\"" + this.getClassDef().getName() + "\"");
		parts.add("\"value\":" + this.str(0) + "");
		return new AussomString("{" + Util.join(parts, ",") + "}");
	}
}
