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
import com.aussom.types.AussomBool;
import com.aussom.types.AussomNull;
import com.aussom.types.AussomType;

public class astWhile extends astNode implements astNodeInt
{
	private astNode expr = null;
	private astStatementList instructions = new astStatementList();
	
	public astWhile() {
		this.setType(astNodeType.WHILE);
	}
	
	public void addToInstructionList(astNode Node) {
		this.instructions.getStatements().add(Node);
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
			rstr += getTabs(Level + 1) + "\"condition\":\n";
			rstr += ((astNodeInt)this.expr).toString(Level + 1) + ",\n";
		}

		if(this.instructions != null) {
			rstr += this.instructions.toString(Level + 1);
		}
		
		rstr += getTabs(Level) + "}";
		return rstr;
	}

	@Override
	public AussomType evalImpl(Environment env, boolean getref) throws aussomException {
		AussomType ret = new AussomNull();
		AussomType etemp = this.expr.eval(env, getref);
		AussomBool tmp = etemp.evalExpressionBool();
		while(tmp.getValue()) {
			for(astNode inst : this.instructions.getStatements()) {
				ret = inst.eval(env, getref);
				if(astNode.isBreakReturnEvent(ret))
					break;
			}
			if(astNode.isBreakReturnEvent(ret))
				break;
			else {
				etemp = this.expr.eval(env, getref);
				tmp = etemp.evalExpressionBool();
			}
		}
		return ret;
	}

	public astNode getExpr() {
		return expr;
	}

	public void setExpr(astNode expr) {
		this.expr = expr;
	}

	public astStatementList getInstructions() {
		return instructions;
	}

	public void setInstructions(astStatementList instructions) {
		this.instructions = instructions;
	}
}
