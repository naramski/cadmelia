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

import net.nowina.cadmelia.stl.parser.ParseException;
import net.nowina.cadmelia.stl.parser.STLParser;

import java.io.*;

public class STLReader {

    public static void parse(File f, STLEventHandler handler) throws IOException, ParseException {
        try(FileInputStream in = new FileInputStream(f)) {
            parseAscii(in, handler);
        }
    }

    public static void parseAscii(InputStream in, STLEventHandler handler) throws IOException, ParseException {
        try (BufferedInputStream input = new BufferedInputStream(in)) {
            STLParser parser = new STLParser(input);
            parser.setEventHandler(handler);
            parser.Solid();
        }
    }

}
