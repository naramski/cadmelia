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
import net.nowina.cadmelia.script.ScriptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UnionModule extends CompositionModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnionModule.class);

    public static final String MODULE_NAME = "union";

    public UnionModule() {
        super(MODULE_NAME);
    }

    protected UnionModule(String name) {
        super(name);
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {
        LOGGER.info("Execute operation " + op);
        List<Command> operations = op.getOperations();
        if(operations != null && !operations.isEmpty()) {
            LOGGER.debug("Contains " + operations.size() + " operations");
        } else {
            LOGGER.warn("The Union don't contains operations");
        }
        return union(operations, context);
    }

    Construction union(List<Command> operations, ScriptContext context) {
        Construction composition = null;
        for (Command op : operations) {
            LOGGER.info("Union of operation " + op);
            Construction element = operation(op, context);
            if (element != null) {
                composition = union(composition, element);
            }
        }
        return composition;
    }

    private Construction union(Construction element1, Construction element2) {
        if (element1 == null) {
            return element2;
        } else {
            return element1.union(element2);
        }
    }

}
