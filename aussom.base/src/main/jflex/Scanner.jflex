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

import java.math.BigDecimal;
import java.math.BigInteger;

import java_cup.runtime.Symbol;
import com.aussom.stdlib.console;

%%
%class Lexer
%cup
%extends sym
%unicode
%line
%column
%eofval{ 
    return symbol( sym.EOF );
%eofval}

%{
  StringBuffer string = new StringBuffer();

  private Symbol symbol(int sym) {
    return new Symbol(sym, yyline+1, yycolumn+1);
  }
  
  private Symbol symbol(int sym, Object val) {
    return new Symbol(sym, yyline+1, yycolumn+1, val);
  }
  
  private void error(String message) {
    console.get().err("Error at line "+(yyline+1)+", column "+(yycolumn+1)+" : "+message);
  }
%} 

white_space 	= [ \t\f]
/* number 			= "-"? ({white_space}*)? [0-9]+ ("." [0-9]+)? */
number 			= [0-9]+ ("." [0-9]+)?
letter			= [A-Za-z]
digit			= [0-9]
underscore		= [_]
alphanumeric    = {letter}|{digit}
//identifier		= {letter}({alphanumeric}|{underscore})*
identifier		= ({alphanumeric}|{underscore})*
//var				= "$"+{identifier}
new_line 		= \r\n;

%state AUSSOM_DOC
%state C_COMMENT
%state STRING
%state STRING_FRMT
%state STRING_LIT

%%

/* single line comment */
<YYINITIAL> "//"[^\r\n]*						{ /* ignore */ }

/* keywords */
<YYINITIAL> "class"					{ return symbol(sym.CLASSDEF); }
<YYINITIAL> "enum"					{ return symbol(sym.ENUM); }
<YYINITIAL> "include"				{ return symbol(sym.INCLUDE); }
<YYINITIAL> "public"				{ return symbol(sym.PUBLIC); }
<YYINITIAL> "protected"				{ return symbol(sym.PROTECTED); }
<YYINITIAL> "private"				{ return symbol(sym.PRIVATE); }
<YYINITIAL> "static"				{ return symbol(sym.STATIC); }
<YYINITIAL> "extern"				{ return symbol(sym.EXTERN); }
<YYINITIAL> "return"				{ return symbol(sym.RETURN); }
<YYINITIAL> "try"					{ return symbol(sym.TRY); }
<YYINITIAL> "catch"					{ return symbol(sym.CATCH); }
<YYINITIAL> "throw"					{ return symbol(sym.THROW); }
<YYINITIAL> "new"					{ return symbol(sym.NEW); }
<YYINITIAL> "null"					{ return symbol(sym.NULL); }
<YYINITIAL> "if"					{ return symbol(sym.IF); }
<YYINITIAL> "else"					{ return symbol(sym.ELSE); }
<YYINITIAL> "switch"				{ return symbol(sym.SWITCH); }
<YYINITIAL> "case"					{ return symbol(sym.CASE); }
<YYINITIAL> "default"				{ return symbol(sym.DEFAULT); }
<YYINITIAL> "while"					{ return symbol(sym.WHILE); }
<YYINITIAL> "break"					{ return symbol(sym.BREAK); }
<YYINITIAL> "for"					{ return symbol(sym.FOR); }
<YYINITIAL> "instanceof"			{ return symbol(sym.INSTANCEOF); }

<YYINITIAL> ";"						{ return symbol(sym.SEMI); }
<YYINITIAL> ":"						{ return symbol(sym.COLON); }
<YYINITIAL> ","						{ return symbol(sym.COMMA); }
<YYINITIAL> "("						{ return symbol(sym.LPAREN); }
<YYINITIAL> ")"						{ return symbol(sym.RPAREN); }
<YYINITIAL> "{"						{ return symbol(sym.LBRACKET); }
<YYINITIAL> "}"						{ return symbol(sym.RBRACKET); }
<YYINITIAL> "["						{ return symbol(sym.LBRACE); }
<YYINITIAL> "]"						{ return symbol(sym.RBRACE); }

<YYINITIAL> "="						{ return symbol(sym.EQ); }
<YYINITIAL> "+="					{ return symbol(sym.PLEQ); }
<YYINITIAL> "-="					{ return symbol(sym.MIEQ); }
<YYINITIAL> "*="					{ return symbol(sym.MUEQ); }
<YYINITIAL> "/="					{ return symbol(sym.DIEQ); }
<YYINITIAL> "%="					{ return symbol(sym.MODULUSEQ); }
<YYINITIAL> "++"					{ return symbol(sym.PLPL); }
<YYINITIAL> "--"					{ return symbol(sym.MIMI); }
<YYINITIAL> "+"						{ return symbol(sym.PL); }
<YYINITIAL> "-"						{ return symbol(sym.MI); }
<YYINITIAL> "*"						{ return symbol(sym.MU); }
<YYINITIAL> "/"						{ return symbol(sym.DI); }
<YYINITIAL> "%"						{ return symbol(sym.MODULUS); }

<YYINITIAL> "=="					{ return symbol(sym.EQEQ); }
<YYINITIAL> "!="					{ return symbol(sym.NOTEQ); }
<YYINITIAL> "<"						{ return symbol(sym.LT); }
<YYINITIAL> ">"						{ return symbol(sym.GT); }
<YYINITIAL> "<="					{ return symbol(sym.LTEQ); }
<YYINITIAL> ">="					{ return symbol(sym.GTEQ); }
<YYINITIAL> "!"						{ return symbol(sym.NOT); }
<YYINITIAL> "&&"					{ return symbol(sym.AND); }
<YYINITIAL> "||"					{ return symbol(sym.OR); }
<YYINITIAL> "true"					{ return symbol(sym.BOOL, true); }
<YYINITIAL> "false"					{ return symbol(sym.BOOL, false); }
<YYINITIAL> "..."					{ return symbol(sym.ETCETERA); }
<YYINITIAL> "::"					{ return symbol(sym.CALLBACK); }
<YYINITIAL> "#"						{ return symbol(sym.COUNT); }
<YYINITIAL> "@="					{ return symbol(sym.INSERT); }

<YYINITIAL> "."						{ return symbol(sym.DOT); }

<YYINITIAL>
{
    "/**"         			{ string.setLength(0); yybegin(AUSSOM_DOC); }
	"/*"         			{ yybegin(C_COMMENT); }
	[\"]{3}					{ string.setLength(0); yybegin(STRING_FRMT); }
	\"						{ string.setLength(0); yybegin(STRING); }
	\'						{ string.setLength(0); yybegin(STRING_LIT); }
	
	/* Just slash n for *nix */
	"\n"					{ /* ignore */ }
	"\r\n"					{ /* ignore */ }
	{white_space}			{ /* ignore */ }
	{number}				{
								if(yytext().contains("."))
								{
									BigDecimal val = null;
									try{ val = new BigDecimal(yytext()); }
									catch(NumberFormatException e)
									{
										error("Number format exception <" + yytext() + ">");
									}
									return symbol(sym.DOUBLE, val);
								}
								else
								{
									BigInteger val = null;
									try{ val = new BigInteger(yytext()); }
									catch(NumberFormatException e)
									{
										error("Number format exception <" + yytext() + ">");
									}
									return symbol(sym.INT, val);
								}
							}
	{identifier}			{ return symbol(sym.IDENT, yytext()); }
/*	{var}					{ return symbol(sym.VAR, yytext()); } */
}

<STRING_FRMT>
{
	[\"]{3}					{
								yybegin(YYINITIAL); 
								return symbol(sym.STRING, string.toString()); 
							}
	\\\"\\\"\\\"			{ string.append("\"\"\""); }
	\t						{ string.append('\t'); }
	\n						{ string.append('\n'); }
	\r						{ string.append('\r'); }
	\\\\					{ string.append("\\"); }
	.						{ string.append( yytext() ); }
}

<STRING>
{
	\"						{
								yybegin(YYINITIAL); 
								return symbol(sym.STRING, 
								string.toString()); 
							}
	[^\n\r\"\\]+			{ string.append( yytext() ); }
	[\t\r\n]+				{ /* Do nothing. */ }
	\\t						{ string.append('\t'); }
	\\n						{ string.append('\n'); }
	
	\\r						{ string.append('\r'); }
	\\\"					{ string.append('\"'); }
	\\\\					{ string.append("\\"); }
}

<STRING_LIT>
{
	\'						{
								yybegin(YYINITIAL); 
								return symbol(sym.STRING, string.toString()); 
							}
	[^\n\r\'\\]+			{ string.append( yytext() ); }
	[\t\r\n]+				{ /* Do nothing. */ }
	\\t						{ string.append('\t'); }
	\\n						{ string.append('\n'); }
	
	\\r						{ string.append('\r'); }
	\\\'					{ string.append('\''); }
	\\\\					{ string.append("\\"); }
}

<AUSSOM_DOC>
{
	[^*\n]*					{ string.append( yytext() ); }
	"*"+[^*/\n]*			{ string.append( yytext() ); }
	"\n"					{ string.append('\n'); }
	"*"+"/"					{
	                            yybegin(YYINITIAL);
	                            return symbol(sym.AUSSOM_DOC, string.toString());
	                        }
}

<C_COMMENT>
{
	[^*\n]*					{ /* ignore */ }
	"*"+[^*/\n]*			{ /* ignore */ }
	"\n"					{ /* ignore */ }
	"*"+"/"					{ yybegin(YYINITIAL); }
}

/* error fallback */
.						{  /* throw new Error("Illegal character <" + String.ValueOf((int)yytext()) + ">");*/
							error("Illegal character <" + String.valueOf((int)yytext().charAt(0)) + ">");
						}
