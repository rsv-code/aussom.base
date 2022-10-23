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
import com.aussom.types.AussomBool;
import com.aussom.types.AussomNull;
import com.aussom.types.AussomType;

public class astSwitch extends astNode implements astNodeInt {
	private astNode expr = null;
	private ArrayList<astNode> caseConditions = new ArrayList<astNode>();
	private astStatementList defaultList = new astStatementList();
	
	public astSwitch() {
		this.setType(astNodeType.SWITCH);
	}
	
	public void addToCaseList(astNode Node) {
		this.caseConditions.add(Node);
	}
	
	public void addToDefaultInstructionList(astNode Node) {
		this.defaultList.getStatements().add(Node);
	}

	public String toString() {
		return this.toString(0);
	}
	
	@Override
	public String toString(int Level) {
		String rstr = "";
		
		rstr += getTabs(Level) + "{\n";
		rstr += this.getNodeStr(Level + 1) + ",\n";

		if(this.expr != null) {
			rstr += getTabs(Level + 1) + "\"Expression\":\n";
			rstr += ((astNodeInt)this.expr).toString(Level + 1) + ",\n";
		}

		if(this.caseConditions != null) {
			rstr += getTabs(Level + 1) + "\"caseConditions\": {\n";
			for(int i = 0; i < this.caseConditions.size(); i++)
			  rstr += ((astNodeInt)this.caseConditions.get(i)).toString(Level + 2) + ",\n";
			rstr += getTabs(Level + 1) + "}";
		}

		if(this.defaultList != null) {
			rstr += getTabs(Level + 1) + "\"defaultList\":\n";
			rstr += this.defaultList.toString(Level + 1) + "\n";
		}
		
		rstr += getTabs(Level) + "}";
		return rstr;
	}

	@Override
	public AussomType evalImpl(Environment env, boolean getref) throws aussomException {
		AussomType ret = new AussomNull();
		AussomType tmp = this.expr.eval(env, getref);
		boolean found = false;
		// Else If Blocks
		for(int i = 0; (i < this.caseConditions.size())&&(!found); i++) {
			astConditionBlock acb = (astConditionBlock)this.caseConditions.get(i);
			AussomType etemp = acb.getExpression().eval(env, getref);
			if(tmp.getType() == etemp.getType()) {
				AussomBool res = tmp.compareEqual(env, etemp);
				if(res.getValue()) {
					ret = acb.eval(env, getref);
					found = true;
					break;
				}
			} else {
				throw new aussomException(acb, "Switch statement expecting type '" + tmp.getType().name() + " but found type '" + etemp.getType().name() + "'.", env.stackTraceToString());
			}
		}
		// Else block
		if(!found) {
			for(astNode inst : this.defaultList.getStatements()) {
				ret = inst.eval(env, getref);
				if(astNode.isBreakReturnEvent(ret))
					break;
			}
		}
		return ret;
	}
	
	public void setExpression(astNode Expr) { this.expr = Expr; }
	public astNode getExpression() { return this.expr; }

	public ArrayList<astNode> getCaseConditions() {
		return caseConditions;
	}

	public void setCaseConditions(ArrayList<astNode> caseConditions) {
		this.caseConditions = caseConditions;
	}

	public astStatementList getDefaultList() {
		return defaultList;
	}

	public void setDefaultList(astStatementList defaultList) {
		this.defaultList = defaultList;
	}
}
