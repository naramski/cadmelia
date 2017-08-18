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

}
