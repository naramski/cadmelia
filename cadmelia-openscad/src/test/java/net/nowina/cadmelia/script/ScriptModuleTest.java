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

import org.junit.Assert;
import org.junit.Test;

public class ScriptModuleTest {

    @Test
    public void test1() {

        Command command1 = new Command("command1");
        command1.addArg(new Argument(ExpressionBuilder.element(1d)));
        command1.addArg(new Argument(ExpressionBuilder.element(2d)));
        command1.addArg(new Argument(ExpressionBuilder.element(3d)));

        Module module = new Module("command1");
        module.addParam(new Parameter("arg1"));
        module.addParam(new Parameter("arg2"));
        module.addParam(new Parameter("arg3"));

        ScriptModule scriptModule = new ScriptModule(module, false, null);
        ScriptContext ctx = scriptModule.populateContext(command1, new ScriptContext());

        Assert.assertEquals(1d, (double) ctx.getVariableValue("arg1"), 1e-8);
        Assert.assertEquals(2d, (double) ctx.getVariableValue("arg2"), 1e-8);
        Assert.assertEquals(3d, (double) ctx.getVariableValue("arg3"), 1e-8);

    }

    @Test
    public void test2() {

        Command command1 = new Command("command1");

        Module module = new Module("command1");
        module.addParam(new Parameter("arg1", 4d));
        module.addParam(new Parameter("arg2", 5d));
        module.addParam(new Parameter("arg3", 6d));

        ScriptModule scriptModule = new ScriptModule(module, false, null);
        ScriptContext ctx = scriptModule.populateContext(command1, new ScriptContext());

        Assert.assertEquals(4d, (double) ctx.getVariableValue("arg1"), 1e-8);
        Assert.assertEquals(5d, (double) ctx.getVariableValue("arg2"), 1e-8);
        Assert.assertEquals(6d, (double) ctx.getVariableValue("arg3"), 1e-8);

    }

    @Test
    public void test3() {

        Command command1 = new Command("command1");
        command1.addArg(new Argument(ExpressionBuilder.element(1d)));
        command1.addArg(new Argument(ExpressionBuilder.element(2d)));

        Module module = new Module("command1");
        module.addParam(new Parameter("arg1"));
        module.addParam(new Parameter("arg2", 5d));
        module.addParam(new Parameter("arg3", 6d));

        ScriptModule scriptModule = new ScriptModule(module, false, null);
        ScriptContext ctx = scriptModule.populateContext(command1, new ScriptContext());

        Assert.assertEquals(1d, (double) ctx.getVariableValue("arg1"), 1e-8);
        Assert.assertEquals(2d, (double) ctx.getVariableValue("arg2"), 1e-8);
        Assert.assertEquals(6d, (double) ctx.getVariableValue("arg3"), 1e-8);

    }

}
