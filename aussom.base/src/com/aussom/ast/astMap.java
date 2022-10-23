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

package com.aussom.ast;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.aussom.Environment;
import com.aussom.types.AussomMap;
import com.aussom.types.AussomType;
import com.aussom.types.AussomTypeInt;

public class astMap extends astNode implements astNodeInt {
	private Map<astNode, astNode> items = new ConcurrentHashMap<astNode, astNode>();
	
	public astMap() {
		this.setType(astNodeType.MAP);
	}
	
	public astMap(Map<astNode, astNode> Items) {
		this();
		this.items = Items;
	}

	public void add(astNode Key, astNode Value) { this.items.put(Key, Value); }
	public boolean containsKey(String Key) { return this.items.containsKey(Key); }
	public astNode get(String Key) { return this.items.get(Key); }
	public int size() { return this.items.size(); }
	
	public String toString() {
		return this.toString(0);
	}
	
	@Override
	public String toString(int Level) {
		String rstr = "";
		rstr += getTabs(Level) + "{\n";
		rstr += this.getNodeStr(Level + 1) + ",\n";
		rstr += getTabs(Level + 1) + "\"pairs\": [\n";
		for(astNode key : this.items.keySet()) {
			astNode value = this.items.get(key);
			rstr += getTabs(Level + 2) + "{\n";
			rstr += getTabs(Level + 3) + "\"key\":\n";
			rstr += ((astNodeInt)key).toString(Level + 3) + ",\n";
			rstr += getTabs(Level + 3) + "\"value\":\n";
			rstr += ((astNodeInt)value).toString(Level + 3);
			rstr += getTabs(Level + 2) + "},\n";
		}
		rstr += getTabs(Level + 1) + "]\n";
		if(this.getChild() != null) {
			rstr += getTabs(Level + 1) + "\"child\":\n";
			rstr += ((astNodeInt)this.getChild()).toString(Level + 1) + ",\n";
		}
		rstr += getTabs(Level) + "}";
		return rstr;
	}

	@Override
	public AussomType evalImpl(Environment env, boolean getref) throws aussomException {
		AussomMap cm = new AussomMap();
		for (astNode key : this.items.keySet()) {
			astNode tn = this.items.get(key);
			String kstr = "";
			if (key.getType() == astNodeType.VAR || (key.getType() == astNodeType.OBJ && ((astObj)key).getChild() == null)) {
				kstr = key.getName();
			} else {
				AussomType kval = key.eval(env, getref);
				kstr = ((AussomTypeInt)kval).str();
			}
			cm.put(kstr, tn.eval(env, getref));
		}
		
		if (this.getChild() != null) {
			Environment tenv = env.clone(cm);
			return this.getChild().eval(tenv, getref);
		}
		
		return cm;
	}

	public Map<astNode, astNode> getItems() {
		return items;
	}

	public void setItems(Map<astNode, astNode> items) {
		this.items = items;
	}
}
