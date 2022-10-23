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

package com.aussom;

import com.aussom.ast.astClass;
import com.aussom.types.AussomObject;
import com.aussom.types.AussomType;
import com.aussom.types.Members;

/**
 * Houses the current Aussom executing environment. Things it maintains are the current Aussom 
 * Engine instance, the current class instance, the local variables, the call stack and the 
 * current object pointer.
 * @author austin
 */
public class Environment {
	private Engine eng = null;
	private AussomObject ci = null;
	private Members locals = null;
	private CallStack st = null;
	
	/**
	 *  This is to store the current working object. This isn't used 
	 *  for scoping, that is what ci is for.
	 */
	private AussomType curObj = null;
	
	/**
	 * Default constructor takes the Engine reference 
	 * as argument.
	 * @param Eng is the Aussom Engine instance.
	 */
	public Environment(Engine Eng) {
		this.eng = Eng;
		this.curObj = null;
	}

	/**
	 * Sets the environment information.
	 * @param Ci is a AussomObject with the current class instance for scope.
	 * @param Locals is an instance of Members which is the function locals.
	 * @param St is an instance of StackTrace object.
	 */
	public void setEnvironment(AussomObject Ci, Members Locals, CallStack St) {
		this.ci = Ci;
		this.locals = Locals;
		this.st = St;
	}
	
	/**
	 * Gets a reference to the current Aussom Engine object.
	 * @return A Engine object.
	 */
	public Engine getEngine() {
		return eng;
	}

	/**
	 * Gets the current class instance.
	 * @return A AussomObject with the current class instance.
	 */
	public AussomObject getClassInstance() {
		return ci;
	}

	/**
	 * Sets the current class instance.
	 * @param ci Is a AussomObject with the class instance to set.
	 */
	public void setClassInstance(AussomObject ci) {
		this.ci = ci;
	}

	/**
	 * Gets an instance of the current locals.
	 * @return A Members object with the current locals.
	 */
	public Members getLocals() {
		return locals;
	}

	/**
	 * Sets the current locals.
	 * @param locals is a Memvers object with the current locals.
	 */
	public void setLocals(Members locals) {
		this.locals = locals;
	}

	/**
	 * Gets the current call stack object.
	 * @return A CallStack object.
	 */
	public CallStack getCallStack() {
		return st;
	}

	/**
	 * Sets the current call stack.
	 * @param st is a CallStack object to set.
	 */
	public void setCallStack(CallStack st) {
		this.st = st;
	}

	/**
	 * Gets the current object reference.
	 * @return An object with the current object.
	 */
	public AussomType getCurObj() {
		return curObj;
	}

	/**
	 * Sets the current object reference.
	 * @param curObj is the current object reference.
	 */
	public void setCurObj(AussomType curObj) {
		this.curObj = curObj;
	}
	
	/**
	 * Helper that gets a class instance by name 
	 * from the Engine.
	 * @param Name is a String with the name of the class definition to get.
	 * @return A astClass object for the provided name.
	 */
	public astClass getClassByName(String Name) {
		return this.eng.getClassByName(Name);
	}
	
	/**
	 * Clones the environment using the provided current object 
	 * for the new environment and returns the cloned environment object.
	 * @param Cur is an object with the current object reference.
	 * @return The cloned Environment object.
	 */
	public Environment clone(AussomType Cur) {
		Environment ret = new Environment(this.eng);
		ret.setEnvironment(this.ci, this.locals, this.st);
		ret.setCurObj(Cur);
		return ret;
	}
	
	/**
	 * Gets the current StackTrace object as a string.
	 * @return A String with the current stack trace.
	 */
	public String stackTraceToString() {
		return this.st.getStackTrace();
	}
	
	/**
	 * Removes the last StackTrace object from the chain.
	 */
	public void removeLastTrace() {
		synchronized(this) {
			if (this.st.getParent() != null) {
				this.st = this.st.getParent();
			}
		}
	}
}
