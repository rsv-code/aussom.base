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


public class astVar extends astNode implements astNodeInt {
	protected astNode associative = null;
	
	public astVar() {
		this.setType(astNodeType.VAR);
	}
	
	public astVar(String Name) {
		this();
		this.setName(Name);
	}
	
	public astVar(astVar Var) {
		this.setType(Var.getType());
		this.setName(Var.getName());
		this.setAssociative(Var.getAssociative());
	}
	
	public astNode getAssociative() {
		return associative;
	}

	public void setAssociative(astNode associative) {
		this.associative = associative;
	}
	
	public String toString(int Level) {
		String rstr = "";
		rstr += getTabs(Level) + "{\n";
		rstr += this.getNodeStr(Level + 1) + "\n";
		rstr += getTabs(Level) + "}";
		return rstr;
	}

	@Override
	public AussomType evalImpl(Environment env, boolean getref) throws aussomException {
		AussomType ret = new AussomNull();
		if (env.getLocals().contains(this.getName())) {
		  // Return local variable.
		  ret = env.getLocals().get(this.getName());
		}
		return ret;
	}
}
