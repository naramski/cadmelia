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

import net.nowina.cadmelia.script.expression.InlineForExpression;
import net.nowina.cadmelia.script.parser.ScriptParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class RangeTest {

    @Test
    public void test1() throws Exception {
        Range r = new Range(1, 10, 1);
        int count = 0;
        double total = 0;
        for(Expression d : r) {
            count++;
            total += d.evaluateAsDouble(new ScriptContext());
        }
        Assert.assertEquals(10, count);
        Assert.assertEquals(55, (int) total);
    }

    @Test
    public void test2() throws Exception {

        ScriptContext context = new ScriptContext();
        context.defineVariableValue("num", 10);
        ScriptParser parser = new ScriptParser(new StringReader("[for (a=[0:num-1]) [ a, a*2 ]]"));
        InlineForExpression expr = parser.listFor();

        List list = expr.evaluateAsList(context);

        Iterable<Expression> exps = expr.iterable(context);
        for(Expression e : exps) {
            Assert.assertNotNull(e.evaluate(context));
        }

        context = new ScriptContext();
        parser = new ScriptParser(new StringReader("[for (i=[0:num-1], a=i*360/num) [ r*cos(a), r*sin(a) ]]"));
        parser.listFor();
    }

}
