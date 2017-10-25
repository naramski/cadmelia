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

public class CommandTest {

    @Test
    public void test1() {

        Command command1 = new Command("command1");
        command1.addArg(new Argument(ExpressionBuilder.element(1d)));
        command1.addArg(new Argument(ExpressionBuilder.element(2d)));
        command1.addArg(new Argument(ExpressionBuilder.element(3d)));

        Assert.assertEquals(3, command1.getUnamedArgCount());

        command1 = new Command("command1");
        command1.addArg(new Argument(ExpressionBuilder.element(1d)));
        command1.addArg(new Argument("text", ExpressionBuilder.element(2d)));
        command1.addArg(new Argument(ExpressionBuilder.element(3d)));

        Assert.assertEquals(1, command1.getUnamedArgCount());

        command1 = new Command("command1");
        command1.addArg(new Argument(ExpressionBuilder.element(1d)));
        command1.addArg(new Argument(ExpressionBuilder.element(2d)));
        command1.addArg(new Argument("size", ExpressionBuilder.element(3d)));

        Assert.assertEquals(2, command1.getUnamedArgCount());
    }

}
