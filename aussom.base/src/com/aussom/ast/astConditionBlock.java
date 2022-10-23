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

import com.aussom.Environment;
import com.aussom.types.AussomNull;
import com.aussom.types.AussomType;

public class astConditionBlock extends astNode implements astNodeInt {
	private astNode expr = null;
	private astStatementList instructionList = new astStatementList();

	public astConditionBlock() {
		this.setType(astNodeType.CONDITION);
	}
	
	public void setExpression(astNode Expr) { this.expr = Expr; }
	public astNode getExpression() { return this.expr; }
	
	public void addToInstructionList(astNode Inst) {
		this.instructionList.getStatements().add(Inst);
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
			rstr += getTabs(Level + 1) + "\"expression\":\n";
			rstr += ((astNodeInt)this.expr).toString(Level + 1) + ",\n";
		}

		if(this.instructionList != null) {
			rstr += getTabs(Level + 1) + "\"statementList\":\n";
			rstr += this.instructionList.toString(Level + 1) + ",\n";
		}
		
		rstr += getTabs(Level) + "}";
		return rstr;
	}
	
	@Override
	public AussomType evalImpl(Environment env, boolean getref) throws aussomException {
		AussomType ret = new AussomNull();
		AussomType tmp = new AussomNull();
		
		for(astNode inst : this.instructionList.getStatements()) {
			tmp = inst.eval(env, getref);
			if(astNode.isBreakReturnExcept(tmp)) {
				ret = tmp;
				break;
			}
		}
		return ret;
	}

	public astStatementList getInstructionList() {
		return instructionList;
	}

	public void setInstructionList(astStatementList instructionList) {
		this.instructionList = instructionList;
	}
}
