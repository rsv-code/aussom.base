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
import com.aussom.ast.astClass;
import com.aussom.ast.astFunctDef;
import com.aussom.ast.astNode;
import com.aussom.ast.astNodeType;
import com.aussom.ast.aussomException;
import com.aussom.types.AussomBool;
import com.aussom.types.AussomList;
import com.aussom.types.AussomMap;
import com.aussom.types.AussomObject;
import com.aussom.types.AussomString;
import com.aussom.types.AussomType;
import com.aussom.types.AussomTypeInt;
import com.aussom.types.cType;

public class AClass {
	private astClass classDef = null;
	
	public AClass() { }
	
	public void setClass(astClass ClassDef) { this.classDef = ClassDef; }
	
	public AussomType getName(Environment env, ArrayList<AussomType> args) {
		return new AussomString(this.classDef.getName());
	}
	
	public AussomType isStatic(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(this.classDef.getStatic());
	}
	
	public AussomType isExtern(Environment env, ArrayList<AussomType> args) {
		return new AussomBool(this.classDef.getExtern());
	}
	
	public AussomType getExternClassName(Environment env, ArrayList<AussomType> args) {
		return new AussomString(this.classDef.getExternClassName());
	}
	
	public AussomType getMembers(Environment env, ArrayList<AussomType> args) {
		AussomMap mp = new AussomMap();
		
		for(String mname : this.classDef.getMembers().keySet()) {
			mp.put(mname, new AussomString(this.classDef.getMembers().get(mname).getType().name()));
		}
		
		return mp;
	}
	
	public AussomType getMethods(Environment env, ArrayList<AussomType> args) throws aussomException {
		AussomMap map = new AussomMap();
		
		for(String mname : this.classDef.getFuncts().keySet()) {
			astFunctDef afd = (astFunctDef) this.classDef.getFunct(mname);
			AussomMap fm = new AussomMap();
			fm.put("isExtern", new AussomBool(afd.getExtern()));
			
			AussomList alist = new AussomList();
			for(astNode tn : afd.getArgList().getArgs()) {
				AussomMap am = new AussomMap();
				
				if(tn.getType() == astNodeType.ETCETERA) {
					am.put("name", new AussomString("..."));
				} else {
					am.put("name", new AussomString(tn.getName()));
					
					
					
					String primType = "";
					if(tn.getPrimType() != cType.cUndef) {
						primType = tn.getPrimType().name().toLowerCase().substring(1);
					}
					am.put("requiredType", new AussomString(primType));
					
					if (tn.getType() == astNodeType.VAR) {
						am.put("hasDefaultValue", new AussomBool(false));
					} else {
						am.put("hasDefaultValue", new AussomBool(true));
						AussomType defVal = tn.eval(env);
						am.put("defaultValueType", new AussomString(defVal.getType().name().toLowerCase().substring(1)));
						am.put("defaultValue", new AussomString(((AussomTypeInt)defVal).str()));
					}
				}
				
				alist.add(am);
			}
			fm.put("arguments", alist);
			map.put(mname, fm);
		}
		
		return map;
	}
}