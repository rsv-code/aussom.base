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
import com.aussom.types.AussomType;


public interface astNodeInt {
	/* Setters */
	public void setName(String Name);
	public void setType(astNodeType Type);
	
	/* Getters */
	public String getName();
	public astNodeType getType();
	
	/* To String */
	public String toString();
	public String toString(int Level);
	
	public AussomType evalImpl(Environment env, boolean getref) throws aussomException;
}
