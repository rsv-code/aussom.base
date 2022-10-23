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


package com.aussom.types;

import java.util.ArrayList;

import com.aussom.Environment;

/**
 * Implements functions that internal AussomObjects expect. If you are creating an 
 * external object, you can implement this interface to make your object act 
 * like a normal internal aussom object.
 * @author Austin Lehman
 */
public interface AussomTypeObjectInt {
	public AussomType toJson(Environment env, ArrayList<AussomType> args);
	public AussomType pack(Environment env, ArrayList<AussomType> args);
}
