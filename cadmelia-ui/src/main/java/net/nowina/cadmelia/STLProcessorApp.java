/*
 * Copyright 2017 David Naramski.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *      http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.nowina.cadmelia;

import net.nowina.cadmelia.stl.STLAnalyser;
import net.nowina.cadmelia.stl.STLWriter;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class STLProcessorApp {

    public static void main(String[] args) throws Exception {

        Options options = new Options();
        options.addOption("biggest", "Clean the input file and keep only the biggest object (in term of faces count)");
        options.addRequiredOption("i", "input", true, "Input file STL");
        options.addRequiredOption("o", "output", true, "Output file STL");

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            File fileInput = new File(cmd.getOptionValue("input"));
            File fileOutput = new File(cmd.getOptionValue("output"));

            if (cmd.hasOption("biggest")) {
                biggest(fileInput, fileOutput);
            }

        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("camelia", options);
        }
    }

    public static void biggest(File input, File output) throws Exception {

        try (FileInputStream in = new FileInputStream(input)) {
            STLAnalyser analyser = new STLAnalyser();
            analyser.readSTL(in);
            List<TriangleMesh> solids = analyser.toPolyhedrons();

            System.out.println("There is " + solids.size() + " detected solids");
            Collections.sort(solids, Comparator.comparing(TriangleMesh::getPolygonCount));
            TriangleMesh biggest = solids.get(solids.size() - 1);
            System.out.println("Biggest one has " + biggest.polygons.size() + " polygons");

            try (PrintWriter w = new PrintWriter(output)) {
                STLWriter writer = new STLWriter();
                writer.write(biggest, w);
            }

        }

    }


}
