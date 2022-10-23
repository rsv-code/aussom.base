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
import com.aussom.types.AussomInt;
import com.aussom.types.AussomType;


public class astInt extends astNode implements astNodeInt {
	private long value = 0;
	
	public astInt() {
		this.setType(astNodeType.INT);
	}
	
	public astInt(long Value) {
		this.setType(astNodeType.INT);
		this.value = Value;
	}
	
	public long getValueInt() {
		return this.value;
	}
	
	public String toString() {
		return this.toString(0);
	}
	
	public String toString(int Level) {
		String rstr = "";
		rstr += getTabs(Level) + "{\n";
		rstr += this.getNodeStr(Level + 1) + ",\n";
		rstr += getTabs(Level + 1) + "\"value\": " + this.value + "\n";
		if(this.getChild() != null) {
			rstr += getTabs(Level + 1) + "\"child\":\n";
			rstr += ((astNodeInt)this.getChild()).toString(Level + 1) + ",\n";
		}
		rstr += getTabs(Level) + "}";
		return rstr;
	}


	public AussomType evalImpl(Environment env, boolean getref) throws aussomException {
		AussomType ret = new AussomInt(this.value);
		if (this.getChild() != null) {
			Environment tenv = env.clone(ret);
			return this.getChild().eval(tenv, getref);
		}
		return ret;
	}
}
