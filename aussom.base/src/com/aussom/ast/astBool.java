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
import com.aussom.types.AussomType;


public class astBool extends astNode implements astNodeInt
{
	private boolean value = false;
	
	public astBool() {
		this.setType(astNodeType.BOOL);
	}
	
	public astBool(boolean Value) {
		this.setType(astNodeType.BOOL);
		this.value = Value;
	}
	
	public astBool(long Value) {
		this.setType(astNodeType.BOOL);
		if(Value == 0) this.value = false;
		else this.value = true;
	}
	
	public boolean getValueBool() {
		return this.value;
	}

	public String toString() {
		return this.toString(0);
	}
	
	public String toString(int Level) {
		String rstr = "";
		rstr += getTabs(Level) + "{\n";
		rstr += this.getNodeStr(Level + 1) + ",\n";
		rstr += getTabs(Level + 1) + "\"value\": ";
		if(value) rstr += "true";
		else rstr += "false";
		rstr += "\n";
		if(this.getChild() != null) {
			rstr += getTabs(Level + 1) + "\"child\":\n";
			rstr += ((astNodeInt)this.getChild()).toString(Level + 1) + ",\n";
		}
		rstr += getTabs(Level) + "}";
		return rstr;
	}

	@Override
	public AussomType evalImpl(Environment env, boolean getref) throws aussomException {
		AussomType ret = new AussomBool(this.value);
		if (this.getChild() != null) {
			Environment tenv = env.clone(ret);
			return this.getChild().eval(tenv, getref);
		}
		return ret;
	}
}
