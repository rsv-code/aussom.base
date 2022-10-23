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

public class astIfElse extends astNode implements astNodeInt {
	private astConditionBlock ifCondition;
	private ArrayList<astNode> ifElseConditions = new ArrayList<astNode>();
	private astStatementList elseInstructionList = new astStatementList();
	
	public astIfElse() {
		this.setType(astNodeType.IFELSE);
	}
	
	public void addToIfElseInstructionList(astNode Node) {
		this.ifElseConditions.add(Node);
	}
	
	public void addToElseInstructionList(astNode Node) {
		this.elseInstructionList.getStatements().add(Node);
	}
	
	public String toString() {
		return this.toString(0);
	}
	
	@Override
	public String toString(int Level) {
		String rstr = "";
		
		rstr += getTabs(Level) + "{\n";
		rstr += this.getNodeStr(Level + 1) + ",\n";

		if(this.ifCondition != null) {
			rstr += getTabs(Level + 1) + "\"ifCondition\":\n";
			rstr += this.ifCondition.toString(Level + 1) + ",\n";
		}

		if(this.ifElseConditions != null) {
			rstr += getTabs(Level + 1) + "\"ifElseConditions\": {\n";
			for(int i = 0; i < this.ifElseConditions.size(); i++)
			  rstr += ((astNodeInt)this.ifElseConditions.get(i)).toString(Level + 2) + ",\n";
			rstr += getTabs(Level + 1) + "}";
		}

		if(this.elseInstructionList != null) {
			rstr += getTabs(Level + 1) + "\"elseInstructionList\":\n";
			rstr += this.elseInstructionList.toString(Level + 1) + "\n";
		}
		
		rstr += getTabs(Level) + "}";
		return rstr;
	}

	@Override
	public AussomType evalImpl(Environment env, boolean getref) throws aussomException {
		AussomType ret = new AussomNull();
		AussomType etmp = this.ifCondition.getExpression().eval(env, getref);
		if(astNode.isBreakReturnExcept(etmp)) { return etmp; }
		AussomBool tmp = etmp.evalExpressionBool();
		if(!tmp.getValue()) {
			// Else If Blocks
			for(int i = 0; (i < this.ifElseConditions.size())&&(!tmp.getValue()); i++) {
				astConditionBlock acb = (astConditionBlock)this.ifElseConditions.get(i);
				etmp = acb.getExpression().eval(env, getref);
				if(astNode.isBreakReturnExcept(etmp)) { return etmp; }
				tmp = etmp.evalExpressionBool();
				if(tmp.getValue()) {
					ret = acb.eval(env, getref);
					break;
				}
			}
			// Else block
			if(!tmp.getValue()) {
				for(astNode inst : this.elseInstructionList.getStatements()) {
					ret = inst.eval(env, getref);
					if(astNode.isBreakReturnExcept(ret)) { return ret; }
				}
			}
		} else {
			ret = this.ifCondition.eval(env, getref);
		}
		
		return ret;
	}

	public astConditionBlock getIfCondition() {
		return ifCondition;
	}

	public void setIfCondition(astConditionBlock ifCondition) {
		this.ifCondition = ifCondition;
	}

	public ArrayList<astNode> getIfElseConditions() {
		return ifElseConditions;
	}

	public void setIfElseConditions(ArrayList<astNode> ifElseConditions) {
		this.ifElseConditions = (ArrayList<astNode>)ifElseConditions;
	}

	public astStatementList getElseInstructionList() {
		return elseInstructionList;
	}

	public void setElseInstructionList(astStatementList elseInstructionList) {
		this.elseInstructionList = elseInstructionList;
	}
}
