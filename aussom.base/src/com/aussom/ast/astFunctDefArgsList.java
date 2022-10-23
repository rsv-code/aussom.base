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
import java.util.List;

import com.aussom.Environment;
import com.aussom.types.AussomList;
import com.aussom.types.AussomType;

public class astFunctDefArgsList extends astNode implements astNodeInt {
	private List<astNode> args = new ArrayList<astNode>();
	
	public astFunctDefArgsList() {
		this.setType(astNodeType.FUNCTDEFARGSLIST);
	}
	
	public astFunctDefArgsList(String Name) {
		super();
		this.setName(Name);
	}
	
	public void addNode(astNode Node) {
		this.args.add(Node);
	}
	
	public void setArgs(List<astNode> Args) {
		this.args = Args;
	}
	
	public List<astNode> getArgs() {
		return this.args;
	}

	@Override
	public String toString(int Level) {
		String rstr = "";
		rstr += getTabs(Level) + "{\n";
		rstr += this.getNodeStr(Level + 1) + ",\n";
		rstr += getTabs(Level + 1) + "\"argumentList\": [\n";
		for(int i = 0; i < args.size(); i++)
			rstr += ((astNodeInt)this.args.get(i)).toString(Level + 2) + ",\n";
		rstr += getTabs(Level + 1) + "]\n";
		rstr += getTabs(Level) + "}";
		return rstr;
	}

	@Override
	public AussomType evalImpl(Environment env, boolean getRef) throws aussomException {
		AussomList lst = new AussomList();
		AussomType ret = lst;

		for(int i = 0; i < this.args.size(); i++) {
			astNode arg = this.args.get(i);
			AussomType tmp = null;
			tmp = arg.eval(env, getRef);
			if(!tmp.isEx()) {
				lst.add(tmp);
			} else {
				ret = tmp;
				break;
			}
		}
		return ret;
	}
}
