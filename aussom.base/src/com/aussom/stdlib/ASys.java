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
import java.net.URLDecoder;
import java.util.ArrayList;

import com.aussom.Environment;
import com.aussom.Universe;
import com.aussom.types.AussomInt;
import com.aussom.types.AussomNull;
import com.aussom.types.AussomString;
import com.aussom.types.AussomType;

public class ASys {
	public static String getSysInfo(Environment env) throws Exception {
		String ret = "";
		
		ArrayList<AussomType> args = new ArrayList<AussomType>();
		ret += "OS Information\n";
		ret += "\tOS Arch: " + (getOsArch(env, args)).getValueString() + "\n";
		ret += "\tOS Name: " + (getOsName(env, args)).getValueString() + "\n";
		ret += "\tOS Version: " + (getOsVersion(env, args)).getValueString() + "\n";
		ret += "\tOS File Separator: " + (getFileSeparator(env, args)).getValueString() + "\n";
		
		ret += "\n";
		ret += "Java Information\n";
		ret += "\tJava Version: " + (getJavaVersion(env, args)).getValueString() + "\n";
		ret += "\tJava Vendor: " + (getJavaVendor(env, args)).getValueString() + "\n";
		ret += "\tJava Vendor Url: " + (getJavaVendorUrl(env, args)).getValueString() + "\n";
		ret += "\tJava Class Path: " + (getJavaClassPath(env, args)).getValueString() + "\n";
		ret += "\tJava Home: " + (getJavaHome(env, args)).getValueString() + "\n";
		
		ret += "\n";
		ret += "Aussom Information\n";
		ret += "\tAussom Interpreter Version: " + (getAussomVersion(env, args)).getValueString() + "\n";
		ret += "\tAussom Assembly: " + (getAssembly(env, args)).getValueString() + "\n";
		ret += "\tAussom Assembly Path: " + (getAssemblyPath(env, args)).getValueString() + "\n";
		
		ret += "\n";
		ret += "User Information\n";
		ret += "\tCurrent Path: " + (getCurrentPath(env, args)).getValueString() + "\n";
		ret += "\tHome Path: " + (getHomePath(env, args)).getValueString() + "\n";
		ret += "\tUser Name: " + (getUserName(env, args)).getValueString() + "\n";
		
		ret += "\n";
		
		return ret;
	}
	
	/*
	 * Environment functions
	 */
	public static AussomType getSysInfo(Environment env, ArrayList<AussomType> args) throws Exception {
		String ret = ASys.getSysInfo(env);
		return new AussomString(ret);
	}
	
	@SuppressWarnings("deprecation")
	public static String _getAssembly() throws Exception {
		String path = "";
		try {
			path = (new File(URLDecoder.decode(Universe.class.getProtectionDomain().getCodeSource().getLocation().getPath())).getAbsolutePath());
		} catch(Exception e) {
			/*
			if(runtime.getInstance().getOsType() == osType.MOBILE) {
				path = android.getInstance().getAssemblyPath();
			}
			*/
			throw e;
		}
		return path;
	}
	
	@SuppressWarnings("deprecation")
	public static String _getAssemblyPath() throws Exception {
		String path = "";
		try {
			path = (new File(URLDecoder.decode(Universe.class.getProtectionDomain().getCodeSource().getLocation().getPath())).getParent()) + System.getProperty("file.separator");
		} catch(Exception e) {
			/*
			if(runtime.getInstance().getOsType() == osType.MOBILE) {
				path = android.getInstance().getAssemblyPath();
			}
			*/
			throw e;
		}
		return path;
	}
	
	public static AussomType getOsArch(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("os.info.view")) {
			return new AussomString(System.getProperty("os.arch"));
		}
		return new AussomNull();
	}
	
	public static AussomType getOsName(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("os.info.view")) {
			return new AussomString(System.getProperty("os.name"));
		}
		return new AussomNull();
	}
	
	public static AussomType getOsVersion(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("os.info.view")) {
			return new AussomString(System.getProperty("os.version"));
		}
		return new AussomNull();
	}
	
	public static AussomType getFileSeparator(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("os.info.view")) {
			return new AussomString(System.getProperty("file.separator"));
		}
		return new AussomNull();
	}
	
	public static AussomType getLineSeparator(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("os.info.view")) {
			return new AussomString(System.getProperty("line.separator"));
		}
		return new AussomNull();
	}
	
	public static AussomType getJavaVersion(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("java.info.view")) {
			return new AussomString(System.getProperty("java.version"));
		}
		return new AussomNull();
	}
	
	public static AussomType getJavaVendor(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("java.info.view")) {
			return new AussomString(System.getProperty("java.vendor"));
		}
		return new AussomNull();
	}
	
	public static AussomType getJavaVendorUrl(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("java.info.view")) {
			return new AussomString(System.getProperty("java.vendor.url"));
		}
		return new AussomNull();
	}
	
	public static AussomType getJavaClassPath(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("java.classpath.view")) {
			return new AussomString(System.getProperty("java.class.path"));
		}
		return new AussomNull();
	}
	
	public static AussomType getJavaHome(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("java.home.view")) {
			return new AussomString(System.getenv("JAVA_HOME"));
		}
		return new AussomNull();
	}
	
	public static AussomType getAussomVersion(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("aussom.info.view")) {
			return new AussomString(Universe.getAussomVersion());
		}
		return new AussomNull();
	}
	
	public static AussomType getAssembly(Environment env, ArrayList<AussomType> args) throws Exception {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("aussom.info.view")) {
			return new AussomString(_getAssembly());
		}
		return new AussomNull();
	}
	
	public static AussomType getAssemblyPath(Environment env, ArrayList<AussomType> args) throws Exception {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("aussom.path.view")) {
			return new AussomString(_getAssemblyPath());
		}
		return new AussomNull();
	}
	
	public static AussomType getCurrentPath(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("current.path.view")) {
			return new AussomString(System.getProperty("user.dir"));
		}
		return new AussomNull();
	}
	
	public static AussomType getHomePath(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("home.path.view")) {
			return new AussomString(System.getProperty("user.home"));
		}
		return new AussomNull();
	}
	
	public static AussomType getUserName(Environment env, ArrayList<AussomType> args) {
		if ((Boolean)env.getEngine().getSecurityManager().getProperty("user.name.view")) {
			return new AussomString(System.getProperty("user.name"));
		}
		return new AussomNull();
	}
	
	/*
	public static AussomType getMainFilePath(Environment env, ArrayList<AussomType> args) {
		if(runtime.getInstance().getOsType() == osType.MOBILE)
			return new AussomString(runtime.getInstance().getMobilePath());
		else
			return sys.getCurrentPath(args);
	}
	*/
	
	/*
	 * Time functions
	 */
	public static AussomType getMills(Environment env, ArrayList<AussomType> args) {
		return new AussomInt(System.currentTimeMillis());
	}
	
	public static AussomType _sleep(Environment env, ArrayList<AussomType> args) throws InterruptedException {
		AussomType ret = new AussomNull();
		Thread.sleep(((AussomInt)args.get(0)).getValue());
		return ret;
	}
}
