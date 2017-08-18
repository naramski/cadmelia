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

import net.nowina.cadmelia.construction.BuilderFactory;
import net.nowina.cadmelia.construction.Construction;
import net.nowina.cadmelia.script.Command;
import net.nowina.cadmelia.script.ScriptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HullModule extends UnionModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(HullModule.class);

    public static final String MODULE_NAME = "hull";

    private BuilderFactory factory;

    public HullModule(BuilderFactory factory) {
        super(MODULE_NAME);
        this.factory = factory;
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {

        Construction composition = null;
        for (Command internal : op.getOperations()) {
            Construction solid = operation(internal, context);
            if (composition == null) {
                composition = solid;
            } else {
                composition = composition.union(solid);
            }
        }

        return factory.createSolidBuilder().hull(composition);

    }

}
