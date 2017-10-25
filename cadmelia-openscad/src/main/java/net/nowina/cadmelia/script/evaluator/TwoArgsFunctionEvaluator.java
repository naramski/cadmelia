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
package net.nowina.cadmelia.script.evaluator;

import net.nowina.cadmelia.script.Command;
import net.nowina.cadmelia.script.Evaluator;
import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.ScriptContext;
import net.nowina.cadmelia.script.expression.TwoArgsFunction;

import java.util.function.Function;

public class TwoArgsFunctionEvaluator implements Evaluator {

    private TwoArgsFunction<Double, Double, Double> function;

    public TwoArgsFunctionEvaluator(TwoArgsFunction<Double, Double, Double> function) {
        this.function = function;
    }

    @Override
    public Object evaluate(Command command, ScriptContext context) {
        Expression arg0 = command.getArg(0);
        Double value0 = arg0.evaluateAsDouble(context);
        Expression arg1 = command.getArg(1);
        Double value1 = arg1.evaluateAsDouble(context);
        return function.apply(value0, value1);
    }

}
