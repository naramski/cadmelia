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
package net.nowina.cadmelia.script.expression;

import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.Literal;
import net.nowina.cadmelia.script.ScriptContext;

import java.util.ArrayList;
import java.util.List;

public class BinaryExpression extends Expression {

    final Expression xExpr;

    final Expression yExpr;

    final TwoArgsFunction<Double, Double, Double> function;

    public BinaryExpression(Expression xExpr, Expression yExpr, TwoArgsFunction<Double, Double, Double> function) {
        this.xExpr = xExpr;
        this.yExpr = yExpr;
        this.function = function;
    }

    @Override
    protected Object doEvaluation(ScriptContext context) {
        Literal x = xExpr.evaluate(context);
        Literal y = yExpr.evaluate(context);
        if(x.isList()) {
            List<Double> doubleList = x.asList();
            Double factor = y.asDouble();
            return applyFunctionOnList(doubleList, factor);
        } else if(y.isList()) {
            List<Double> doubleList = y.asList();
            Double factor = x.asDouble();
            return applyFunctionOnList(doubleList, factor);
        } else {
            return function.apply(x.asDouble(), y.asDouble());
        }
    }

    private List<Double> applyFunctionOnList(List<Double> doubleList, Double factor) {
        List<Double> list = new ArrayList<>();
        for(Double e : doubleList) {
            list.add(function.apply(factor, e));
        }
        return list;
    }

}
