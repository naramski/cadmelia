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

import net.nowina.cadmelia.construction.BuilderFactory;
import net.nowina.cadmelia.construction.Solid;
import net.nowina.cadmelia.script.Script;
import net.nowina.cadmelia.script.ScriptScene;
import net.nowina.cadmelia.script.parser.ScriptParser;
import net.nowina.cadmelia.shape.jts_clipper.JTSClipperShapeBuilder;
import net.nowina.cadmelia.solid.bspcsg.FactoryBuilder;
import net.nowina.cadmelia.stl.STLWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;

public class CompileToSTLApp {

    public static void main(String[] argv) throws Exception {
        BuilderFactory.registerShapeBuilder(new JTSClipperShapeBuilder());
        BuilderFactory.registerSolidBuilder(new FactoryBuilder().build());
        BuilderFactory factory = BuilderFactory.getInstance();
        ScriptScene scene = null;
        if (argv.length > 3 && argv[3].equals("-opt")) {
            scene = new ScriptScene(true, factory);
        } else {
            scene = new ScriptScene(factory);
        }

        try (InputStream in = new FileInputStream(argv[1]);
             PrintWriter out = new PrintWriter(new FileOutputStream(argv[2]))) {
            ScriptParser parser = new ScriptParser(in);
            Script script = parser.Script();
            scene.executeScript(script);

            STLWriter writer = new STLWriter();
            writer.write((Solid) scene.getRoot(), out);
        }
    }

}
