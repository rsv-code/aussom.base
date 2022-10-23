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

package com.aussom.ast;

public enum expType {
	UNDEF,
	ASSIGNMENT,
	ADD,
	SUBTRACT,
	MULTIPLY,
	DIVIDE,
	MODULUS,
	EQEQ,
	NOTEQ,
	LT,
	GT,
	LTEQ,
	GTEQ,
	NOT,
	AND,
	OR,
	COUNT,
	INSERT,
	PLEQ,
	MIEQ,
	MUEQ,
	DIEQ,
	PLPL,
	MIMI,
	INSTANCEOF,
	INCLUDE
}
