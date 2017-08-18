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

import net.nowina.cadmelia.construction.Construction;
import net.nowina.cadmelia.script.Command;
import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.Iteration;
import net.nowina.cadmelia.script.ScriptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForModule extends UnionModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForModule.class);

    public static final String MODULE_NAME = "for";

    public ForModule() {
        super(MODULE_NAME);
    }

    @Override
    public Construction execute(Command _op, ScriptContext context) {

        Iteration op = (Iteration) _op;

        double start = (Double) op.getStart().evaluate(context);
        Expression endExpr = op.getEnd();
        double end = (Double) endExpr.evaluate(context);
        Expression incExpression = op.getIncrement();
        double increment = 1;
        if (incExpression != null) {
            increment = (Double) incExpression.evaluate(context);
        }

        Construction iteration = null;

        for (double i = start; i <= end; i += increment) {
            ScriptContext childContext = new ScriptContext(context);
            childContext.defineVariableValue(op.getVariable(), i);
            LOGGER.info("Iteration " + op.getVariable() + "=" + i);

            for (Command internal : op.getOperations()) {

                Construction element = operation(internal, childContext);
                if (element == null) {
                    LOGGER.warn("Null returned by " + internal);
                } else {
                    if (iteration == null) {
                        iteration = element;
                    } else {
                        iteration = iteration.union(element);
                    }
                }

            }

            LOGGER.info("Result of this iteration " + iteration);

        }
        return iteration;
    }

}
