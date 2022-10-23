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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.aussom.Environment;
import com.aussom.Universe;
import com.aussom.ast.aussomException;
import com.aussom.types.AussomBool;
import com.aussom.types.AussomDouble;
import com.aussom.types.AussomInt;
import com.aussom.types.AussomList;
import com.aussom.types.AussomMap;
import com.aussom.types.AussomNull;
import com.aussom.types.AussomObject;
import com.aussom.types.AussomString;
import com.aussom.types.AussomType;

public class AJson {
	public static AussomType parse(Environment env, ArrayList<AussomType> args) throws Exception {
		String jstr = ((AussomString)args.get(0)).getValueString();
		
		JSONParser parser = new JSONParser();
		JSONObject jobj = (JSONObject) parser.parse(jstr);
		
		return AJson.parseJsonObject(jobj);
	}
	
	/**
	 * Takes the provided JSON obejct and converts it into a aussom 
	 * map which is then returned.
	 * @param jobj is a JSONObject with the parsed object to convert.
	 * @return A AussomMap object with the converted data.
	 * @throws aussomException 
	 */
	private static AussomType parseJsonObject(JSONObject jobj) throws aussomException {
		AussomMap mp = new AussomMap();
		
		for (Object key : jobj.keySet()) {
			Object obj = jobj.get(key);
			try {
				mp.put((String)key, AJson.parseJsonDataType(obj));
			} catch (aussomException e) {
				throw new aussomException("json.parse(): Exception for key '" + (String)key + "': " + e.getMessage());
			}
		}
		
		return mp;
	}
	
	/**
	 * Takes the provided JSON array and converts it into a aussom 
	 * list which is then returned.
	 * @param jobj is a JSONArray with the parsed array to convert.
	 * @return A AussomArray with the converted data.
	 * @throws aussomException 
	 */
	private static AussomType parseJsonArray(JSONArray jobj) throws aussomException {
		AussomList lst = new AussomList();
		
		for (int i = 0; i < jobj.size(); i++) {
			Object obj = jobj.get(i);
			try {
				lst.add(AJson.parseJsonDataType(obj));
			} catch (aussomException e) {
				throw new aussomException("json.parse(): Exception for index '" + i + "': " + e.getMessage());
			}
		}
		
		return lst;
	}
	
	/**
	 * Takes the provided Object argument, checks it's type and converts and 
	 * returns its proper aussom type.
	 * @param obj is an Object to convert.
	 * @return A AussomType with the converted value.
	 * @throws aussomException
	 */
	private static AussomType parseJsonDataType(Object obj) throws aussomException {
		AussomType cobj = null;
		
		if (obj instanceof String) {
			cobj = new AussomString((String)obj);
		} else if (obj instanceof Double) {
			cobj = new AussomDouble((Double)obj);
		} else if (obj instanceof Long) {
			cobj = new AussomInt((Long)obj);
		} else if (obj instanceof Boolean) {
			cobj = new AussomBool((Boolean)obj);
		} else if (obj == null) {
			cobj = new AussomNull();
		} else if (obj instanceof JSONObject) {
			cobj = AJson.parseJsonObject((JSONObject)obj);
		} else if (obj instanceof JSONArray) {
			cobj = AJson.parseJsonArray((JSONArray)obj);
		} else {
			throw new aussomException("Unxpected data type '" + obj.getClass().getName() + "' found.");
		}
		
		return cobj;
	}
	
	public static AussomType unpack(Environment env, ArrayList<AussomType> args) throws Exception {
		String jstr = ((AussomString)args.get(0)).getValueString();
		
		JSONParser parser = new JSONParser();
		JSONObject jobj = (JSONObject) parser.parse(jstr);
		
		return AJson.unpackJsonData(env, jobj);
	}
	
	private static AussomType unpackJsonData(Environment env, JSONObject obj) throws aussomException {
		AussomType cobj = null;
		
		if (obj.containsKey("type")) {
			String type = AJson.getJsonString(env, obj, "type");
			// BOOL
			if (type.equals("bool")) {
				if (obj.containsKey("value")) {
					cobj = new AussomBool(AJson.getJsonBoolean(env, obj, "value"));
				} else {
					throw new aussomException("json.unpack(): Malformed JSON, missing value.");
				}
			}
			// INT
			else if (type.equals("int")) {
				if (obj.containsKey("value")) {
					cobj = new AussomInt(AJson.getJsonInt(env, obj, "value"));
				} else {
					throw new aussomException("json.unpack(): Malformed JSON, missing value.");
				}
			}
			// DOUBLE
			else if (type.equals("double")) {
				if (obj.containsKey("value")) {
					cobj = new AussomDouble(AJson.getJsonDouble(env, obj, "value"));
				} else {
					throw new aussomException("json.unpack(): Malformed JSON, missing value.");
				}
			}
			// STRING
			else if (type.equals("string")) {
				if (obj.containsKey("value")) {
					cobj = new AussomString(AJson.getJsonString(env, obj, "value"));
				} else {
					throw new aussomException("json.unpack(): Malformed JSON, missing value.");
				}
			}
			// NULL
			else if (type.equals("null")) {
				if (obj.containsKey("value")) {
					cobj = new AussomNull();
				} else {
					throw new aussomException("json.unpack(): Malformed JSON, missing value.");
				}
			}
			// LIST
			else if (type.equals("list")) {
				if (obj.containsKey("value")) {
					cobj = AJson.getJsonList(env, obj, "value");
				} else {
					throw new aussomException("json.unpack(): Malformed JSON, missing value.");
				}
			}
			// OBJECT
			else if (Universe.get().getClassDef(type) != null) {
				if (obj.containsKey("members")) {
					cobj = AJson.getJsonObject(env, type, obj);
				} else {
					throw new aussomException("json.unpack(): Malformed JSON, missing value.");
				}
			}
			// UNKNOWN?
			else {
				throw new aussomException("json.unpack(): Malformed JSON, type '" + type + "' unknown.");
			}
		} else {
			throw new aussomException("json.unpack(): Malformed JSON, 'type' not found.");
		}
		
		return cobj;
	}
	
	private static Boolean getJsonBoolean(Environment env, JSONObject obj, String key) throws aussomException {
		Object tobj = obj.get(key);
		if (tobj instanceof Boolean) {
			return (Boolean)tobj;
		} else {
			throw new aussomException("json.unpack(): Getting value for key '" + key + "'. Expecting object of type Boolean but found '" + tobj.getClass().getName() + "' instead.");
		}
	}
	
	private static Long getJsonInt(Environment env, JSONObject obj, String key) throws aussomException {
		Object tobj = obj.get(key);
		if (tobj instanceof Long) {
			return (Long)tobj;
		} else {
			throw new aussomException("json.unpack(): Getting value for key '" + key + "'. Expecting object of type Long but found '" + tobj.getClass().getName() + "' instead.");
		}
	}
	
	private static Double getJsonDouble(Environment env, JSONObject obj, String key) throws aussomException {
		Object tobj = obj.get(key);
		if (tobj instanceof Double) {
			return (Double)tobj;
		} else {
			throw new aussomException("json.unpack(): Getting value for key '" + key + "'. Expecting object of type Double but found '" + tobj.getClass().getName() + "' instead.");
		}
	}
	
	private static String getJsonString(Environment env, JSONObject obj, String key) throws aussomException {
		Object tobj = obj.get(key);
		if (tobj instanceof String) {
			return (String)tobj;
		} else {
			throw new aussomException("json.unpack(): Getting value for key '" + key + "'. Expecting object of type String but found '" + tobj.getClass().getName() + "' instead.");
		}
	}
	
	private static AussomList getJsonList(Environment env, JSONObject obj, String key) throws aussomException {
		Object tobj = obj.get(key);
		if (tobj instanceof JSONArray) {
			JSONArray ja = (JSONArray) tobj;
			AussomList cl = new AussomList();
			for (int i = 0; i < ja.size(); i++) {
				Object aobj = ja.get(i);
				if (aobj instanceof JSONObject) {
					cl.add(AJson.unpackJsonData(env, (JSONObject)aobj));
				} else {
					throw new aussomException("json.unpack(): Malformed JSON structure found for array '" + key + "' at index " + i + ", expecting JSON object but found '\" + tobj.getClass().getName() + \"' instead.");
				}
			}
			return cl;
		} else {
			throw new aussomException("json.unpack(): Getting value for key '" + key + "'. Expecting object of type JSONArray but found '" + tobj.getClass().getName() + "' instead.");
		}
	}
	
	private static AussomType getJsonObject(Environment env, String type, JSONObject obj) throws aussomException {
		Object tobj = obj.get("members");
		if (tobj instanceof JSONObject) {
			JSONObject jmemb = (JSONObject)tobj;
			AussomObject co = env.getEngine().instantiateObject(type);
			
			for (String key : co.getMembers().getMap().keySet()) {
				if (jmemb.containsKey(key)) {
					if (jmemb.get(key) instanceof JSONObject) {
						co.addMember(key, AJson.unpackJsonData(env, (JSONObject)jmemb.get(key)));
					} else {
						throw new aussomException("json.unpack(): Malformed JSON, expecting '" + key + "' to be a JSON object but found '" + tobj.getClass().getName() + "' instead.");
					}
				} else {
					throw new aussomException("json.unpack(): Malformed JSON, object '" + type + "' missing member '" + key + "'.");
				}
			}
			
			return co;
		} else {
			throw new aussomException("json.unpack(): Getting value for key 'members'. Expecting object of type JSONObject but found '" + tobj.getClass().getName() + "' instead.");
		}
	}
}
