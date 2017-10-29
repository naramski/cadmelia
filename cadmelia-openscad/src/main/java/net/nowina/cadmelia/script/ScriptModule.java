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

import net.nowina.cadmelia.construction.FactoryBuilder;
import net.nowina.cadmelia.construction.Construction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ScriptModule extends ModuleExec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptModule.class);

    private Module module;

    private boolean preview;

    private FactoryBuilder factory;

    public ScriptModule(Module module, boolean preview, FactoryBuilder factory) {
        super(module.getName());
        this.module = module;
        this.preview = preview;
        this.factory = factory;
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {

        ScriptContext childContext = populateContext(op, context);

        CommandInterpreter builder = new CommandInterpreter(preview, childContext, factory);
        for (Instruction i : module.getInstructions()) {
            switch (i.getType()) {
                case MODULE:
                    Module module = (Module) i;
                    childContext.registerModule(new ScriptModule(module, preview, factory));
                    break;
                case DEFINE:
                    Define define = (Define) i;
                    Object value = define.getExpression().evaluate(childContext).getValue();
                    childContext.defineVariableValue(define.getName(), value);
                    break;
                case COMMAND:
                    // Command are interpreted last
                    break;
                case FUNCTION:
                    Function fun = (Function) i;
                    childContext.registerFunction(fun);
                    break;
                default:
                    LOGGER.warn("Instruction of type " + i.getType() + " is not recognized");
                    break;
            }
        }

        for (Instruction i : module.getInstructions()) {

            if (i.getType() == InstructionType.COMMAND) {
                builder.execute((Command) i);
            }

        }

        return builder.getRoot();

    }

    ScriptContext populateContext(Command op, ScriptContext parentContext) {
        ScriptContext childContext = new ScriptContext(parentContext);
        List<Parameter> parameters = module.getParameters();

        LOGGER.info("Execution of module " + op.getName());
        for (int i = 0; i < parameters.size(); i++) {
            Parameter param = parameters.get(i);
            String name = param.getName();

            Expression valueExpr = param.getDefaultValue();
            Object value = null;
            if(valueExpr != null) {
                value = valueExpr._evaluate(parentContext);
            }
            LOGGER.info("Received param " + name + " with default value " + value);

            /* If the the argument was provided at the time of the call
               the default value is overrided
             */
            boolean unamed = i <= op.getUnamedArgCount();
            Expression argumentValue = unamed ? op.getArg(i) : op.getArg(name);
            if (argumentValue != null) {
                value = argumentValue.evaluate(parentContext).getValue();
                LOGGER.info("Param " + name + " is overrided by param " + value);
            }

            childContext.defineVariableValue(name, value);
        }
        return childContext;
    }

}
