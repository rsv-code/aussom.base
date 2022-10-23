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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.aussom.Engine;
import com.aussom.Main;
import com.aussom.Util;

public class Lang {
	/**
	 * The single Lang instance.
	 */
	private static Lang instance = null;
	
	/**
	 * Instance of this JAR file.
	 */
	private File jarFile = null;
	
	/**
	 * Default constructor set to private to defeat instantiation. See get to get an 
	 * instance of the object.
	 */
	private Lang() {
		this.init();
	}
	
	/**
	 * Gets a handle of the Universe object.
	 * @return The instance of the Lang object.
	 */
	public static Lang get() {
		if(instance == null) instance = new Lang();
		return instance;
	}
	
	public Map<String, String> langIncludes = new ConcurrentHashMap<String, String>();
	
	/**
	 * Initializes the list of lang includes with the basic includes in 
	 * com.aussom.stdlib.aus.
	 */
	private void init() {
		this.jarFile =  new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
		
		langIncludes.put("lang.aus", Util.loadResource("/com/aussom/stdlib/ca/lang.aus"));
		langIncludes.put("sys.aus", Util.loadResource("/com/aussom/stdlib/ca/sys.aus"));
		langIncludes.put("reflect.aus", Util.loadResource("/com/aussom/stdlib/ca/reflect.aus"));
		langIncludes.put("cunit.aus", Util.loadResource("/com/aussom/stdlib/ca/cunit.aus"));
		langIncludes.put("math.aus", Util.loadResource("/com/aussom/stdlib/ca/math.aus"));
	}
	
	public List<String> listResourceDirectory(String Path) throws IOException, URISyntaxException {
		List<String> ret = new ArrayList<String>();
		if(this.jarFile.isFile()) {
		    JarFile jar = new JarFile(this.jarFile);
		    Enumeration<JarEntry> entries = jar.entries();
		    while(entries.hasMoreElements()) {
		        String name = entries.nextElement().getName();
		        // Make sure name starts with '/'.
		        if (!name.startsWith("/")) name = "/" + name;
		        if (name.startsWith(Path)) {
		            ret.add(name);
		        }
		    }
		    jar.close();
		} else {
		    URL url = Engine.class.getResource(Path);
		    if (url != null) {
	            File entries = new File(url.toURI());
	            for (File entry : entries.listFiles()) {
	                ret.add(entry.getPath());
	            }
		    }
		}
		
		return ret;
	}
}
