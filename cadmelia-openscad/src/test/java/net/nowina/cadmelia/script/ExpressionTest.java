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

import net.nowina.cadmelia.script.parser.ParseException;
import net.nowina.cadmelia.script.parser.ScriptParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class ExpressionTest {

    @Test
    public void test1() throws Exception {
        ScriptContext context = new ScriptContext();
        verify("4", 4d, context);
        verify("-4", -4d, context);
        verify("4 + 3", 7d, context);
        verify("-4 + 3", -1d, context);
        verify("4 + 3 - 1", 6d, context);
        verify("(4 - 3) + 1", 2d, context);
        verify("4 - (3 + 1)", 0d, context);
        verify("4 - 3 + 1", 2d, context);
        verify("4 - 3 * 3", -5d, context);
        verify(".5", .5d, context);
        verify("95/100*135", 128.25, context);
    }

    private void verify(String expression, double expected, ScriptContext context) throws ParseException {
        ScriptParser parser = new ScriptParser(new StringReader(expression));
        Expression ex = parser.Expression();
        double value = ex.evaluateAsDouble(context);
        Assert.assertEquals("Error evaluating '" + expression + "'", expected, value, 0.0001);
    }

    @Test
    public void testBooleans() throws Exception {
        verifyBoolean("1 == 1", true);
        verifyBoolean("1 == 3", false);
        verifyBoolean("true == true", true);
        verifyBoolean("true != true", false);
    }

    private void verifyBoolean(String expression, boolean expected) throws ParseException {
        ScriptParser parser = new ScriptParser(new StringReader(expression));
        Expression ex = parser.Expression();
        boolean value = ex.evaluateAsBoolean(new ScriptContext());
        Assert.assertEquals("Error evaluating '" + expression + "'", expected, value);
    }

    @Test
    public void testIf() throws Exception {
        ScriptParser parser = new ScriptParser(new StringReader("if(true) {cube(1); }; if(true) {sphere(1);};"));
        Script script = parser.Script();

        Assert.assertEquals(2, script.getInstructions().size());

        parser = new ScriptParser(new StringReader("if(true) {cube(1); } if(true) {sphere(1);}"));
        script = parser.Script();

        Assert.assertEquals(2, script.getInstructions().size());

        parser = new ScriptParser(new StringReader("if(true) cube(1); if(true) {sphere(1);}"));
        script = parser.Script();

        Assert.assertEquals(2, script.getInstructions().size());

        parser = new ScriptParser(new StringReader("translate([1,1,1]) { if(true) cube(1); if(true) {sphere(1);}}"));
        script = parser.Script();

        Assert.assertEquals(1, script.getInstructions().size());

        parser = new ScriptParser(new StringReader("translate([1,1,1]) if(true) cube(1) else if(true) {sphere(1);}"));
        script = parser.Script();

        Assert.assertEquals(1, script.getInstructions().size());

        parser = new ScriptParser(new StringReader("translate([1,1,1]) if(true) cube(1); if(true) {sphere(1);}"));
        script = parser.Script();

        Assert.assertEquals(2, script.getInstructions().size());

        parser = new ScriptParser(new StringReader("echo(theta);\n\nif(part==2) sleeve(2);"));
        script = parser.Script();

        Assert.assertEquals(2, script.getInstructions().size());

    }

    @Test
    public void testChain() throws Exception {
        ScriptParser parser = new ScriptParser(new StringReader("sphere(1); cube(2);"));
        Script script = parser.Script();

        Assert.assertEquals(2, script.getInstructions().size());

        parser = new ScriptParser(new StringReader("translate([1,1,1]) cube(1); sphere(2);"));
        script = parser.Script();

        Assert.assertEquals(2, script.getInstructions().size());

        parser = new ScriptParser(new StringReader("cube(3); translate([1,1,1]) cube(1);"));
        script = parser.Script();

        Assert.assertEquals(2, script.getInstructions().size());

        parser = new ScriptParser(new StringReader("cube(3); translate([1,1,1]) cube(1)"));
        script = parser.Script();

        Assert.assertEquals(2, script.getInstructions().size());

        parser = new ScriptParser(new StringReader("cube(1); translate([1,1,1]) rotate([1,1,1]) sphere(2); cube(3);"));
        script = parser.Script();

        Assert.assertEquals(3, script.getInstructions().size());

        parser = new ScriptParser(new StringReader("cube(1); translate([1,1,1]) rotate([1,1,1]) { sphere(2); cube(3); }"));
        script = parser.Script();

        Assert.assertEquals(2, script.getInstructions().size());

        parser = new ScriptParser(new StringReader("cube(1); translate([1,1,1]) rotate([1,1,1]) { sphere(2); cube(3); }; cube(1); "));
        script = parser.Script();

        Assert.assertEquals(3, script.getInstructions().size());

        parser = new ScriptParser(new StringReader("cube(1); translate([1,1,1]) rotate([1,1,1]) { sphere(2); cube(3); } cube(1); "));
        script = parser.Script();

        Assert.assertEquals(3, script.getInstructions().size());

    }
}
