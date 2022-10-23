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
import com.aussom.types.AussomReturn;
import com.aussom.types.AussomType;

public class astReturn extends astNode implements astNodeInt
{
	private astNode value = null;
	
	public astReturn() {
		this.setType(astNodeType.RETURN);
	}
	
	public void setValue(astNode Value) {
		this.value = Value;
	}

	@Override
	public String toString() {
		return this.toString(0);
	}
	
	@Override
	public String toString(int Level) {
		String rstr = "";
		rstr += getTabs(Level) + "{\n";
		rstr += this.getNodeStr(Level + 1) + ",\n";
		if(this.value != null) {
			rstr += getTabs(Level + 1) + "\"value\": [\n";
			rstr += ((astNodeInt)this.value).toString(Level + 1) + ",\n";
		}
		rstr += getTabs(Level) + "}";
		return rstr;
	}

	@Override
	public AussomType evalImpl(Environment env, boolean getref) throws aussomException {
		AussomReturn ret = new AussomReturn();
		if(this.value != null) {
			ret.setValue(this.value.eval(env, getref));
		}
		return ret;
	}
}
