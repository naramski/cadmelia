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

public class IntersectionModule extends CompositionModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntersectionModule.class);

    public static final String MODULE_NAME = "intersection";

    public IntersectionModule() {
        super(MODULE_NAME);
    }

    protected IntersectionModule(String name) {
        super(name);
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {
        return intersection(op, context);
    }

    private Construction intersection(Command transformation, ScriptContext context) {
        Construction composition = null;
        for (Command op : transformation.getOperations()) {
            Construction element = operation(op, context);
            if (composition == null) {
                composition = element;
            } else {
                composition = composition.intersection(element);
            }
        }
        return composition;
    }

}
