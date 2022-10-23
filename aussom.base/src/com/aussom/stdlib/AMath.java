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
import com.aussom.ast.aussomException;
import com.aussom.types.AussomDouble;
import com.aussom.types.AussomException;
import com.aussom.types.AussomInt;
import com.aussom.types.AussomType;
import com.aussom.types.cType;

public class AMath {
	public static AussomType e(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.E);
	}
	
	public static AussomType pi(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.PI);
	}
	
	public static AussomType abs(Environment env, ArrayList<AussomType> args) {
		AussomType ret = new AussomInt(0);
		AussomType arg = args.get(0);
		if(arg.getType() == cType.cBool);
		else if(arg.getType() == cType.cInt) ret = new AussomInt(Math.abs((int)((AussomInt)arg).getValue()));
		else if(arg.getType() == cType.cDouble) ret = new AussomDouble(Math.abs(((AussomDouble)arg).getValue()));
		else ret = new AussomException("math.abs(): Expecting object of type bool, int or double but found " + arg.getType().name() + " instead.");
		return ret;
	}
	
	public static AussomType acos(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.acos(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType asin(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.asin(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType atan(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.atan(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType cbrt(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.cbrt(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType ceil(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.ceil(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType copySign(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.copySign(((AussomDouble)args.get(0)).getValue(), ((AussomDouble)args.get(1)).getValue()));
	}
	
	public static AussomType cos(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.cos(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType cosh(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.cosh(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType exp(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.exp(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType expm1(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.expm1(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType floor(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.floor(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType getExponent(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.getExponent(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType hypot(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.hypot(((AussomDouble)args.get(0)).getValue(), ((AussomDouble)args.get(1)).getValue()));
	}
	
	public static AussomType IEEEremainder(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.IEEEremainder(((AussomDouble)args.get(0)).getValue(), ((AussomDouble)args.get(1)).getValue()));
	}
	
	public static AussomType log(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.log(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType log10(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.log10(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType log1p(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.log1p(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType max(Environment env, ArrayList<AussomType> args) throws aussomException {
		AussomType ret = new AussomInt(0);
		AussomType arg = args.get(0);
		AussomType arg2 = args.get(1);
		if(arg.isNumericType() && arg2.isNumericType()) ret = new AussomDouble(Math.max(arg.getNumericDouble(), arg2.getNumericDouble()));
		else ret = new AussomException("math.abs(): Expecting object of type bool, int or double but found " + arg.getType().name() + " instead.");
		return ret;
	}
	
	public static AussomType min(Environment env, ArrayList<AussomType> args) throws aussomException {
		AussomType ret = new AussomInt(0);
		AussomType arg = args.get(0);
		AussomType arg2 = args.get(1);
		if(arg.isNumericType()&&arg2.isNumericType()) ret = new AussomDouble(Math.min(arg.getNumericDouble(), arg2.getNumericDouble()));
		else ret = new AussomException("math.abs(): Expecting object of type bool, int or double but found " + arg.getType().name() + " instead.");
		return ret;
	}
	
	public static AussomType nextAfter(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.nextAfter(((AussomDouble)args.get(0)).getValue(), ((AussomDouble)args.get(1)).getValue()));
	}
	
	public static AussomType nextUp(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.nextUp(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType pow(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.pow(((AussomDouble)args.get(0)).getValue(), ((AussomDouble)args.get(1)).getValue()));
	}
	
	public static AussomType rand(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.random());
	}
	
	public static AussomType rint(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.rint(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType round(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.round(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType scalb(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.scalb(((AussomDouble)args.get(0)).getValue(), (int)((AussomInt)args.get(1)).getValue()));
	}
	
	public static AussomType signum(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.signum(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType sin(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.sin(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType sinh(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.sinh(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType sqrt(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.sqrt(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType tan(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.tan(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType tanh(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.tanh(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType toDeg(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.toDegrees(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType toRad(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.toRadians(((AussomDouble)args.get(0)).getValue()));
	}
	
	public static AussomType ulp(Environment env, ArrayList<AussomType> args) {
		return new AussomDouble(Math.ulp(((AussomDouble)args.get(0)).getValue()));
	}
}
