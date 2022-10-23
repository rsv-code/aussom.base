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

import java.util.ArrayList;

import com.aussom.Environment;
import com.aussom.Universe;
import com.aussom.ast.aussomException;
import com.aussom.stdlib.console;

public class AussomException extends AussomObject implements AussomTypeInt {
	public enum exType {
		exUndef,
		exInternal,
		exRuntime
	};
	
	private exType et = exType.exUndef;
	private int lineNumber = -1;
	private String id = "";
	private String text = "";
	private String details = "";
	private String stackTrace = "";
	
	public AussomException() {
		this.setType(cType.cException);
		
		// Setup linkage for string object.
		this.setExternObject(this);
		try {
			this.setClassDef(Universe.get().getClassDef("exception"));
		} catch (aussomException e) {
			console.get().err("AussomException(): Unexpected exception getting class definition: " + e.getMessage());
		}
	}
	
	public AussomException(exType ExType) {
		this();
		this.et = ExType;
	}
	
	public AussomException(String Text) {
		this(exType.exRuntime);
		this.text = Text;
		this.details = Text;
	}

	public void setException(int LineNum, String Id, String Text, String Details, String StackTrace) {
		this.lineNumber = LineNum;
		this.id = Id;
		this.text = Text;
		this.details = Details;
		this.stackTrace = StackTrace;
	}
	
	public void setException(int LineNum, String Id, String Text, String StackTrace) {
		this.lineNumber = LineNum;
		this.id = Id;
		this.text = Text;
		this.details = Text;
		this.stackTrace = StackTrace;
	}
	
	public exType getEt() {
		return et;
	}

	public void setEt(exType et) {
		this.et = et;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public String getExceptionTypeString() {
		return this.et.name();
	}
	
	public String stackTraceToString() {
		String rstr = "line ";
		rstr += this.lineNumber;
		rstr += ": ";
		rstr += this.et.name();
		rstr += " Exception. [" + this.id + "] :: " + text + "\n";
		rstr += this.stackTrace;
		return rstr;
	}
	
	@Override
	public String toString(int Level) {
		String rstr = "";

		rstr += AussomType.getTabs(Level);
		rstr += "line ";
		rstr += this.lineNumber;
		rstr += ": ";
		rstr += "[";
		rstr += this.getType().name();
		rstr += "] ";
		rstr += this.et.name();
		rstr += " Exception\n";

		rstr += AussomType.getTabs(Level + 1);
		rstr += "id: " + this.id + "\n";

		rstr += AussomType.getTabs(Level + 1);
		rstr += "text: " + this.text + "\n";

		rstr += AussomType.getTabs(Level + 1);
		rstr += "details: " + this.details + "\n";

		rstr += AussomType.getTabs(Level + 1);
		rstr += "stackTrace: " + this.stackTrace + "\n";

		return rstr;
	}

	@Override
	public String str() {
		return this.stackTraceToString();
	}
	
	public String str(int Level) {
		return this.str();
	}
	
	public AussomType getLineNumber(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(this.lineNumber);
	}
	
	public AussomType getExceptionType(Environment env, ArrayList<AussomType> args) {
		return new AussomString(this.et.name());
	}
	
	public AussomType getId(Environment env, ArrayList<AussomType> args) {
		return new AussomString(this.id);
	}
	
	public AussomType getText(Environment env, ArrayList<AussomType> args) {
		return new AussomString(this.text);
	}
	
	public AussomType getDetails(Environment env, ArrayList<AussomType> args) {
		return new AussomString(this.details);
	}
	
	public AussomType getStackTrace(Environment env, ArrayList<AussomType> args) {
		return new AussomString(this.stackTrace);
	}
}
