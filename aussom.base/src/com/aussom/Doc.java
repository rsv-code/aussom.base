/*
 * Copyright 2021 Austin Lehman
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

import com.aussom.ast.astClass;
import com.aussom.ast.aussomException;
import com.aussom.types.*;

import java.io.File;

/**
 * Class that provides support for Aussom-Doc. Specifically this class
 * will generate documents in different formats. It also supports doc calls
 * from within Aussom code.
 */
public class Doc {
    /**
	 * Builds the aussomdoc for the provied script
	 * file provided and returns it as a Markdown
	 * string.
	 * @return A String with the aussomdoc in markdown format.
	 */
	public static String getAussomdocMarkdown(Engine Eng, String ScriptFile) throws aussomException {
		File sf = new File(ScriptFile);
		String ret = "# file: " + sf.getName() + "\n\n";

		for (String className : Eng.getClasses().keySet()) {
			astClass ac = Eng.getClasses().get(className);
			if (ac.getFileName().equals(ScriptFile)) {
				AussomMap cdoc = (AussomMap)ac.getAussomdoc();

				String staticStr = "";
				if (cdoc.getValue().get("isStatic").getNumericBool()) {
					staticStr = "`static` ";
				}
				String externStr = "";
				if (cdoc.getValue().get("isExtern").getNumericBool()) {
					externStr = "(extern: " + cdoc.getValue().get("externClassName").getValueString() + ") ";
				}
				String extendedStr = "";
				AussomList elist = (AussomList)cdoc.getValue().get("extendedClasses");
				if (elist.size() > 0) {
					extendedStr = "**extends: " + elist.getValue().get(0).getValueString();
					for (int i = 1; i < elist.getValue().size(); i++) {
						extendedStr += ", " + elist.getValue().get(i).getValueString();
					}
					extendedStr += "** ";
				}
				ret += "## class: " + cdoc.getValue().get("className").getValueString() + "\n";
				ret += "[" + cdoc.getValue().get("lineNumber").getNumericInt() + ":" + cdoc.getValue().get("colNumber").getNumericInt() + "] " + staticStr + externStr + extendedStr + "\n";
				ret += Doc.getAussomdocMarkdownMembers(cdoc);
				ret += Doc.getAussomdocMarkdownMethods(cdoc);


				ret += "\n\n";
			}
		}

		return ret;
	}

	/**
	 * Builds the aussomdoc members in markdown format and returns
	 * as a string.
	 * @param cdoc is a AussomMap object with the members.
	 * @return A String with the generated doc.
	 */
	private static String getAussomdocMarkdownMembers(AussomMap cdoc) {
		String ret = "";
		AussomList lst = (AussomList) cdoc.getValue().get("members");
		if (lst.getValue().size() > 0) {
			ret += "#### Members\n";
			for (int i = 0; i < lst.getValue().size(); i++) {
				AussomMap memb = (AussomMap)lst.getValue().get(i);
				ret += "- **" + memb.getValue().get("name").getValueString() + "**\n";

				if (memb.getValue().get("value").getType() != cType.cNull) {
					AussomMap docMap = (AussomMap)memb.getValue().get("value");
					AussomList docList = (AussomList)docMap.getValue().get("docList");
					ret += Doc.getAussomdocMarkdownDoclist(docList);
				}
			}
			ret += "\n";
		}
		return ret;
	}

	/**
	 * Builds the aussomdoc methods in markdown format and returns
	 * as a string.
	 * @param cdoc is a AussomMap object with the methods.
	 * @return A String with the generated doc.
	 */
	private static String getAussomdocMarkdownMethods(AussomMap cdoc) {
		String ret = "";
		AussomList lst = (AussomList) cdoc.getValue().get("methods");
		if (lst.getValue().size() > 0) {
			ret += "#### Methods\n";
			for (int i = 0; i < lst.getValue().size(); i++) {
				AussomMap methb = (AussomMap)lst.getValue().get(i);
				ret += "- **" + methb.getValue().get("name").getValueString() + "** (";

				AussomList margs = (AussomList)methb.getValue().get("args");
				ret += getAussomdocMarkdownMethodArgs(margs);

				ret += ")\n";
				if (methb.getValue().containsKey("aussomDoc")) {
					AussomMap docMap = (AussomMap)methb.getValue().get("aussomDoc");
					AussomList docList = (AussomList)docMap.getValue().get("docList");
					ret += Doc.getAussomdocMarkdownDoclist(docList);
				} else {
					ret += "\n";
				}
			}
			ret += "\n";
		}
		return ret;
	}

	/**
	 * Builds the aussomdoc function in markdown format and returns
	 * as a string.
	 * @param docList is a AussomList object with items from the function comment.
	 * @return A String with the generated doc.
	 */
	private static String getAussomdocMarkdownDoclist(AussomList docList) {
		String ret = "";
		for (AussomType cobj : docList.getValue()) {
			AussomMap docItem = (AussomMap) cobj;
			if (docItem.getValue().get("type").getValueString().toLowerCase().equals("annotation")) {
				ret += "\t- **@" + docItem.getValue().get("tagName").getValueString() + "** `" + docItem.getValue().get("tagValue").getValueString() + "` " + docItem.getValue().get("tagDescription").getValueString() + "\n";
			} else {
				// Text node
				ret += "\t> " + docItem.getValue().get("text").getValueString() + "\n";
			}
		}
		return ret;
	}

	/**
	 * Builds the aussomdoc method args in markdown format and returns
	 * as a string.
	 * @param margs is a AussomList object with the method args.
	 * @return A String with the generated doc.
	 */
	private static String getAussomdocMarkdownMethodArgs(AussomList margs) {
		String ret = "";
		if (margs.size() > 0) {
			String argsStr = "";
			for (int j = 0; j < margs.getValue().size(); j++) {
				AussomMap arg = (AussomMap) margs.getValue().get(j);
				String argStr = "";
				if (arg.contains("specifiedType")) {
					String type = arg.getValue().get("specifiedType").getValueString();
					if (!type.equals("undef")) {
						argStr += " " + type;
					}
				}
				if (arg.contains("name")) {
					argStr += " " + arg.getValue().get("name").getValueString();
				}
				if (arg.contains("type")) {
					String type = arg.getValue().get("type").getValueString();
					if (type.equals("etcetera")) {
						argStr += "...";
					}
				}

				if (arg.contains("valueType")) {
					AussomType valType = arg.getValue().get("value");
					String val = arg.getValue().get("value").getValueString();

					if (valType instanceof AussomString) {
						argStr += " = \"" + val + "\"";
					} else {
						argStr += " = " + val;
					}
				}

				if (!argStr.trim().equals("")) {
					if (j > 0) argsStr += ", ";
					argsStr += argStr.trim();
				}
			}
			ret += "`" + argsStr + "`";
		}
		return ret;
	}
}
