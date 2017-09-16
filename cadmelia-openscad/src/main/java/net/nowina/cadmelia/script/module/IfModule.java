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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class IfModule extends UnionModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(IfModule.class);

    public static final String MODULE_NAME = "if";

    public IfModule() {
        super(MODULE_NAME);
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {

        if(!(op instanceof IfCommand)) {
            System.out.println("Gni?");
        }
        IfCommand ifCommand = (IfCommand) op;
        Expression conditionExpr = ifCommand.getCondition();
        boolean condition = conditionExpr.evaluateAsBoolean(context);

        if (condition) {
            return executeInstruction(ifCommand.getThenScope(), context);
        } else {
            if(ifCommand.getElseScope() != null) {
                return executeInstruction(ifCommand.getElseScope(), context);
            }
        }

        return null;
    }

    Construction executeInstruction(Instruction instruction, ScriptContext context) {

        switch (instruction.getType()) {
            case COMMAND:
                Command command = (Command) instruction;
                return operation(command, context);
            case SCOPE:
                Scope scope = (Scope) instruction;
                ScriptContext thisScope = new ScriptContext(context);

                List<Command> operations = new ArrayList<>();
                for(Instruction i : scope.getInstructions()) {
                    switch (i.getType()) {
                        case DEFINE:
                            Define define = (Define) i;
                            thisScope.defineVariableValue(define.getName(), define.getExpression());
                            break;
                        case COMMAND:
                            operations.add((Command) i);
                            break;
                        default:
                            throw new IllegalArgumentException("Unexpected type " + i.getType());
                    }
                }

                return super.union(operations, thisScope);

            default:
                throw new IllegalStateException();

        }

    }

}
