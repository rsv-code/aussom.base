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
import com.aussom.types.AussomNull;
import com.aussom.types.AussomType;

public class astStatementList extends astNode implements astNodeInt {
	private List<astNode> statements = new ArrayList<astNode>();

	public astStatementList() {
		this.setType(astNodeType.STATEMENTLIST);
	}
	
	public astStatementList(String Name) {
		this();
		this.setName(Name);
	}
	
	public List<astNode> getStatements() {
		return statements;
	}

	public void setStatements(List<astNode> statements) {
		this.statements = statements;
	}
	
	public void addNode(astNode Node) {
		this.statements.add(Node);
	}

	@Override
	public AussomType evalImpl(Environment env, boolean getRef) throws aussomException {
		AussomType ret = new AussomNull();
		for(int i = 0; i < this.statements.size(); i++) {
			ret = this.statements.get(i).eval(env, getRef);
			if (ret.isEx() || ret.isReturn() || ret.isBreak()) {
			  break;
			}
		}
		return ret;
	}
	
	@Override
	public String toString(int Level) {
		String rstr = "";
		rstr += getTabs(Level) + "{\n";
		rstr += this.getNodeStr(Level + 1) + ",\n";
		rstr += getTabs(Level + 1) + "\"statements\": [\n";
		for(int i = 0; i < statements.size(); i++)
			rstr += ((astNodeInt)this.statements.get(i)).toString(Level + 2) + ",\n";
		rstr += getTabs(Level + 1) + "]\n";
		rstr += getTabs(Level) + "}";
		return rstr;
	}
}
