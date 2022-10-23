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

import com.aussom.Engine;
import com.aussom.Environment;
import com.aussom.Universe;
import com.aussom.ast.astClass;
import com.aussom.types.*;

public class ALang {
	public static AussomType type(Environment env, ArrayList<AussomType> args) {
		AussomType ct = args.get(0);
		if (ct.getType() == cType.cObject) {
			return new AussomString(((AussomObject)ct).getClassDef().getName());
		} else {
			return new AussomString(args.get(0).getType().name().toLowerCase().substring(1));
		}
	}

	public static AussomType getClassAussomdoc(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("aussomdoc.class.getJson")) {
			String className = ((AussomString)args.get(0)).getValue();
			Engine eng = env.getEngine();
			astClass cls = eng.getClassByName(className);
			return cls.getAussomdoc();
		}
		return new AussomNull();
	}
}
