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
import com.aussom.types.AussomDouble;
import com.aussom.types.AussomException;
import com.aussom.types.AussomInt;
import com.aussom.types.AussomString;
import com.aussom.types.AussomType;

public class SDouble {
	public static AussomType maxExp(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(Double.MAX_EXPONENT);
	}
	
	public static AussomType maxVal(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Double.MAX_VALUE);
	}
	
	public static AussomType minExp(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(Double.MIN_EXPONENT);
	}
	
	public static AussomType minNormal(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Double.MIN_NORMAL);
	}
	
	public static AussomType minVal(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Double.MIN_VALUE);
	}
	
	public static AussomType nanVal(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Double.NaN);
	}
	
	public static AussomType negInfinity(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Double.NEGATIVE_INFINITY);
	}
	
	public static AussomType posInfinity(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Double.POSITIVE_INFINITY);
	}
	
	public static AussomType size(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(Double.SIZE);
	}
	
	public static AussomType parse(Environment env, ArrayList<AussomType> args) {
		try {
			return new AussomDouble(Double.parseDouble(((AussomString)args.get(0)).getValue()));
		} catch(Exception e) {
			return new AussomException("Double.parse(): Double parse exception.");
		}
	}
}
