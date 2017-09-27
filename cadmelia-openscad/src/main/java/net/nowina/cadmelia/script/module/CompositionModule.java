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
import net.nowina.cadmelia.script.ModuleExec;
import net.nowina.cadmelia.script.ScriptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CompositionModule extends ModuleExec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompositionModule.class);

    protected CompositionModule(String name) {
        super(name);
    }

    /**
     * Execute one operation using the provided context
     * @param op The operation to execute
     * @param context The context in which the execution occurs
     * @return The resulting Construction
     * @throws RuntimeException if the operation is not recognized in this context
     */
    protected Construction operation(Command op, ScriptContext context) {
        LOGGER.info("Execute operation " + op);
        Construction result = null;
        ModuleExec module = context.getModule(op.getName());
        if (module != null) {
            result = module.execute(op, context);
        } else {
            throw new RuntimeException("The operation '" + op.getName() + "' is not recognized");
        }

        LOGGER.info("Result " + result);
        return result;
    }

}
