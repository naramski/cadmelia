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
package net.nowina.cadmelia.script.module;

import net.nowina.cadmelia.script.*;
import org.junit.Test;

public class ForModuleTest {

    @Test
    public void test() throws Exception {

        ScriptContext context = new ScriptContext();

        ForCommand command = new ForCommand();
        command.addIteration(new Iteration("x", new RangeIterableDef(ExpressionBuilder.element(1), ExpressionBuilder.element(3))));
        command.addIteration(new Iteration("y", new RangeIterableDef(ExpressionBuilder.element(1), ExpressionBuilder.element(3))));
        command.setInstruction(new Define("val", ExpressionBuilder.element("x")));

        ForModule module = new ForModule();
        module.execute(command, context);

    }

}
