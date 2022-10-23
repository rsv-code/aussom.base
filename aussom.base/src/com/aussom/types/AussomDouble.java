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

public class AussomDouble extends AussomObject implements AussomTypeInt, AussomTypeObjectInt {
	private double value = 0.0;
	
	public AussomDouble() {
		this.setType(cType.cDouble);
		
		// Setup linkage for string object.
		this.setExternObject(this);
		try {
			this.setClassDef(Universe.get().getClassDef("double"));
		} catch (aussomException e) {
			console.get().err("AussomDouble(): Unexpected exception getting class definition: " + e.getMessage());
		}
	}
	
	public AussomDouble(double Value) {
		this();
		this.value = Value;
	}
	
	public void setValue(double Value) {
		this.value = Value;
	}
	
	public double getValue() {
		return this.value;
	}

	@Override
	public String toString(int Level) {
		String rstr = "";
		rstr += AussomType.getTabs(Level) + "{\n";
		rstr += AussomType.getTabs(Level + 1) + "\"type\": \"" + this.getType().name() + "\",\n";
		rstr += AussomType.getTabs(Level + 1) + "\"value\": " + this.value + "\n";
		rstr += AussomType.getTabs(Level) + "}";
		return rstr;
	}

	@Override
	public String str() {
		return "" + this.value;
	}
	
	public String str(int Level) {
		return this.str();
	}
	
	public AussomType toInt(Environment env, ArrayList<AussomType> args) {
		return new AussomInt((int)this.value);
	}
	
	public AussomType toBool(Environment env, ArrayList<AussomType> args) {
		if (this.value == 0.0) {
			return new AussomBool(false);
		}
		return new AussomBool(true);
	}
	
	public AussomType toString(Environment env, ArrayList<AussomType> args) {
		return new AussomString("" + this.value);
	}
	
	public AussomType compare(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(Double.compare(this.value, ((AussomDouble)args.get(0)).getValue()));
	}
	
	public AussomType isInfinite(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(Double.isInfinite(this.value));
	}
	
	public AussomType isNan(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(Double.isNaN(this.value));
	}
	
	public AussomType parse(Environment env, ArrayList<AussomType> args) {
		try {
			return new AussomDouble(Double.parseDouble(((AussomString)args.get(0)).getValue()));
		} catch(Exception e) {
			return new AussomException("double.parse(): Double parse exception.");
		}
	}
	
	public AussomType toHex(Environment env, ArrayList<AussomType> args) {
		return new AussomString(Double.toHexString(this.value));
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
