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
import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.script.Command;
import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.ScriptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScaleModule extends UnionModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScaleModule.class);

    public static final String MODULE_NAME = "scale";
    public static final String VECTOR_PARAM = "v";

    public ScaleModule() {
        super(MODULE_NAME);
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {

        Construction composition = super.execute(op, context);

        Expression scaleExpr = op.getArg(VECTOR_PARAM);
        if (scaleExpr == null) {
            scaleExpr = op.getFirstUnamedArg();
        }

        if (scaleExpr == null) {
            throw new NullPointerException("a scale vector must be provided");
        }

        Object value = scaleExpr.evaluate(context);
        Vector scale = null;
        if(value instanceof Vector) {
            scale = (Vector) value;
        } else {
            double d = (double) value;
            scale = new Vector(d, d, d);
        }
        LOGGER.info("Scale composition of " + scale);
        composition = composition.scale(scale);
        return composition;
    }

}
