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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Aussom interpreter utility functions.
 * @author austin
 */
public class Util {
	
	/**
	 * Reads a file with the provided file name and returns it as 
	 * a String.
	 * @param FileName is a String with the file name to read.
	 * @return A String with the file contents.
	 * @throws IOException on IO exception.
	 */
	public static String read(String FileName) throws IOException
	{	
		FileReader fr = null;
		try {
			fr = new FileReader(FileName);
			
			int len = -1;
			char[] buff = new char[4096];
			final StringBuffer buffer = new StringBuffer();
			while ((len = fr.read(buff)) > 0) { buffer.append(buff, 0, len); }
	        
	        return buffer.toString();
	    } catch (IOException e) {
			throw e;
		} finally {
			if(fr != null) {
		        try { fr.close(); }
		        catch (IOException e) { throw e; }
			}
	    }
	}
	
	/**
	 * Writes the provided String to file. If append is set to true, it will append 
	 * the text to file, otherwise it will replace it if the file already exists.
	 * @param FileName is a String with the file name to save.
	 * @param Data is a String with the text data to write.
	 * @param Append is a boolean with true for append and false for not.
	 * @throws IOException on IO exception.
	 */
	public static void write(String FileName, String Data, boolean Append) throws IOException {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(FileName, Append));
			bw.write(Data);
		} catch (IOException e) {
			throw e;
		} finally {
			if(bw != null) {
		        try { bw.close(); }
		        catch (IOException e) { throw e; }
			}
	    }
	}
	
	/**
	 * Joins the provided list of strings with the provided glue string.
	 * @param parts is an ArrayList of Strings to join.
	 * @param Glue is a String with the glue.
	 * @return The combined String.
	 */
	public static String join(ArrayList<String> parts, String Glue) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < parts.size(); i++) {
			if (i > 0) {
				sb.append(Glue);
			}
			sb.append(parts.get(i));
		}
		return sb.toString();
	}
	
	/**
	 * Loads a file resource with the provided path 
	 * within the project/jar and returns it as a String.
	 * @param ResourceName is a String with the resource to get.
	 * @return A String with the contents of the resource.
	 */
	public static String loadResource(String ResourceName) {
		String ret = "";
		
		InputStream in = Util.class.getResourceAsStream(ResourceName); 
		InputStreamReader rdr = new InputStreamReader(in);
		try {
			int len = -1;
			char[] buff = new char[4096];
			final StringBuffer buffer = new StringBuffer();
			while ((len = rdr.read(buff)) > 0) { buffer.append(buff, 0, len); }
	        
	        ret = buffer.toString();
		} catch (IOException e) {
			System.err.println("Lang.loadResource(): Failed to load resource '" + ResourceName + "'.");
		} finally {
			if(rdr != null) {
		        try { rdr.close(); }
		        catch (IOException e) { }
			}
	    }
		
		return ret;
	}
}
