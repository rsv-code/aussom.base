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

package com.aussom.types;

import com.aussom.Environment;
import com.aussom.ast.aussomException;

public class AussomType {
	
	private cType type = cType.cUndef;
	
	public AussomType() { }

	public cType getType() {
		return type;
	}

	public void setType(cType type) {
		this.type = type;
	}
	
	public boolean isEx() {
		if (this.type == cType.cException) { return true; }
		return false;
	}
	
	public boolean isNull() {
		if (this.type == cType.cNull) { return true; }
		return false;
	}
	
	public boolean isReturn() {
		if (this.type == cType.cReturn) { return true; }
		return false;
	}
	
	public boolean isBreak() {
		if (this.type == cType.cBreak) { return true; }
		return false;
	}
	
	@Override
	public String toString() {
		return ((AussomTypeInt)this).toString(0);
	}
	
	public static String getTabs(int Level) {
		String rstr = "";
		for (int i = 0; i < Level; i++) { rstr += "\t"; }
		return rstr;
	}
	
	public boolean isNumericType() {
		if(this.type == cType.cInt)
			return true;
		else if(this.type == cType.cDouble)
			return true;
		else if(this.type == cType.cBool)
			return true;
		return false;
	}
	
	public boolean getNumericBool() throws aussomException {
		if(this.type == cType.cBool)
			return ((AussomBool)this).getValue();
		else if(this.type == cType.cInt) {
			if(((AussomInt)this).getValue() != 0)
				return true;
			return false;
		}
		else if(this.type == cType.cDouble) {
			if(((AussomDouble)this).getValue() != 0.0)
				return true;
			return false;
		} else {
			throw new aussomException("INTERNAL [AussomType.getNumericBool] Not expecting type '" + this.type.name() + "'.");
		}
	}
	
	public long getNumericInt() throws aussomException {
		if(this.type == cType.cInt)
			return ((AussomInt)this).getValue();
		else if(this.type == cType.cBool) {
			if(((AussomBool)this).getValue() == true)
				return 1;
			return 0;
		} else if(this.type == cType.cDouble) {
			return (int)((AussomDouble)this).getValue();
		} else {
			throw new aussomException("INTERNAL [AussomType.getNumericInt] Not expecting type '" + this.type.name() + "'.");
		}
	}
	
	public double getNumericDouble() throws aussomException {
		if(this.type == cType.cDouble)
			return ((AussomDouble)this).getValue();
		else if(this.type == cType.cInt) {
			return (double)((AussomInt)this).getValue();
		} else if(this.type == cType.cBool) {
			if(((AussomBool)this).getValue() == true)
				return 1.0;
			return 0.0;
		} else {
			throw new aussomException("INTERNAL [AussomType.getNumericDouble] Not expecting type '" + this.type.name() + "'.");
		}
	}
	
	public String getValueString() {
		return ((AussomTypeInt)this).str();
	}
	
	public AussomBool compareEqual(Environment env, AussomType To) throws aussomException {
		AussomBool ret = new AussomBool(false);
		
		if((this.type == cType.cNull)||(To.getType() == cType.cNull)) {
			if((this.type == cType.cNull)&&(To.getType() == cType.cNull))
				ret = new AussomBool(true);
			else
				ret = new AussomBool(false);
		} else {
			switch(this.type){
				case cBool:
					if(To.isNumericType()) {
						if(this.getNumericDouble() == To.getNumericDouble())
							ret = new AussomBool(true);
						else
							ret = new AussomBool(false);
					}
					else {
						throw new aussomException("Attempting to compare objects of type '" + this.getType() + "' and '" + To.getType() + "'.");
					}
					break;
				case cInt:
					if(To.isNumericType()) {
						if(this.getNumericDouble() == To.getNumericDouble())
							ret = new AussomBool(true);
						else
							ret = new AussomBool(false);
					} else {
						throw new aussomException("Attempting to compare objects of type '" + this.getType() + "' and '" + To.getType() + "'.");
					}
					break;
				case cDouble:
					if(To.isNumericType()) {
						if(this.getNumericDouble() == To.getNumericDouble())
							ret = new AussomBool(true);
						else
							ret = new AussomBool(false);
					} else {
						throw new aussomException("Attempting to compare objects of type '" + this.getType() + "' and '" + To.getType() + "'.");
					}
					break;
				case cString:
					if(To.getType() == cType.cString) {
						if(((AussomString)this).getValueString().equals(((AussomString)To).getValueString()))
							ret = new AussomBool(true);
						else
							ret = new AussomBool(false);
					} else {
						throw new aussomException("Attempting to compare objects of type '" + this.getType() + "' and '" + To.getType() + "'.");
					}
					break;
				case cObject:
					if(To.getType() == cType.cObject) {
						if(this == To)
							ret = new AussomBool(true);
						else
							ret = new AussomBool(false);
					} else {
						throw new aussomException("Attempting to compare objects of type '" + this.getType() + "' and '" + To.getType() + "'.");
					}
					break;
				case cList:
					if(To.getType() == cType.cList) {
						if(this == To)
							ret = new AussomBool(true);
						else
							ret = new AussomBool(false);
					} else {
						throw new aussomException("Attempting to compare objects of type '" + this.getType() + "' and '" + To.getType() + "'.");
					}
					break;
				case cMap:
					if(To.getType() == cType.cMap) {
						if(this == To)
							ret = new AussomBool(true);
						else
							ret = new AussomBool(false);
					} else {
						throw new aussomException("Attempting to compare objects of type '" + this.getType() + "' and '" + To.getType() + "'.");
					}
					break;
				default:
					throw new aussomException("Attempting to compare objects of type '" + this.getType() + "' and '" + To.getType() + "'.");
			}
		}
		
		return ret;
	}
	
	public AussomBool evalExpressionBool() throws aussomException {
		AussomBool ret = new AussomBool();
		
		switch(this.type) {
			case cBool:
				ret = (AussomBool) this;
				break;
			case cInt:
				if(((AussomInt)this).getValue() != 0)
					ret = new AussomBool(true);
				else
					ret = new AussomBool(false);
				break;
			case cDouble:
				if(((AussomDouble)this).getValue() != 0.0)
					ret = new AussomBool(true);
				else
					ret = new AussomBool(false);
				break;
			case cString:
				if(!((AussomString)this).getValue().equals(""))
					ret = new AussomBool(true);
				else
					ret = new AussomBool(false);
				break;
			case cList:
				if(((AussomList)this).size() != 0)
					ret = new AussomBool(true);
				else
					ret = new AussomBool(false);
				break;
			case cMap:
				if(((AussomMap)this).size() != 0)
					ret = new AussomBool(true);
				else
					ret = new AussomBool(false);
				break;
			case cObject:
				ret = new AussomBool(true);
				break;
			case cNull:
				ret = new AussomBool(false);
				break;
			default:
				throw new aussomException("Unexpected result from condition of type '" + this.type.name() + "'.");
		}
		return ret;
	}
}
