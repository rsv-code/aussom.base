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

public class AussomInt extends AussomObject implements AussomTypeInt, AussomTypeObjectInt {
	private long value = 0L;
	
	public AussomInt() {
		this.setType(cType.cInt);
		
		// Setup linkage for string object.
		this.setExternObject(this);
		try {
			this.setClassDef(Universe.get().getClassDef("int"));
		} catch (aussomException e) {
			console.get().err("AussomInt(): Unexpected exception getting class definition: " + e.getMessage());
		}
	}
	
	public AussomInt(long Value) {
		this();
		this.value = Value;
	}
	
	public void setValue(long Value) {
		this.value = Value;
	}
	
	public long getValue() {
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
	
	public AussomType toDouble(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble((double)this.value);
	}
	
	public AussomType toBool(Environment env, ArrayList<AussomType> args) {
		if (this.value == 0) {
			return new AussomBool(false);
		}
		return new AussomBool(true);
	}
	
	public AussomType toString(Environment env, ArrayList<AussomType> args) {
		return new AussomString("" + this.value);
	}
	
	public AussomType compare(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(Long.compare(this.value, ((AussomInt)args.get(0)).getValue()));
	}
	
	public AussomType numLeadingZeros(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(Long.numberOfLeadingZeros(this.value));
	}
	
	public AussomType numTrailingZeros(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(Long.numberOfTrailingZeros(this.value));
	}
	
	public AussomType reverse(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(Long.reverse(this.value));
	}
	
	public AussomType reverseBytes(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(Long.reverseBytes(this.value));
	}
	
	public AussomType rotateLeft(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(Long.rotateLeft(this.value, (int)((AussomInt)args.get(0)).getValue()));
	}
	
	public AussomType rotateRight(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(Long.rotateRight(this.value, (int)((AussomInt)args.get(0)).getValue()));
	}
	
	public AussomType signum(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(Long.signum(this.value));
	}
	
	public AussomType toBinary(Environment env, ArrayList<AussomType> args) {
		return new AussomString(Long.toBinaryString(this.value));
	}
	
	public AussomType toHex(Environment env, ArrayList<AussomType> args) {
		return new AussomString(Long.toHexString(this.value));
	}
	
	public AussomType toOctal(Environment env, ArrayList<AussomType> args) {
		return new AussomString(Long.toOctalString(this.value));
	}
	
	public AussomType parse(Environment env, ArrayList<AussomType> args) {
		try {
			if(args.get(1).isNull()) {
				return new AussomInt(Long.parseLong(((AussomString)args.get(0)).getValue()));
			} else {
				return new AussomInt(Long.parseLong(((AussomString)args.get(0)).getValue(), (int)((AussomInt)args.get(1)).getValue()));
			}
		} catch(Exception e) {
			return new AussomException("int.parse(): Integer parse exception.");
		}
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
