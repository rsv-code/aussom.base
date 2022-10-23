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

public class AussomRef extends AussomType implements AussomTypeInt {
	private boolean isMap;
	private int lkey = -1;
	private String mkey = "";
	private ArrayList<AussomType> lobj;
	private ConcurrentHashMap<String, AussomType> mobj;
	
	public AussomRef() {
		this.setType(cType.cRef);
		this.isMap = true;
		this.lkey = -1;
		this.mkey = "";
		this.mobj = new ConcurrentHashMap<String, AussomType>();
	}
	
	public void setList(int Key, ArrayList<AussomType> Lobj) {
		this.isMap = false;
		this.lkey = Key;
		this.lobj = Lobj;
		this.mkey = "";
		this.mobj = null;
	}
	
	public ArrayList<AussomType> getList() {
		return this.lobj;
	}
	
	public void setMap(String Key, ConcurrentHashMap<String, AussomType> Mobj) {
		this.isMap = true;
		this.mkey = Key;
		this.mobj = Mobj;
		this.lkey = -1;
		this.lobj = null;
	}
	
	public ConcurrentHashMap<String, AussomType> getMap() {
		return this.mobj;
	}
	
	public AussomType getValue() throws Exception {
		if (this.isMap) {
			if (this.mobj.containsKey(this.mkey)) {
				return this.mobj.get(this.mkey);
			} else {
				throw new Exception("Reference can't find item for '" + this.mkey + "'.");
			}
		} else {
			if (this.lkey >= 0 && this.lkey < (long)this.lobj.size()) {
				return this.lobj.get((int) this.lkey);
			} else {
				throw new Exception("Reference index " + this.lkey + " out of bounds.");
			}
		}
	}
	
	public synchronized void assign(AussomType Value) {
		if (this.isMap) {
			synchronized(this.mobj) {
				this.mobj.put(this.mkey, Value);
			}
		} else {
			synchronized(this.lobj) {
				this.lobj.set(this.lkey, Value);
			}
		}
	}

	@Override
	public String toString(int Level) {
		String rstr = "";
		rstr += AussomType.getTabs(Level) + "{\n";
		rstr += AussomType.getTabs(Level + 1) + "\"type\": \"" + this.getType().name() + "\",\n";
		if (!this.isMap && this.lkey != -1) {
		  rstr += AussomType.getTabs(Level + 1) + "\"objType\": \"list\",\n";
		  rstr += AussomType.getTabs(Level + 1) + "\"lkey\": " + this.lkey + ",\n";
		  rstr += AussomType.getTabs(Level + 1) + "\"lobj\": [\n";
		  for (int i = 0; i < this.lobj.size(); i++) {
			rstr += ((AussomTypeInt)this.lobj.get(i)).toString(Level + 2) + ",\n";
		  }
		  rstr += AussomType.getTabs(Level + 1) + "],\n";
		} else if (this.isMap && this.mkey != "") {
		  rstr += AussomType.getTabs(Level + 1) + "\"objType\": \"map\",\n";
		  rstr += AussomType.getTabs(Level + 1) + "\"mkey\": \"" + this.mkey + "\",\n";
		  rstr += AussomType.getTabs(Level + 1) + "\"mobj\": {\n";
		  for(String key : this.mobj.keySet()) {
			AussomType val = this.mobj.get(key);
			rstr += AussomType.getTabs(Level + 2) + key + ":\n";
			rstr += ((AussomTypeInt)val).toString(Level + 2) + ",\n";
		  }
		  rstr += AussomType.getTabs(Level + 1) + "},\n";
		}
		rstr += AussomType.getTabs(Level) + "}";
		return rstr;
	}

	@Override
	public String str() {
		if (this.isMap) {
			return "cMap@" + Integer.toHexString(System.identityHashCode(this));
		} else {
			return "cList@" + Integer.toHexString(System.identityHashCode(this));
		}
	}
	
}
