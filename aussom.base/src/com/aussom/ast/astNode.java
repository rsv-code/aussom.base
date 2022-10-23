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
import com.aussom.types.AussomMap;
import com.aussom.types.AussomType;
import com.aussom.types.cType;

public class astNode {
	private String name = "";
	private astNodeType type = astNodeType.UNDEF;
	private String fileName = "";
	private int lineNum = 0;
	private int colNum = 0;
	
	private AccessType accessType = AccessType.aPrivate;
	
	// Primative type allows for strong typing.
	protected cType primativeType = cType.cUndef;
	
	private astNode child = null;

	protected astAussomDoc docNode = null;
	
	public void setPrimType(cType PrimType) {
		this.primativeType = PrimType;
	}
	
	public cType getPrimType() {
		return this.primativeType;
	}
	
	public String getNodeStr(int Level) {
		String rstr = "";
		rstr += getTabs(Level) + "\"type\": \"" + this.type.name() + "\",\n";
		rstr += getTabs(Level) + "\"name\": \"" + this.name + "\",\n";
		rstr += getTabs(Level) + "\"line\": " + this.getLineNum();
		if (this.type != astNodeType.UNDEF) {
			rstr += ",\n";
			rstr += getTabs(Level) + "\"dataType\": \"" + this.type.name() + "\"";
		}
		if (this.primativeType != cType.cUndef) {
			rstr += ",\n";
			rstr += getTabs(Level) + "\"primativeType\": \"" + this.primativeType.name() + "\"";
		}
		return rstr;
	}
	
	public static String getTabs(int level) {
		String s = "";
		for(int i = 0; i < level; i++) s += "\t";
		return s;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public astNodeType getType() {
		return type;
	}
	public void setType(astNodeType type) {
		this.type = type;
	}

	public void setFileName(String FileName){
		this.fileName = FileName;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public void setLineNum(int LineNum) {
		this.lineNum = LineNum;
	}
	
	public int getLineNum() {
		return this.lineNum;
	}
	
	public void setColNum(int ColNum) {
		this.colNum = ColNum;
	}
	
	public int getColNum() {
		return this.colNum;
	}
	
	public void setChild(astNode Child) {
		this.child = Child;
	}
	
	public astNode getChild() {
		return this.child;
	}
	
	public void setParserInfo(String FileName, int LineNum, int ColNum) {
		this.fileName = FileName;
		this.lineNum = LineNum;
		this.colNum = ColNum;
	}

	public astAussomDoc getDocNode() {
		return docNode;
	}

	public void setDocNode(astAussomDoc docNode) {
		this.docNode = docNode;
	}

	public AussomType eval(Environment env) throws aussomException {
		return this.eval(env, false);
	}
	
	public AussomType eval(Environment env, boolean getref) throws aussomException {
		AussomType ret = null;
		
		switch(type) {
		case NULL:
			ret = ((astNull)this).evalImpl(env, getref);
			break;
		case BOOL:
			ret = ((astBool)this).evalImpl(env, getref);
			break;
		case INT:
			ret = ((astInt)this).evalImpl(env, getref);
			break;
		case DOUBLE:
			ret = ((astDouble)this).evalImpl(env, getref);
			break;
		case STRING:
			ret = ((astString)this).evalImpl(env, getref);
			break;
		case LIST:
			ret = ((astList)this).evalImpl(env, getref);
			break;
		case MAP:
			ret = ((astMap)this).evalImpl(env, getref);
			break;
		case OBJ:
			ret = ((astObj)this).evalImpl(env, getref);
			break;
		case VAR:
			ret = ((astVar)this).evalImpl(env, getref);
			break;
		case EXP:
			ret = ((astExpression)this).evalImpl(env, getref);
			break;
		case FUNCTCALL:
			ret = ((astFunctCall)this).evalImpl(env, getref);
			break;
		case FUNCTDEFARGSLIST:
			ret = ((astFunctDefArgsList)this).evalImpl(env, getref);
			break;
		case RETURN:
			ret = ((astReturn)this).evalImpl(env, getref);
			break;
		case TRYCATCH:
			ret = ((astTryCatch)this).evalImpl(env, getref);
			break;
		case NEWINST:
			ret = ((astNewInst)this).evalImpl(env, getref);
			break;
		case IFELSE:
			ret = ((astIfElse)this).evalImpl(env, getref);
			break;
		case CONDITION:
			ret = ((astConditionBlock)this).evalImpl(env, getref);
			break;
		case SWITCH:
			ret = ((astSwitch)this).evalImpl(env, getref);
			break;
		case WHILE:
			ret= ((astWhile)this).evalImpl(env, getref);
			break;
		case BREAK:
			ret = ((astBreak)this).evalImpl(env, getref);
			break;
		case FOR:
			ret = ((astFor)this).evalImpl(env, getref);
			break;
		case CALLBACK:
			ret = ((astCallback)this).evalImpl(env, getref);
			break;
		case THROW:
			ret = ((astThrow)this).evalImpl(env, getref);
			break;
		case INCLUDE:
			ret = ((astInclude)this).evalImpl(env, getref);
			break;
		case AUSSOM_DOC:
			ret = ((astAussomDoc)this).evalImpl(env, getref);
			break;
		default:
			throw new aussomException(this, "INTERNAL [astNode.eval] Not implemented, attempting to eval type '" + type.name() + "'.", env.stackTraceToString());
		}
		
		return ret;
	}

	public static boolean isBreakReturnEvent(AussomType ret) {
		if((ret != null)&&((ret.getType() == cType.cReturn)||(ret.getType() == cType.cBreak))) return true;
		return false;
	}
	
	public static boolean isBreakReturnExcept(AussomType ret) {
		if (astNode.isBreakReturnEvent(ret) || ret.getType() == cType.cException) return true;
		return false;
	}

	public AccessType getAccessType() {
		return accessType;
	}

	public void setAccessType(AccessType accessType) {
		this.accessType = accessType;
	}
}
