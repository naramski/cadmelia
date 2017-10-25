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
import net.nowina.cadmelia.script.*;
import net.nowina.cadmelia.script.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ForModule extends UnionModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForModule.class);

    public static final String MODULE_NAME = "for";

    public ForModule() {
        super(MODULE_NAME);
    }

    @Override
    public Construction execute(Command _op, ScriptContext context) {

        ForCommand op = (ForCommand) _op;

        Construction iteration = null;

        Literal iterable = op.getIterableDef().evaluate(context);
        for (Object val : iterable.asList()) {

            ScriptContext childContext = new ScriptContext(context);
            childContext.defineVariableValue(op.getVariable(), val);
            LOGGER.info("ForCommand " + op.getVariable() + "=" + val);

            Instruction instruction = op.getInstruction();

            switch (instruction.getType()) {
                case SCOPE:
                    iteration = executeScope(iteration, childContext, (Scope) instruction);
                    break;
                case COMMAND:
                    iteration = executeCommand(iteration, childContext, (Command) instruction);
                    break;
            }

            LOGGER.info("Result of this iteration " + iteration);

        }
        return iteration;
    }

    private Construction executeScope(Construction iteration, ScriptContext childContext, Scope instruction) {
        Scope scope = instruction;
        List<Command> operations = new ArrayList<>();
        for(Instruction instr : scope.getInstructions()) {
            switch (instr.getType()) {
                case DEFINE:
                    Define define = (Define) instr;
                    childContext.defineVariableValue(define.getName(), define.getExpression());
                    break;
                case COMMAND:
                    operations.add((Command) instr);
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected type " + instr.getType());
            }
        }

        for (Command internal : operations) {

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
        return iteration;
    }

    private Construction executeCommand(Construction iteration, ScriptContext childContext, Command internal) {

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

        return iteration;
    }

}
