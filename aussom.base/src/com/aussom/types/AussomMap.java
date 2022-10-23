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
import java.util.concurrent.ConcurrentHashMap;

import com.aussom.Environment;
import com.aussom.Universe;
import com.aussom.Util;
import com.aussom.ast.aussomException;
import com.aussom.stdlib.console;

public class AussomMap extends AussomObject implements AussomTypeInt, AussomTypeObjectInt {
	private ConcurrentHashMap<String, AussomType> value = new ConcurrentHashMap<String, AussomType>();
	
	public AussomMap() {
		this(true);
	}
	
	public AussomMap(boolean LinkClass) {
		// Call parent AussomObject constructor with passed LinkClass arg or end up with 
		// Universe freaking out because it can't find object class reference.
		super(LinkClass);
		
		this.setType(cType.cMap);
		
		if (LinkClass) {
			// Setup linkage for string object.
			this.setExternObject(this);
			try {
				this.setClassDef(Universe.get().getClassDef("map"));
			} catch (aussomException e) {
				console.get().err("AussomMap(): Unexpected exception getting class definition: " + e.getMessage());
			}
		}
	}
	
	public AussomMap(ConcurrentHashMap<String, AussomType> Value) {
		this(true);
		this.value = Value;
	}

	public ConcurrentHashMap<String, AussomType> getValue() {
		return value;
	}

	public void setValue(ConcurrentHashMap<String, AussomType> value) {
		this.value = value;
	}

	public int size() {
		return this.value.size();
	}
	
	public boolean contains(String Key) {
		return this.value.containsKey(Key);
	}
	
	public void put(String Key, AussomType Val) {
		this.value.put(Key, Val);
	}
	
	@Override
	public String toString(int Level) {
		String rstr = "";

		rstr += AussomType.getTabs(Level) + "{\n";
		rstr += AussomType.getTabs(Level + 1) + "\"type\": \"" + this.getType().name() + "\",\n";
		rstr += AussomType.getTabs(Level + 1) + "\"values\": {\n";
		for (String Key : this.value.keySet()) {
			AussomType Val = this.value.get(Key);
			rstr += AussomType.getTabs(Level + 2) + Key + ":\n";
			rstr += ((AussomTypeInt)Val).toString(Level + 2) + ",\n";
		}
		rstr += AussomType.getTabs(Level + 1) + "}\n";
		rstr += AussomType.getTabs(Level) + "}";

		return rstr;
	}

	@Override
	public String str() {
		return this.str(0);
	}
	
	public String str(int Level) {
		if (this.value.size() > 0) {
			String rstr = "{\n";
			int count = 0;
			for (String name : this.value.keySet()) {
				rstr += getTabs(Level + 1) + "\"" + name + "\": ";
				AussomType child = this.value.get(name);
				rstr += ((AussomObject)child).str(Level + 1);
				count++;
				if (count < this.value.size()) {
					rstr += ",";
				}
				rstr += "\n";
			}
			rstr += getTabs(Level) + "}";
			return rstr;
		} else {
			return "{}";
		}
	}
	
	public AussomType clear(Environment env, ArrayList<AussomType> args) {
		this.value.clear();
		return this;
	}
	
	public AussomType containsKey(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(this.value.containsKey(((AussomString)args.get(0)).getValue()));
	}
	
	public AussomType containsVal(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(this.value.containsValue(args.get(0)));
	}
	
	public AussomType get(Environment env, ArrayList<AussomType> args) {
		return this.value.get(((AussomString)args.get(0)).getValue());
	}
	
	public AussomType isEmpty(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(this.value.isEmpty());
	}
	
	public AussomType keySet(Environment env, ArrayList<AussomType> args) {
		AussomList lst = new AussomList();
		for (String str : this.value.keySet()) {
			lst.add(new AussomString(str));
		}
		return lst;
	}
	
	public AussomType put(Environment env, ArrayList<AussomType> args) {
		this.value.put(((AussomString)args.get(0)).getValue(), args.get(1));
		return this;
	}
	
	public AussomType putAll(Environment env, ArrayList<AussomType> args) {
		AussomMap mp = (AussomMap)args.get(0);
		this.value.putAll(mp.getValue());
		return this;
	}
	
	public AussomType putIfAbsent(Environment env, ArrayList<AussomType> args) {
		this.value.putIfAbsent(((AussomString)args.get(0)).getValue(), args.get(1));
		return this;
	}
	
	public AussomType remove(Environment env, ArrayList<AussomType> args) {
		Object ret = this.value.remove(((AussomString)args.get(0)).getValue());
		if (ret == null) {
			return new AussomNull();
		} else {
			return (AussomType) ret;
		}
	}
	
	public AussomType size(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(this.value.size());
	}
	
	public AussomType values(Environment env, ArrayList<AussomType> args) {
		AussomList lst = new AussomList();
		for (String str : this.value.keySet()) {
			lst.add(this.value.get(str));
		}
		return lst;
	}
	
	@Override
	public AussomType toJson(Environment env, ArrayList<AussomType> args) {
		ArrayList<String> parts = new ArrayList<String>();
		for (String key : this.value.keySet()) {
			AussomType ct = this.value.get(key);
			if (
					ct instanceof AussomBool
					|| ct instanceof AussomNull
					|| ct instanceof AussomInt
					|| ct instanceof AussomDouble
					|| ct instanceof AussomString
					|| ct instanceof AussomList
					|| ct instanceof AussomMap
					|| ct instanceof AussomObject
				) {
				parts.add("\"" + key + "\":" + ((AussomTypeObjectInt)ct).toJson(env, new ArrayList<AussomType>()).getValueString());
				
			} else {
				return new AussomException("Unexpected type found '" + ct.getType().name() + "' when converting to JSON.");
			}
		}
		return new AussomString("{" + Util.join(parts, ",") + "}");
	}
	
	@Override
	public AussomType pack(Environment env, ArrayList<AussomType> args) {
		ArrayList<String> parts = new ArrayList<String>();
		
		// Object metadata.
		parts.add("\"type\":\"" + this.getClassDef().getName() + "\"");
		
		ArrayList<String> mparts = new ArrayList<String>();
		for (String key : this.value.keySet()) {
			AussomType ct = this.value.get(key);
			if (
					ct instanceof AussomBool
					|| ct instanceof AussomNull
					|| ct instanceof AussomInt
					|| ct instanceof AussomDouble
					|| ct instanceof AussomString
					|| ct instanceof AussomList
					|| ct instanceof AussomMap
					|| ct instanceof AussomObject
				) {
				mparts.add("\"" + key + "\":" + ((AussomTypeObjectInt)ct).pack(env, new ArrayList<AussomType>()).getValueString());
				
			} else {
				return new AussomException("Unexpected type found '" + ct.getType().name() + "' when packing map.");
			}
		}
		parts.add("\"value\":{" + Util.join(mparts, ",") + "}");
		
		return new AussomString("{" + Util.join(parts, ",") + "}");
	}
}
