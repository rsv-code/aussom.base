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

public class AussomNull extends AussomObject implements AussomTypeInt, AussomTypeObjectInt {
	public AussomNull() {
		this.setType(cType.cNull);
		
		// Setup linkage for string object.
		this.setExternObject(this);
		try {
			this.setClassDef(Universe.get().getClassDef("cnull"));
		} catch (aussomException e) {
			console.get().err("AussomNull(): Unexpected exception getting class definition: " + e.getMessage());
		}
	}

	@Override
	public String toString(int Level) {
		String rstr = "";
		rstr += AussomType.getTabs(Level) + "{\n";
		rstr += AussomType.getTabs(Level + 1) + "\"type\": \"" + this.getType().name() + "\",\n";
		rstr += AussomType.getTabs(Level + 1) + "\"value\": null\n";
		rstr += AussomType.getTabs(Level) + "}";
		return rstr;
	}

	@Override
	public String str() {
		return "null";
	}
	
	public String str(int Level) {
		return this.str();
	}
	
	@Override
	public AussomType toJson(Environment env, ArrayList<AussomType> args) {
		return new AussomString("null");
	}
	
	@Override
	public AussomType pack(Environment env, ArrayList<AussomType> args) {
		ArrayList<String> parts = new ArrayList<String>();
		parts.add("\"type\":\"" + this.getClassDef().getName() + "\"");
		parts.add("\"value\":null");
		return new AussomString("{" + Util.join(parts, ",") + "}");
	}
}
