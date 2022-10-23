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

import com.aussom.stdlib.console;
import org.apache.commons.cli.*;

import java.util.List;

/**
 * Main class implements program entry point for command line 
 * usage.
 * @author austin
 */
public class Main {
	/**
	 * Command line program entry point.
	 * @param args is an array of Strings with the command line arguments.
	 * @throws Exception on failure.
	 */
	public static void main(String[] args) throws Exception {
		// create the command line parser
		CommandLineParser parser = new DefaultParser();

		// create the Options
		Options options = new Options();

		// Help option
		Option help = new Option("h", "help", false, "print this message" );
		options.addOption(help);

		// Aussomdoc option
		Option doc = new Option("d", "doc", false, "generate aussomdoc for file");
		options.addOption(doc);

		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			// has the buildfile argument been passed?
			if(line.hasOption("help")) {
				printHelp(options);
				return;
			} else if (line.hasOption("doc")) {
				if (line.getArgList().size() == 1) {
					String aussomFile = line.getArgList().get(0);
					console.get().info("Now to generate doc for file '" + aussomFile + "'.");
					String outFile = aussomFile + ".md";
					Util.write(outFile, getAussomdocMarkdown(aussomFile), false);
					console.get().info("Wrote doc to '" + outFile + "'.");
				} else if (line.getArgList().size() > 1) {
					console.get().err("Too many arguments provided, expecting just one script file or none at all.\n");
					printHelp(options);
				}

			} else {
				List<String> pargs = line.getArgList();
				if (pargs.size() == 0) {
					console.get().err("Expecting aussom script file, but no arguments found.\n");
					printHelp(options);
				} else if (pargs.size() > 1) {
					console.get().err("Too many arguments provided, expecting just one script file.\n");
					printHelp(options);
				} else {
					// We nailed it, let's run the file
					String ScriptFile = pargs.get(0);
					runFile(ScriptFile);
				}
			}
		}
		catch( ParseException exp ) {
			System.out.println("CLI parse exception:" + exp.getMessage());
		}
	}

	public static void runFile(String ScriptFile) throws Exception {
		// Create a new Aussom engine.
		Engine eng = new Engine(new DefaultSecurityManagerImpl());

		// Sets debug output to true.
		// eng.setDebug(true);

		// Add resource include path for testing.
		eng.addResourceIncludePath("/com/aussom/stdlib/ca/");

		// Parse the provided file name.
		eng.parseFile(ScriptFile);

		// Prints the full parsed abastract syntax tree of all the sources.
		//System.out.println(eng.toString());

		// Attempt to run the code.
		eng.run();
	}

	public static String getAussomdocMarkdown(String ScriptFile) throws Exception {
		// Create a new Aussom engine.
		Engine eng = new Engine(new DefaultSecurityManagerImpl());

		// Sets debug output to true.
		// eng.setDebug(true);

		// Add resource include path for testing.
		eng.addResourceIncludePath("/com/aussom/stdlib/ca/");

		// Parse the provided file name.
		eng.parseFile(ScriptFile);

		// Build the doc and return it.
		return Doc.getAussomdocMarkdown(eng, ScriptFile);
	}

	public static void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		printInfo();
		formatter.printHelp("aussom [options] <aussom-file>", options);
	}

	/**
	 * Prints the application info to standard output.
	 */
	public static void printInfo() {
		String rstr = "";
		rstr += "Aussom Version " + Universe.getAussomVersion()  + "\n";
		rstr += "Copyright 2020 Austin Lehman\n";
		rstr += "Apache License Version 2\n";
		System.out.println(rstr);
	}
}