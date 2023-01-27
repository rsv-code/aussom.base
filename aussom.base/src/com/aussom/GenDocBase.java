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

import java.io.File;

public class GenDocBase {
    public static void main(String[] args) throws Exception {
        // Generates all the docs
        genDoc("src/com/aussom/stdlib/aus/cunit.aus", "doc");
        genDoc("src/com/aussom/stdlib/aus/lang.aus", "doc");
        genDoc("src/com/aussom/stdlib/aus/math.aus", "doc");
        genDoc("src/com/aussom/stdlib/aus/reflect.aus", "doc");
        genDoc("src/com/aussom/stdlib/aus/sys.aus", "doc");

    }

    private static void genDoc(String inFile, String outDir) throws Exception {
        File ifile = new File(inFile);

        if (ifile.exists()) {
            // Ensure outDir exists
            buildOutDir(outDir);

            String aussomFile = ifile.getName();
            console.get().info("Now to generate doc for file '" + aussomFile + "'.");
            String outFile = aussomFile + ".md";
            if (!outDir.trim().equals(""))
                outFile = outDir + "/" + outFile;
            Util.write(outFile, Main.getAussomdocMarkdown(inFile), false);
            console.get().info("Wrote doc to '" + outFile + "'.");
        } else {
            console.get().err("Provided input file '" + inFile + "' wasn't found.");
        }
    }

    private static void buildOutDir(String dirName) {
        File outDir = new File(dirName);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
    }
}
