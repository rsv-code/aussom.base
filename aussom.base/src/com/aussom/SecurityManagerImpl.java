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
import java.util.concurrent.ConcurrentHashMap;

import com.aussom.types.AussomBool;
import com.aussom.types.AussomDouble;
import com.aussom.types.AussomException;
import com.aussom.types.AussomInt;
import com.aussom.types.AussomList;
import com.aussom.types.AussomMap;
import com.aussom.types.AussomNull;
import com.aussom.types.AussomString;
import com.aussom.types.AussomType;

/**
 * Default implementation of the security manager. This can be extended
 * to implement custom security manager functionality or properties. This 
 * object is provided in the aussom environment as secman object.
 * @author Austin Lehman
 */
public class SecurityManagerImpl implements SecurityManagerInt {
	/**
	 * Holds the properties for the security manager.
	 */
	protected ConcurrentHashMap<String, Object> props = new ConcurrentHashMap<String, Object>();

	/**
	 * Default constructor adds the standard properties.
	 */
	public SecurityManagerImpl() {
		/*
		 * Security manager itself.
		 */
		// instantiate - can new instances be created from this one? This 
		// normally applies to ones instantiated from within aussom and are blocked 
		// in ASecurityManager sub-class constructor.
		this.props.put("securitymanager.instantiate", true);
		
		// getProp
		this.props.put("securitymanager.property.get", true);
		
		// keySet/getMap
		this.props.put("securitymanager.property.list", true);
		
		// setProp
		this.props.put("securitymanager.property.set", false);
		
		/*
		 *  System information view. See com.aussom.stdlib.ASys.java.
		 */
		this.props.put("os.info.view", false);
		this.props.put("java.info.view", false);
		this.props.put("java.home.view", false);
		this.props.put("java.classpath.view", false);
		this.props.put("aussom.info.view", false);
		this.props.put("aussom.path.view", false);
		this.props.put("current.path.view", false);
		this.props.put("home.path.view", false);
		this.props.put("user.name.view", false);
		
		/*
		 *  Reflection actions. See com.aussom.stdlib.AReflect.java.
		 */
		this.props.put("reflect.eval.string", false);
		this.props.put("reflect.eval.file", false);
		this.props.put("reflect.include.module", false);

		/*
		 * Aussomdoc actions. See com.aussom.stdlib.ALang.java.
		 */
		this.props.put("aussomdoc.file.getJson", false);
		this.props.put("aussomdoc.class.getJson", false);
	}
	
	/**
	 * Java get property.
	 */
	@Override
	public Object getProperty(String PropName) {
		return this.props.get(PropName);
	}

	/**
	 * Aussom getProperty. This method will get the property, match it to a 
	 * standard AussomType and return it. If property 
	 * securitymanager.property.get is set to false this method will 
	 * throw a security exception. 
	 */
	@Override
	public AussomType getProp(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)this.getProperty("securitymanager.property.get")) {
			String PropName = ((AussomString)args.get(0)).getValueString();
			Object obj = this.props.get(PropName);
			if (obj == null) {
				return new AussomNull();
			} else if (obj instanceof Boolean) {
				return new AussomBool((Boolean)obj);
			} else if (obj instanceof Long) {
				return new AussomInt((Long)obj);
			} else if (obj instanceof Double) {
				return new AussomDouble((Double)obj);
			} else if (obj instanceof String) {
				return new AussomString((String)obj);
			} else {
				return new AussomString(obj.toString());
			}
		} else {
			return new AussomException("securitymanager.getProp(): Security exception, action 'securitymanager.property.get' not permitted.");
		}
	}
	
	/**
	 * Gets the key set of the properties as a list of strings.
	 */
	@Override
	public AussomType keySet(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)this.getProperty("securitymanager.property.list")) {
			AussomList cl = new AussomList();
			for (String key : this.props.keySet()) {
				cl.add(new AussomString(key));
			}
			return cl;
		} else {
			return new AussomException("securitymanager.keySet(): Security exception, action 'securitymanager.property.list' not permitted.");
		}
	}
	
	/**
	 * Gets a aussom map of the security manager properties and their values.
	 */
	@Override
	public AussomType getMap(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)this.getProperty("securitymanager.property.list")) {
			AussomMap cm = new AussomMap();
			for (String key : this.props.keySet()) {
				Object tobj = this.props.get(key);
				if (tobj == null) {
					cm.put(key, new AussomNull());
				} else if (tobj instanceof Boolean) {
					cm.put(key, new AussomBool((Boolean)tobj));
				} else if (tobj instanceof Long) {
					cm.put(key, new AussomInt((Long)tobj));
				} else if (tobj instanceof Double) {
					cm.put(key, new AussomDouble((Double)tobj));
				} else if (tobj instanceof String) {
					cm.put(key, new AussomString((String)tobj));
				} else {
					return new AussomException("securitymanager.getMap(): Expecting simpel type (bool, int, double, string, null) but found '" + tobj.getClass().getName() + "' instead for key '" + key + "'.");
				}
			}
			return cm;
		} else {
			return new AussomException("securitymanager.getMap(): Security exception, action 'securitymanager.property.list' not permitted.");
		}
	}
	
	/**
	 * Aussom setProp. This method provides the ability 
	 * to set the property of a security manager property pair. If property 
	 * securitymanager.property.set is set to false this method will 
	 * throw a security exception. 
	 */
	@Override
	public AussomType setProp(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)this.getProperty("securitymanager.property.set")) {
			String key = ((AussomString)args.get(0)).getValueString();
			AussomType ct = args.get(1);
			Object val = null;
			
			if (ct instanceof AussomBool) {
				val = ((AussomBool)ct).getValue();
			} else if (ct instanceof AussomString) {
				val = ((AussomString)ct).getValueString();
			} else if (ct instanceof AussomInt) {
				val = ((AussomInt)ct).getValue();
			} else if (ct instanceof AussomDouble) {
				val = ((AussomDouble)ct).getValue();
			} else if (ct instanceof AussomNull) {
				val = null;
			} else {
				return new AussomException("securitymanager.setProp(): Expecting simpel type (bool, int, double, string, null) but found '" + ct.getClass().getName() + "' instead.");
			}
			
			this.props.put(key, val);
			return env.getClassInstance();
		} else {
			return new AussomException("securitymanager.getProp(): Security exception, action 'securitymanager.property.set' not permitted.");
		}
	}
	
	/**
	 * Aussom setMap. This method provides the ability 
	 * to set a whole map of key-val pairs. If property 
	 * securitymanager.property.set is set to false this method will 
	 * throw a security exception. 
	 */
	@Override
	public AussomType setMap(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)this.getProperty("securitymanager.property.set")) {
			AussomMap mp = (AussomMap)args.get(0);
			for (String key : mp.getValue().keySet()) {
				AussomType ct = mp.getValue().get(key);
				Object val = null;
				
				if (ct instanceof AussomBool) {
					val = ((AussomBool)ct).getValue();
				} else if (ct instanceof AussomString) {
					val = ((AussomString)ct).getValueString();
				} else if (ct instanceof AussomInt) {
					val = ((AussomInt)ct).getValue();
				} else if (ct instanceof AussomDouble) {
					val = ((AussomDouble)ct).getValue();
				} else if (ct instanceof AussomNull) {
					val = null;
				} else {
					return new AussomException("securitymanager.setMap(): Expecting simpel type (bool, int, double, string, null) but found '" + ct.getClass().getName() + "' instead.");
				}
				
				this.props.put(key, val);
			}
			return env.getClassInstance();
		} else {
			return new AussomException("securitymanager.getProp(): Security exception, action 'securitymanager.property.set' not permitted.");
		}
	}
}
