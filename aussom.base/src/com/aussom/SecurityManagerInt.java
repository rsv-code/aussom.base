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

import java.util.ArrayList;
import com.aussom.types.AussomType;

/**
 * Security manager interface. The security manager defines the Java and Aussom 
 * interfaces for setting and getting properties that manage security settings 
 * for the Aussom Engine.
 * @author Austin Lehman
 */
public interface SecurityManagerInt {
	// Java get property value.
	public Object getProperty(String PropName);
	
	/*
	 * Aussom set or get property. Either one of these may throw not permitted or 
	 * not implemented exceptions.
	 */
	public AussomType getProp(Environment env, ArrayList<AussomType> args);
	public AussomType keySet(Environment env, ArrayList<AussomType> args);
	public AussomType getMap(Environment env, ArrayList<AussomType> args);
	public AussomType setProp(Environment env, ArrayList<AussomType> args);
	public AussomType setMap(Environment env, ArrayList<AussomType> args);
}
