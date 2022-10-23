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

package com.aussom.stdlib;

import java.util.ArrayList;

import com.aussom.Environment;
import com.aussom.types.AussomType;

/**
 * Aussom security manager. This class is the implementation within Aussom that gives access 
 * to the set/get prop functions from the security manager within the current Engine.
 * @author Austin Lehman
 */
public class ASecMan {
	/**
	 * Aussom getProperty. This method will get the property, match it to a 
	 * standard AussomType and return it.
	 * @param env is the current Environment object.
	 * @param args is an ArrayList of AussomType objects which are the function arguments.
	 * @return A AussomType object with the requested property.
	 */
	public AussomType getProp(Environment env, ArrayList<AussomType> args) {
		return env.getEngine().getSecurityManager().getProp(env, args);
	}
	
	/**
	 * Gets the key set of the properties as a list of strings.
	 * @param env is the current Environment object.
	 * @param args is an ArrayList of AussomType objects which are the function arguments.
	 * @return A AussomType object with the current object.
	 */
	public AussomType keySet(Environment env, ArrayList<AussomType> args) {
		return env.getEngine().getSecurityManager().keySet(env, args);
	}
	
	/**
	 * Gets a aussom map of the security manager properties and their values.
	 * @param env is the current Environment object.
	 * @param args is an ArrayList of AussomType objects which are the function arguments.
	 * @return A AussomType object with the requested map.
	 */
	public AussomType getMap(Environment env, ArrayList<AussomType> args) {
		return env.getEngine().getSecurityManager().getMap(env, args);
	}
	
	/**
	 * Aussom setProperty. This method by default returns an exception 
	 * because we don't normally want the application code modifying the 
	 * contents of the security manager. This can be overridden if other 
	 * functionality is desired.
	 * @param env is the current Environment object.
	 * @param args is an ArrayList of AussomType objects which are the function arguments.
	 * @return A AussomType object with the current object.
	 */
	public AussomType setProp(Environment env, ArrayList<AussomType> args) {
		return env.getEngine().getSecurityManager().setProp(env, args);
	}
}
