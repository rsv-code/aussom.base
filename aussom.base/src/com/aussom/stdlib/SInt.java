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
import com.aussom.types.AussomException;
import com.aussom.types.AussomInt;
import com.aussom.types.AussomString;
import com.aussom.types.AussomType;

public class SInt {
	public static AussomType parse(Environment env, ArrayList<AussomType> args) {
		try {
			if(args.get(1).isNull()) {
				return new AussomInt(Long.parseLong(((AussomString)args.get(0)).getValue()));
			} else {
				return new AussomInt(Long.parseLong(((AussomString)args.get(0)).getValue(), (int)((AussomInt)args.get(1)).getValue()));
			}
		} catch(Exception e) {
			return new AussomException("Int.parse(): Integer parse exception.");
		}
	}
	
	public static AussomType maxVal(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(Long.MAX_VALUE);
	}
	
	public static AussomType minVal(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(Long.MIN_VALUE);
	}
}
