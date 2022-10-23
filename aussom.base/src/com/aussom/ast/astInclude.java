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

import java.util.ArrayList;
import java.util.Arrays;

import com.aussom.Environment;
import com.aussom.types.AussomString;
import com.aussom.types.AussomType;

public class astInclude extends astNode implements astNodeInt {
	private ArrayList<String> incList = new ArrayList<String>();
	private String ext = ".aus";
	
	public astInclude() {
		this.setType(astNodeType.INCLUDE);
	}
	
	public astInclude(String Name) {
		this.setType(astNodeType.INCLUDE);
		this.incList.add(Name);
	}
	
	public void addName(String Name) {
		this.incList.add(Name);
	}
	
	public void addFullString(String FullString) {
		this.incList = new ArrayList<String>(Arrays.asList(FullString.split("\\.")));
	}
	
	public String getPath() {
		String path = "";
		for(int i = 0; i < this.incList.size(); i++) {
			path += incList.get(i);
			if(i < (this.incList.size() -1))
				path += System.getProperty("file.separator");
		}
		if(!path.equals(""))
			path += this.ext;
		return path;
	}
	
	public String getExternClass() {
		String path = "";
		for(int i = 0; i < this.incList.size(); i++) {
			path += incList.get(i);
			if(i < (this.incList.size() -1))
				path += ".";
		}
		return path;
	}

	@Override
	public String toString(int Level) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AussomType evalImpl(Environment env, boolean getref) {
		return new AussomString(this.getPath());
	}
}
