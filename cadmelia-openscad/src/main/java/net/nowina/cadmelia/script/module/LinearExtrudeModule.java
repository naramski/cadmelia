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

import net.nowina.cadmelia.construction.FactoryBuilder;
import net.nowina.cadmelia.construction.Construction;
import net.nowina.cadmelia.script.Command;
import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.ScriptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinearExtrudeModule extends UnionModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinearExtrudeModule.class);

    public static final String MODULE_NAME = "linear_extrude";
    public static final String HEIGHT_PARAM = "height";
    public static final String SCALE_PARAM = "scale";

    private FactoryBuilder factory;

    public LinearExtrudeModule(FactoryBuilder factory) {
        super(MODULE_NAME);
        this.factory = factory;
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {

        Construction composition = super.execute(op, context);

        if (!composition.isShape()) {
            LOGGER.warn("Extruded construction must be a Shape");
            return composition;
        } else {

            Expression expH = op.getArg(HEIGHT_PARAM);
            if (expH == null) {
                expH = op.getFirstUnamedArg();
            }
            if(expH == null) {
                throw new IllegalArgumentException("Parameter '" + HEIGHT_PARAM + "' is not defined");
            }
            double height = expH.evaluateAsDouble(context);

            double scale = 1.0;
            Expression scaleExpr = op.getArg(SCALE_PARAM);
            if(scaleExpr != null) {
                scale = scaleExpr.evaluateAsDouble(context);
            }

            return factory.createSolidFactory().extrude(composition, height, scale);
        }
    }

}
