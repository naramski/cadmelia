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

import net.nowina.cadmelia.script.*;
import net.nowina.cadmelia.script.evaluator.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionExpression extends Expression {

    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionExpression.class);

    private Command command;

    public FunctionExpression(Command command) {
        this.command = command;
    }

    @Override
    protected Object doEvaluation(ScriptContext scriptContext) {
        return evaluateFunction(command, scriptContext);
    }

    public static Object evaluateFunction(Command command, ScriptContext context) {
        switch (command.getName()) {
            case "max":
                return new MaxEvaluator().evaluate(command, context);
            case "sin":
                return new SimpleFunctionEvaluator(a -> Math.sin(Math.toRadians(a))).evaluate(command, context);
            case "cos":
                return new SimpleFunctionEvaluator(a -> Math.cos(Math.toRadians(a))).evaluate(command, context);
            case "round":
                return new SimpleFunctionEvaluator(value -> (double) Math.round(value)).evaluate(command, context);
            case "tan":
                return new SimpleFunctionEvaluator(a -> Math.tan(Math.toRadians(a))).evaluate(command, context);
            case "atan":
                return new SimpleFunctionEvaluator(a -> Math.toDegrees(Math.atan(a))).evaluate(command, context);
            case "rand":
                return new RandEvaluator().evaluate(command, context);
            case "rands":
                return new RandsEvaluator().evaluate(command, context);
            case "version":
                return new VersionEvaluator().evaluate(command, context);
            case "lookup":
                return new LookupEvaluator().evaluate(command, context);
            case "str":
                return new StrEvaluator().evaluate(command, context);
            default:
                return evaluateFunctionDef(command, context);
        }
    }

    private static Object evaluateFunctionDef(Command command, ScriptContext context) {
        Function fun = context.getFunction(command.getName());
        if(fun == null) {
            throw new UnsupportedOperationException("Function not recognized " + command.getName());
        } else {
            ScriptContext childContext = new ScriptContext(context);

            boolean unamed = command.getUnamedArgCount() == fun.getArgs().size();

            LOGGER.info("Execution of evaluator " + command.getName());
            for (int i = 0; i < fun.getArgs().size(); i++) {
                Parameter param = fun.getArgs().get(i);
                String name = param.getName();

                Object value = param.getDefaultValue();
                LOGGER.info("Received param " + name + " with default value " + value);

                /* If the the argument was provided at the time of the call
                   the default value is overrided
                 */
                Expression argumentValue = unamed ? command.getArg(i) : command.getArg(name);
                if (argumentValue != null) {
                    value = argumentValue.evaluate(context).getValue();
                    LOGGER.info("Param " + name + " is overrided by param " + value);
                }

                childContext.defineVariableValue(name, value);
            }

            Expression exp = fun.getExpression();
            return exp.evaluate(childContext).getValue();
        }
    }

    @Override
    public String toString() {
        return command.toString();
    }

}
