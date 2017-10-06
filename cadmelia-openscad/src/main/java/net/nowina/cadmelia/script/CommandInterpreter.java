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
import net.nowina.cadmelia.script.module.UnionModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandInterpreter extends UnionModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandInterpreter.class);

    private final boolean preview;

    private final ScriptContext context;

    private Construction root;

    private FactoryBuilder factory;

    public CommandInterpreter(boolean preview, ScriptContext context, FactoryBuilder factory) {
        this.factory = factory;
        this.preview = preview;
        this.context = context;
    }

    public void execute(Command command) {
        LOGGER.info("New command " + command);
        Construction element = operation(command, context);
        if (root == null) {
            root = element;
        } else {
            if (element != null) {
                root = root.union(element);
            }
        }
    }

    public Construction getRoot() {
        return root;
    }

    public ScriptContext getContext() {
        return context;
    }

    public boolean isPreview() {
        return preview;
    }

}
