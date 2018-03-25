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

import javafx.embed.swing.JFXPanel;
import net.nowina.cadmelia.model.ModelConstruction;
import net.nowina.cadmelia.model.ModelFactoryBuilder;
import net.nowina.cadmelia.script.Script;
import net.nowina.cadmelia.script.ScriptScene;
import net.nowina.cadmelia.script.parser.ScriptParser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class SampleTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleTest.class);

    @BeforeClass
    public static void initJavaFX() {
        new JFXPanel();
    }

    @Parameterized.Parameter(0)
    public /* NOT private */ File scriptFile;

    @Parameterized.Parameters(name = "{0}")
    public static List<File> testFiles() {
        return Arrays.asList(new File("src/test/resources/scripts").listFiles((pathname) -> {
            if(pathname == null || pathname.getName() == null) {
                return false;
            }
            return pathname.getName().endsWith(".scad");
        }));
    }

    @Test
    public void test() throws Exception {

        File buildDir = new File("target");

        File stlFile = new File(buildDir, scriptFile.getName().substring(0, scriptFile.getName().length()-5) + ".stl");

        CompileToSTLApp.main(new String[]{scriptFile.getPath(), stlFile.getPath()});

    }

}
