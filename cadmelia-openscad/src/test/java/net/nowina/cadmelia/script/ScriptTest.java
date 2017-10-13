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
import net.nowina.cadmelia.script.parser.ScriptParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.StringReader;

public class ScriptTest {

    @Test
    public void testSphere2() throws Exception {

        try (FileInputStream in = new FileInputStream("src/test/resources/scripts/sphere.scad")) {
            ScriptParser parser = new ScriptParser(in);
            parser.Command();
        }

        try (FileInputStream in = new FileInputStream("src/test/resources/scripts/sphere.scad")) {
            ScriptParser parser = new ScriptParser(in);
            parser.Chain();
        }

    }

    @Test
    public void testTerminalCommand() throws Exception {

        ScriptParser parser = new ScriptParser(new StringReader("translate(v=[1,2,3]) sphere(2)"));
        Command op = parser.Chain();
        Assert.assertEquals(Command.class, op.getClass());
        Assert.assertEquals("translate", op.getName());
        Assert.assertEquals(1, op.getOperations().size());
        Command c = op.getOperations().get(0);
        Assert.assertEquals("sphere", c.getName());
    }

    @Test
    public void testTwoCommands() throws Exception {

        ScriptContext context = new ScriptContext();
        ScriptParser parser = new ScriptParser(new StringReader("translate(v=[1,2,3]) sphere(2); cube(1);"));

        Command op = parser.Chain();
        Assert.assertEquals(Command.class, op.getClass());
        Assert.assertEquals("translate", op.getName());
        Assert.assertEquals(1, op.getArgCount());
        Assert.assertEquals(new Vector(1, 2, 3), op.getArg("v").evaluate(context));
        Assert.assertEquals(1, op.getOperations().size());
        Command c = op.getOperations().get(0);
        Assert.assertEquals("sphere", c.getName());

        op = parser.Chain();
        Assert.assertEquals("cube", op.getName());
        Assert.assertEquals(1, op.getArgCount());
        Assert.assertEquals(1d, op.getArg(0).evaluate(context));

    }

    @Test
    public void testDefineModule() throws Exception {

        ScriptContext context = new ScriptContext();
        ScriptParser parser = new ScriptParser(new StringReader("module mod1() { cube(1); }"));

        Module op = parser.Module();
        Assert.assertEquals(Module.class, op.getClass());
        Assert.assertEquals("mod1", op.getName());
        Assert.assertEquals(0, op.getArgs().size());
        Assert.assertEquals(1, op.getInstructions().size());
        Instruction i = op.getInstructions().get(0);
        Assert.assertEquals(Command.class, i.getClass());
        Command c = (Command) i;
        Assert.assertEquals("cube", c.getName());

        parser = new ScriptParser(new StringReader("module mod1(a=3) { cube(1); }; "));

        op = parser.Module();
        Assert.assertEquals(Module.class, op.getClass());
        Assert.assertEquals("mod1", op.getName());
        Assert.assertEquals(1, op.getArgs().size());
        Parameter a = op.getArgs().get(0);
        Assert.assertEquals("a", a.getName());
        Assert.assertEquals(3d, a.getDefaultValue());

        parser = new ScriptParser(new StringReader("module mod1(a) { cube(1); }; "));

        op = parser.Module();
        Assert.assertEquals(Module.class, op.getClass());
        Assert.assertEquals("mod1", op.getName());
        Assert.assertEquals(1, op.getArgs().size());
        a = op.getArgs().get(0);
        Assert.assertEquals("a", a.getName());
        Assert.assertEquals(null, a.getDefaultValue());

    }

    @Test
    public void testHull() throws Exception {

        ScriptContext context = new ScriptContext();
        ScriptParser parser = new ScriptParser(new StringReader("hull() { mod1(); cube(); } sphere();"));

        Command op = parser.Chain();
        Assert.assertEquals(Command.class, op.getClass());
        Assert.assertEquals("hull", op.getName());
        Assert.assertEquals(0, op.getArgCount());
        Assert.assertEquals(2, op.getOperations().size());
        Command c1 = op.getOperations().get(0);
        Assert.assertEquals(Command.class, c1.getClass());
        Command c2 = op.getOperations().get(1);
        Assert.assertEquals(Command.class, c2.getClass());

        op = parser.Command();
        Assert.assertEquals("sphere", op.getName());

    }

    @Test
    public void testText() throws Exception {

        ScriptContext context = new ScriptContext();
        ScriptParser parser = new ScriptParser(new StringReader("text(\"CADmelia\", size = 20);"));

        Command op = parser.Command();

        Assert.assertEquals(Command.class, op.getClass());
        Assert.assertEquals("text", op.getName());
        Assert.assertEquals(2, op.getArgCount());

        String text = op.getFirstUnamedArg().evaluateAsString(context);
        Assert.assertEquals("CADmelia", text);

    }

    @Test
    public void testText2() throws Exception {

        Assert.assertEquals("Hello World !", new ScriptParser(new StringReader("\"Hello World !\"")).String());
        Assert.assertEquals("Hello\t World !", new ScriptParser(new StringReader("\"Hello\t World !\"")).String());

    }

    @Test
    public void testMul() throws Exception {

        ScriptParser parser = new ScriptParser(new StringReader("95/100*135"));

        Expression op = parser.Expression();

    }

    @Test
    public void testModule() throws Exception {

        ScriptParser parser = new ScriptParser(new StringReader("module test() { offset(.5) square(); }"));
        Script s = parser.Script();

        parser = new ScriptParser(new StringReader("module test() offset(.5) square();"));
        s = parser.Script();

        parser = new ScriptParser(new StringReader("module curve() polygon();"));
        s = parser.Script();

        parser = new ScriptParser(new StringReader("module curve() polygon([for (a = [ 0 : 0.004 : 1]) position(a)]);"));
        s = parser.Script();

    }

    @Test
    public void testUse() throws Exception {

        ScriptParser parser = new ScriptParser(new StringReader("<test.txt>"));
        String filename = parser.readFilename();
        Assert.assertEquals("<test.txt>", filename);

        parser = new ScriptParser(new StringReader("use <test.txt>"));
        Instruction i1 = parser.useFile();

    }

}
