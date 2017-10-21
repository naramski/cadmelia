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
import net.nowina.cadmelia.script.ScriptContext;

public class InlineIfExpression extends Expression {

    private Expression condition;

    private Expression thenExpr;

    private Expression elseExpr;

    public InlineIfExpression(Expression condition, Expression thenExpr, Expression elseExpr) {
        this.condition = condition;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
    }

    @Override
    protected Object doEvaluation(ScriptContext scriptContext) {

        if(condition.evaluateAsBoolean(scriptContext)) {
            return thenExpr.evaluate(scriptContext).getValue();
        } else {
            return elseExpr.evaluate(scriptContext).getValue();
        }
    }

    @Override
    public String toString() {
        return condition + "?" + thenExpr + ":" + elseExpr;
    }
}
