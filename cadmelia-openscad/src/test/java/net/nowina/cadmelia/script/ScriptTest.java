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
import net.nowina.cadmelia.model.ModelFactoryBuilder;
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
        Assert.assertEquals(new Vector(1, 2, 3), op.getArg("v").evaluateAsVector(context));
        Assert.assertEquals(1, op.getOperations().size());
        Command c = op.getOperations().get(0);
        Assert.assertEquals("sphere", c.getName());

        op = parser.Chain();
        Assert.assertEquals("cube", op.getName());
        Assert.assertEquals(1, op.getArgCount());
        Assert.assertEquals(1d, op.getArg(0).evaluate(context).asDouble(), 10e-8);

    }

    @Test
    public void testDefineModule() throws Exception {

        ScriptContext context = new ScriptContext();
        ScriptParser parser = new ScriptParser(new StringReader("module mod1() { cube(1); }"));

        Module op = parser.Module();
        Assert.assertEquals(Module.class, op.getClass());
        Assert.assertEquals("mod1", op.getName());
        Assert.assertEquals(0, op.getParameters().size());
        Assert.assertEquals(1, op.getInstructions().size());
        Instruction i = op.getInstructions().get(0);
        Assert.assertEquals(Command.class, i.getClass());
        Command c = (Command) i;
        Assert.assertEquals("cube", c.getName());

        parser = new ScriptParser(new StringReader("module mod1(a=3) { cube(1); }; "));

        op = parser.Module();
        Assert.assertEquals(Module.class, op.getClass());
        Assert.assertEquals("mod1", op.getName());
        Assert.assertEquals(1, op.getParameters().size());
        Parameter a = op.getParameters().get(0);
        Assert.assertEquals("a", a.getName());
        Assert.assertEquals(3d, a.getDefaultValue().evaluate(new ScriptContext()).asDouble(), 1e-8);

        parser = new ScriptParser(new StringReader("module mod1(a) { cube(1); }; "));

        op = parser.Module();
        Assert.assertEquals(Module.class, op.getClass());
        Assert.assertEquals("mod1", op.getName());
        Assert.assertEquals(1, op.getParameters().size());
        a = op.getParameters().get(0);
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

        parser = new ScriptParser(new StringReader("module t(t, s = 18, style = \"\") { square([1, 2]); }"));
        s = parser.Script();

        parser = new ScriptParser(new StringReader("module curve() polygon([for (a = [ 0 : 0.004 : 1]) position(a)]);"));
        s = parser.Script();

    }


    @Test
    public void testFunction() throws Exception {

        ScriptParser parser = new ScriptParser(new StringReader("function ngon(num, r) = [for (i=[0:num-1], a=i*360/num) [ r*cos(a), r*sin(a) ]];"));
        Function f = parser.FunctionDef();

        parser = new ScriptParser(new StringReader("function v(a) = let (d = 360/num, v = floor((a+d/2)/d)*d) (r-rounding) * [cos(v), sin(v)];"));
        f = parser.FunctionDef();

        parser = new ScriptParser(new StringReader("function sum(values,s=0) = s == len(values) - 1 ? values[s] : values[s];"));
        f = parser.FunctionDef();

        parser = new ScriptParser(new StringReader("function sum(s) = s == true ? 1 : 1;"));
        f = parser.FunctionDef();

        parser = new ScriptParser(new StringReader("function sum(s) = s == true ? 1: 1+1;"));
        f = parser.FunctionDef();

        parser = new ScriptParser(new StringReader("function sum(s) = s == true ? 1+1: 1;"));
        f = parser.FunctionDef();

        parser = new ScriptParser(new StringReader("function sum(values,s=0) = s == len(values) - 1 ? values[s] : values[s] + sum(values,s+1);"));
        f = parser.FunctionDef();
    }

    @Test
    public void testLetInExpression() throws Exception {

        ScriptParser parser = new ScriptParser(new StringReader("let (num=len(vertices)) vertices[(i+1)%num]"));
        parser.Expression();

        parser = new ScriptParser(new StringReader("let (x = 7) x"));
        parser.Expression();

        parser = new ScriptParser(new StringReader("let (areas = 3, sol = 6) [ areas, sol ]"));
        parser.Expression();

        parser = new ScriptParser(new StringReader("let (areas = 3, sol = let (x = 7) x * 6) [ areas, sol ]"));
        parser.Expression();

        parser = new ScriptParser(new StringReader("let (num=20) [ for (i=[0:num-1]) i ]"));
        parser.Expression();

        parser = new ScriptParser(new StringReader("[ let (num=20) for (i=[0:num-1]) i ]"));
        parser.Expression();

        parser = new ScriptParser(new StringReader("[ for (i=[0:num-1]) i ]"));
        parser.Expression();

        parser = new ScriptParser(new StringReader("let (areas = [ for (i=[0:num-1]) i ]) x"));
        parser.Expression();

        parser = new ScriptParser(new StringReader("let (areas = [ let (num=20) for (i=[0:num-1]) i ]) areas"));
        parser.Expression();

        parser = new ScriptParser(new StringReader("let (areas = [let (num=len(vertices)) for (i=[0:num-1]) triarea(vertices[i], vertices[(i+1)%num]) ]) areas"));
        parser.Expression();
    }

    @Test
    public void testFor() throws Exception {

        ScriptParser parser = new ScriptParser(new StringReader("[0:1:4]"));
        RangeIterableDef def = parser.rangeDefinition();

        parser = new ScriptParser(new StringReader("[0:5]"));
        def = parser.rangeDefinition();

        parser = new ScriptParser(new StringReader("[0:len(hours)]"));
        def = parser.rangeDefinition();

        parser = new ScriptParser(new StringReader("[0:a-1]"));
        def = parser.rangeDefinition();

        parser = new ScriptParser(new StringReader("[0:f(a)-1]"));
        def = parser.rangeDefinition();

        parser = new ScriptParser(new StringReader("[0:(len(hours)-1)]"));
        def = parser.rangeDefinition();

        parser = new ScriptParser(new StringReader("for(i=[0:(len(hours)-1)]) echo(i);"));
        ForCommand f = (ForCommand) parser.Statement();
    }

    @Test
    public void functionCall() throws Exception {

        ScriptParser parser = new ScriptParser(new StringReader("dxf_dim(file = \"example009.dxf\", name = \"bodywidth\");\n"));
        parser.Statement();

        parser = new ScriptParser(new StringReader("dxf_dim(file = \"example009.dxf\", name = \"bodywidth\");\n"));
        parser.Function();

        parser = new ScriptParser(new StringReader("bodywidth = dxf_dim(file = \"example009.dxf\", name = \"bodywidth\");\n"));
        parser.Statement();

    }

    @Test
    public void testUse() throws Exception {

        ScriptParser parser = new ScriptParser(new StringReader("<test.txt>"));
        String filename = parser.readFilename();
        Assert.assertEquals("<test.txt>", filename);

        parser = new ScriptParser(new StringReader("use <test.txt>"));
        Instruction i1 = parser.useFile();

    }

    @Test
    public void testTranslateVector2D() throws Exception {

        ScriptParser parser = new ScriptParser(new StringReader("rotate(45) translate([ 0, -15 ]) square([ 100, 30 ]);"));
        Instruction instruction = parser.Statement();

        parser = new ScriptParser(new StringReader("translate(l * [cos(a), sin(a), 0]) { sphere(r = 2 * r); }"));
        Script script = parser.Script();

        ScriptScene scene = new ScriptScene(new ModelFactoryBuilder());

        ScriptContext ctx = scene.getContext();
        ctx.defineVariableValue("l", 1d);
        ctx.defineVariableValue("a", 30d);
        ctx.defineVariableValue("r", 2d);

        scene.executeScript(script);
    }

}
