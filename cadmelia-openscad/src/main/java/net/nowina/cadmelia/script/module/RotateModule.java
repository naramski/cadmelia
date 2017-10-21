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
import net.nowina.cadmelia.math.Transformation;
import net.nowina.cadmelia.script.Command;
import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.Literal;
import net.nowina.cadmelia.script.ScriptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RotateModule extends UnionModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(RotateModule.class);

    public static final String MODULE_NAME = "rotate";
    public static final String ANGLE_PARAM = "a";
    public static final String VECTOR_PARAM = "v";

    public RotateModule() {
        super(MODULE_NAME);
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {

        Construction composition = super.execute(op, context);
        if(composition == null) {
            LOGGER.warn("Cannot execute operation " + op + " on null");
            return null;
        }

        Expression rotationExpr = op.getArg(ANGLE_PARAM);
        if (rotationExpr == null) {
            rotationExpr = op.getFirstUnamedArg();
        }

        Vector openScadRotation = null;

        Literal rotation = rotationExpr.evaluate(context);
        if(rotation.isVector()) {

            Vector rot = rotation.asVector();
            openScadRotation = new Vector(-rot.x(), -rot.y(), -rot.z());
            LOGGER.info("Rotate composition of " + openScadRotation);
            composition = composition.rotate(openScadRotation);

        } else {

            double angle = rotation.asDouble();

            Expression axisExpr = op.getArg(VECTOR_PARAM);
            if (axisExpr == null) {
                axisExpr = op.getArg(1);
            }

            Vector axis = null;
            if(axisExpr != null) {
                axis = axisExpr.evaluateAsVector(context);
                axis = axis.normalized();
            } else {
                axis = Vector.Z;
            }

            Transformation tx = Transformation.rotation(angle, axis);
            LOGGER.info("Rotate composition of " + tx);
            composition = composition.transform(tx);

        }

        return composition;
    }

}
