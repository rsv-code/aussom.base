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
import java.util.Collections;

import com.aussom.Environment;
import com.aussom.Universe;
import com.aussom.Util;
import com.aussom.ast.aussomException;
import com.aussom.stdlib.console;
import com.aussom.types.AussomListComparator.SortOrder;

public class AussomList extends AussomObject implements AussomTypeInt, AussomTypeObjectInt {
	private ArrayList<AussomType> value = new ArrayList<AussomType>();
	
	public AussomList() {
		this(true);
	}
	
	public AussomList(boolean LinkClass) {
		// Call parent AussomObject constructor with passed LinkClass arg or end up with 
		// Universe freaking out because it can't find object class reference.
		super(LinkClass);
		
		this.setType(cType.cList);
		
		if (LinkClass) {
			// Setup linkage for string object.
			this.setExternObject(this);
			try {
				this.setClassDef(Universe.get().getClassDef("list"));
			} catch (aussomException e) {
				console.get().err("AussomList(): Unexpected exception getting class definition: " + e.getMessage());
			}
		}
	}
	
	public AussomList(ArrayList<AussomType> Value) {
		this();
		this.value = Value;
	}

	public synchronized void add(AussomType Val) {
		this.value.add(Val);
	}
	
	public synchronized int size() {
		return this.value.size();
	}
	
	public synchronized ArrayList<AussomType> getValue() {
		return value;
	}

	public synchronized void setValue(ArrayList<AussomType> value) {
		this.value = value;
	}

	@Override
	public synchronized String toString(int Level) {
		String rstr = "";

		rstr += AussomType.getTabs(Level) + "{\n";
		rstr += AussomType.getTabs(Level + 1) + "\"type\": \"" + this.getType().name() + "\",\n";
		rstr += AussomType.getTabs(Level + 1) + "\"length\": " + this.value.size() + ",\n";
		rstr += AussomType.getTabs(Level + 1) + "\"values\": [\n";
		for (int i = 0; i < this.value.size(); i++) {
		  rstr += ((AussomTypeInt)this.value.get(i)).toString(Level + 2) + ",\n";
		}
		rstr += AussomType.getTabs(Level + 1) + "]\n";
		rstr += AussomType.getTabs(Level) + "}";

		return rstr;
	}

	@Override
	public synchronized String str() {
		return this.str(0);
	}
	
	public synchronized String str(int Level) {
		if (this.value.size() > 0) {
			String rstr = "[\n";
			int count = 0;
			for (AussomType child : this.value) {
				rstr += getTabs(Level + 1) + ((AussomObject)child).str(Level + 1);
				count++;
				if (count < this.value.size()) {
					rstr += ",";
				}
				rstr += "\n";
			}
			rstr += getTabs(Level) + "]";
			return rstr;
		} else {
			return "[]";
		}
	}
	
	public synchronized AussomType add(Environment env, ArrayList<AussomType> args) {
		this.value.add(args.get(0));
		return this;
	}
	
	public synchronized AussomType addAll(Environment env, ArrayList<AussomType> args) {
		this.value.addAll(((AussomList)args.get(0)).getValue());
		return this;
	}
	
	public synchronized AussomType addAllAt(Environment env, ArrayList<AussomType> args) {
		this.value.addAll((int)((AussomInt)args.get(1)).getValue(), ((AussomList)args.get(0)).getValue());
		return this;
	}
	
	public synchronized AussomType clear(Environment env, ArrayList<AussomType> args) {
		this.value.clear();
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized AussomType clone(Environment env, ArrayList<AussomType> args) {
		AussomList nl = new AussomList();
		nl.setValue((ArrayList<AussomType>) this.value.clone());
		return nl;
	}
	
	public synchronized AussomType contains(Environment env, ArrayList<AussomType> args) {
		AussomType mn = args.get(0);
		
		for(AussomType n : this.value) {
			if(mn.isNull()) {
				if(n.isNull()) {
					return new AussomBool(true);
				}
			} else if(mn instanceof AussomBool) { 
				if((n instanceof AussomBool)&&(((AussomBool)n).getValue() == ((AussomBool)mn).getValue())) {
					return new AussomBool(true);
				}
			} else if(mn instanceof AussomInt) {
				if((n instanceof AussomInt)&&((AussomInt)n).getValue() == ((AussomInt)mn).getValue()) {
					return new AussomBool(true);
				}
			} else if(mn instanceof AussomDouble) {
				if((n instanceof AussomDouble)&&(((AussomDouble)n).getValue() == ((AussomDouble)mn).getValue())) {
					return new AussomBool(true);
				}
			} else if(mn instanceof AussomString) {
				if((n instanceof AussomString)&&(((AussomString)n).getValue().equals(((AussomString)mn).getValue()))) {
					return new AussomBool(true);
				}
			} else if(mn instanceof AussomCallback) {
				if((n instanceof AussomCallback)&&(((AussomCallback)n).getFunctName().equals(((AussomCallback)mn).getFunctName()))) {
					return new AussomBool(true);
				}
			}
		}
		
		return new AussomBool(false);
	}
	
	public synchronized AussomType containsObjRef(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(this.value.contains(args.get(0)));
	}
	
	public synchronized AussomType get(Environment env, ArrayList<AussomType> args) {
		int index = (int) ((AussomInt)args.get(0)).getValue();
		if(index >= 0 && index < this.value.size()) {
			return this.value.get(index);
		} else {
			return new AussomException("list.get(): Index out of bounds.");
		}
	}
	
	public synchronized AussomType indexOf(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(this.value.indexOf(args.get(0)));
	}
	
	public synchronized AussomType isEmpty(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(this.value.isEmpty());
	}
	
	public synchronized AussomType remove(Environment env, ArrayList<AussomType> args) {
		this.value.remove(args.get(0));
		return this;
	}
	
	public synchronized AussomType removeAt(Environment env, ArrayList<AussomType> args) {
		int index = (int) ((AussomInt)args.get(0)).getValue();
		if(index >= 0 && index < this.value.size()) {
			return (AussomType)this.value.remove(index);
		} else {
			return new AussomException("list.removeAt(): Index out of bounds.");
		}
	}
	
	public synchronized AussomType removeAll(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(this.value.removeAll(((AussomList)args.get(0)).getValue()));
	}
	
	public synchronized AussomType retainAll(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(this.value.retainAll(((AussomList)args.get(0)).getValue()));
	}
	
	public synchronized AussomType set(Environment env, ArrayList<AussomType> args) {
		int index = (int) ((AussomInt)args.get(0)).getValue();
		if(index >= 0 && index < this.value.size()) {
			return this.value.set(index, args.get(1));
		} else {
			return new AussomException("list.set(): Index out of bounds.");
		}
	}
	
	public synchronized AussomType size(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(this.value.size());
	}
	
	public synchronized AussomType subList(Environment env, ArrayList<AussomType> args) {
		int bindex = (int)((AussomInt)args.get(0)).getValue();
		int eindex = (int)((AussomInt)args.get(1)).getValue();
		if(eindex >= bindex && bindex >= 0 && eindex <= this.value.size()) {
			AussomList ret = new AussomList();
			ret.setValue(new ArrayList<AussomType>(this.value.subList(bindex, eindex)));
			return ret;
		} else {
			return new AussomException("list.subList(): Index out of bounds.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized AussomType sort(Environment env, ArrayList<AussomType> args) {
		AussomListComparator lc = new AussomListComparator();
		Collections.sort(this.value, lc);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized AussomType sortAsc(Environment env, ArrayList<AussomType> args) {
		AussomListComparator lc = new AussomListComparator();
		lc.setSortOrder(SortOrder.ASCENDING);
		Collections.sort(this.value, lc);
		return this;
	}
	
	public synchronized AussomType join(Environment env, ArrayList<AussomType> args) {
		String glue = ((AussomString)args.get(0)).getValue();
		ArrayList<String> parts = new ArrayList<String>();
		
		for (AussomType itm : this.value) {
			parts.add(((AussomTypeInt)itm).str());
		}
		return new AussomString(Util.join(parts, glue));
	}
	
	@SuppressWarnings("unchecked")
	public synchronized AussomType sortCustom(Environment env, ArrayList<AussomType> args) {
		AussomCallback onCompare = (AussomCallback)args.get(0);
		
		AussomListComparator lc = new AussomListComparator();
		lc.setSortOrder(SortOrder.CUSTOM);
		lc.setCallback(onCompare);
		
		try {
			Collections.sort(this.value, lc); 
		} catch(Exception e) {
			return new AussomException(e.getMessage());
		}
		
		return this;
	}
	
	@Override
	public AussomType toJson(Environment env, ArrayList<AussomType> args) {
		ArrayList<String> parts = new ArrayList<String>();
		for (AussomType ct : this.value) {
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
				parts.add(((AussomTypeObjectInt)ct).toJson(env, new ArrayList<AussomType>()).getValueString());
			} else {
				return new AussomException("Unexpected type found '" + ct.getType().name() + "' when converting to JSON.");
			}
		}
		return new AussomString("[" + Util.join(parts, ",") + "]");
	}
	
	@Override
	public AussomType pack(Environment env, ArrayList<AussomType> args) {
		ArrayList<String> parts = new ArrayList<String>();
		
		// Object metadata.
		parts.add("\"type\":\"" + this.getClassDef().getName() + "\"");
		
		ArrayList<String> mparts = new ArrayList<String>();
		for (AussomType ct : this.value) {
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
				mparts.add(((AussomTypeObjectInt)ct).toJson(env, new ArrayList<AussomType>()).getValueString());
			} else {
				return new AussomException("Unexpected type found '" + ct.getType().name() + "' when converting to JSON.");
			}
		}
		parts.add("\"value\":[" + Util.join(mparts, ",") + "]");
		
		return new AussomString("{" + Util.join(parts, ",") + "}");
	}
}
