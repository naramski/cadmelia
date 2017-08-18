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
package net.nowina.cadmelia.stl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

public class STLReader {

    public static void parse(File f, STLEventHandler handler) throws IOException, ParseException {
        parseAscii(f, handler);
    }

    private static void parseAscii(File f, STLEventHandler handler) throws IOException, ParseException {
        try (BufferedReader in = new BufferedReader(new FileReader(f))) {
            String line = null;
            while ((line = in.readLine()) != null) {
                String[] numbers = line.trim().split("\\s+");
                if (numbers[0].equals("vertex")) {
                    float x = parseFloat(numbers[1]);
                    float y = parseFloat(numbers[2]);
                    float z = parseFloat(numbers[3]);
                    handler.onVertex(x, y, z);
                } else if (numbers[0].equals("facet") && numbers[1].equals("normal")) {
                    float nx = parseFloat(numbers[2]);
                    float ny = parseFloat(numbers[3]);
                    float nz = parseFloat(numbers[4]);
                }
            }
        }
    }

    private static float parseFloat(String string) throws ParseException {
        return Float.parseFloat(string);
    }

}
