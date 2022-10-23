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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.aussom.types.AussomNull;
import com.aussom.types.AussomString;
import com.aussom.types.AussomType;
import com.aussom.Environment;
import com.aussom.types.AussomBool;
import com.aussom.types.AussomException;
import com.aussom.types.AussomInt;

public class ADate extends Date {
	private static final long serialVersionUID = 1579993228939943395L;
	
	public ADate() { }
	
	public static LocalDate dateToLocalDate(Date Dt) {
		return Dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public static Date localDateToDate(LocalDate Ld) {
		return new Date(Ld.toEpochDay());
	}
	
	public AussomType newDate(Environment env, ArrayList<AussomType> args) {
		if(!args.get(0).isNull()) {
			this.setTime(((AussomInt)args.get(0)).getValue());
		}
		return new AussomNull();
	}
	
	@SuppressWarnings("deprecation")
	public AussomType getHours(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(this.getHours());
	}
	
	@SuppressWarnings("deprecation")
	public AussomType getMinutes(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(this.getMinutes());
	}
	
	@SuppressWarnings("deprecation")
	public AussomType getSeconds(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(this.getSeconds());
	}
	
	public AussomType getTime(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(this.getTime());
	}
	
	@SuppressWarnings("deprecation")
	public AussomType _setHours(Environment env, ArrayList<AussomType> args) {
		this.setHours((int)((AussomInt)args.get(0)).getValue());
		return new AussomNull();
	}
	
	@SuppressWarnings("deprecation")
	public AussomType _setMinutes(Environment env, ArrayList<AussomType> args) {
		this.setMinutes((int)((AussomInt)args.get(0)).getValue());
		return new AussomNull();
	}
	
	@SuppressWarnings("deprecation")
	public AussomType _setSeconds(Environment env, ArrayList<AussomType> args) {
		this.setSeconds((int)((AussomInt)args.get(0)).getValue());
		return new AussomNull();
	}
	
	public AussomType _setTime(Environment env, ArrayList<AussomType> args) {
		this.setTime(((AussomInt)args.get(0)).getValue());
		return new AussomNull();
	}
	
	public AussomType toString(Environment env, ArrayList<AussomType> args) {
		return new AussomString(this.toString());
	}
	
	public AussomType parse(Environment env, ArrayList<AussomType> args) {
		SimpleDateFormat sdf = new SimpleDateFormat(((AussomString)args.get(1)).getValueString());
		try {
			Date td = sdf.parse(((AussomString)args.get(0)).getValueString());
			this.setTime(td.getTime());
			return new AussomNull();
		} catch (ParseException e) {
			return new AussomException("Date.parse(): Parse exception. (" + e.getMessage() + ")");
		}
	}
	
	public AussomType format(Environment env, ArrayList<AussomType> args) {
		SimpleDateFormat sdf = new SimpleDateFormat(((AussomString)args.get(0)).getValueString());
		return new AussomString(sdf.format(this));
	}

	public AussomType isEpoch(Environment env, ArrayList<AussomType> args) {
		if(this.getTime() == 0) {
			return new AussomBool(true);
		}
		return new AussomBool(false);
	}
	
	/*
	 * Helper functions
	 */
	public static Date addDays(Date dt, int numDays) {
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.DATE, numDays);
		return c.getTime();
	}
}