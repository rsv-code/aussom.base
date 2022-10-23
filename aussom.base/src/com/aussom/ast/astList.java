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

import java.util.ArrayList;

import com.aussom.Environment;
import com.aussom.types.AussomList;
import com.aussom.types.AussomType;

public class astList extends astNode implements astNodeInt {
	private ArrayList<astNode> items = new ArrayList<astNode>();
	
	public astList() {
		this.setType(astNodeType.LIST);
	}
	
	public astList(ArrayList<astNode> Items) {
		this.setType(astNodeType.LIST);
		this.items = Items;
	}

	public void add(astNode Node) { this.items.add(Node); }
	public int size() { return this.items.size(); }
	public void setItems(ArrayList<astNode> Items) { this.items = Items; }
	public ArrayList<astNode> getItems() { return this.items; }
	
	public String toString() {
		return this.toString(0);
	}
	
	public String toString(int Level) {
		String rstr = "";
		rstr += getTabs(Level) + "{\n";
		rstr += this.getNodeStr(Level + 1) + ",\n";
		rstr += getTabs(Level + 1) + "\"items\": [\n";
		for(int i = 0; i < this.items.size(); i++)
			rstr += ((astNodeInt)this.items.get(i)).toString(Level + 2);
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
		AussomList cl = new AussomList();
		for (astNode item : this.items) {
			cl.add(item.eval(env, getref));
		}
		
		if (this.getChild() != null) {
			Environment tenv = env.clone(cl);
			return this.getChild().eval(tenv, getref);
		}
		
		return cl;
	}
}
