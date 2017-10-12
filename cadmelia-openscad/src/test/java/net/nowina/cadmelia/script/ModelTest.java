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
package net.nowina.cadmelia.script;

import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.model.*;
import net.nowina.cadmelia.script.parser.ScriptParser;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class ModelTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelTest.class);

    @Test
    public void testModuleInModule() throws Exception {


        ScriptParser parser = new ScriptParser(new StringReader("hello(); module hello() { module world() { text(\"World\"); } text(\"Hello\"); world(); } text(\"!\"); "));
        Script script = parser.Script();

        ScriptScene scene = new ScriptScene(new ModelFactoryBuilder());
        scene.executeScript(script);

        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);
        ModelConstruction root = (ModelConstruction) scene.getRoot();

        root.print(writer);

        writer.close();
        LOGGER.info("CSG Tree :\n" + buffer.toString());
    }

    @Test
    public void testDefineInModule() throws Exception {


        ScriptParser parser = new ScriptParser(new StringReader("var = 2; hello(); module hello() { var = 1; circle(var); } circle(var)"));
        Script script = parser.Script();

        ScriptScene scene = new ScriptScene(new ModelFactoryBuilder());
        scene.executeScript(script);

        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);
        Union root = (Union) scene.getRoot();

        root.print(writer);

        writer.close();
        LOGGER.info("CSG Tree :\n" + buffer.toString());

        Assert.assertEquals(2, root.getElements().size());

    }

    @Test
    public void testCylinder() throws Exception {


        ScriptParser parser = new ScriptParser(new StringReader("cylinder(1,2,3);"));
        Script script = parser.Script();

        ScriptScene scene = new ScriptScene(new ModelFactoryBuilder());
        scene.executeScript(script);

        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);
        Cylinder root = (Cylinder) scene.getRoot();

        root.print(writer);

        writer.close();
        LOGGER.info("CSG Tree :\n" + buffer.toString());

        Assert.assertEquals(1, root.getHeight(), Vector.EPSILON);
        Assert.assertEquals(2, root.getBottomRadius(), Vector.EPSILON);
        Assert.assertEquals(3, root.getTopRadius(), Vector.EPSILON);

    }

    @Test
    public void testCube() throws Exception {


        ScriptParser parser = new ScriptParser(new StringReader("cube(1,true);"));
        Script script = parser.Script();

        ScriptScene scene = new ScriptScene(new ModelFactoryBuilder());
        scene.executeScript(script);

        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);
        Cube root = (Cube) scene.getRoot();

        root.print(writer);

        writer.close();
        LOGGER.info("CSG Tree :\n" + buffer.toString());

        Assert.assertEquals(1, root.getSizeX(), Vector.EPSILON);
        Assert.assertEquals(1, root.getSizeY(), Vector.EPSILON);
        Assert.assertEquals(1, root.getSizeZ(), Vector.EPSILON);
        Assert.assertEquals(true, root.isCentered());

    }

    @Test
    public void testEcho() throws Exception {


        ScriptParser parser = new ScriptParser(new StringReader("echo(a=3);"));
        parser.Script();

        parser = new ScriptParser(new StringReader("echo(version=version());"));
        parser.Script();

        parser = new ScriptParser(new StringReader("echo(a=sin(3));"));
        parser.Script();

        parser = new ScriptParser(new StringReader("echo(a=version());"));
        parser.Script();

    }

}
