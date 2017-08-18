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
import net.nowina.cadmelia.construction.SolidBuilder;
import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.script.Command;
import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.ModuleExec;
import net.nowina.cadmelia.script.ScriptContext;

public class CubeModule extends ModuleExec {

    public static final String PARAM_SIZE = "size";

    private BuilderFactory factory;

    public CubeModule(BuilderFactory factory) {
        super("cube");
        this.factory = factory;
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {

        SolidBuilder builder = factory.createSolidBuilder();

        Vector sizeVector = new Vector(1, 1, 1);
        Expression unamedSize = op.getFirstUnamedArg();
        if (unamedSize != null) {
            Object sizeParam = unamedSize.evaluate(context);
            if (sizeParam instanceof Double) {
                double sizeDouble = (double) sizeParam;
                sizeVector = new Vector(sizeDouble, sizeDouble, sizeDouble);
            } else {
                sizeVector = (Vector) sizeParam;
            }
        } else {
            Expression size = op.getArg(PARAM_SIZE);
            if (size != null) {
                Object sizeParam = size.evaluate(context);
                if (sizeParam instanceof Double) {
                    double sizeDouble = (double) sizeParam;
                    sizeVector = new Vector(sizeDouble, sizeDouble, sizeDouble);
                } else {
                    sizeVector = (Vector) sizeParam;
                }
            }
        }

        boolean centered = false;
        Expression center = op.getArg("center");
        if (center == null) {
            center = op.getArg(1);
        }
        if (center != null) {
            centered = center.evaluateAsBoolean(context);
        }

        return builder.cube(sizeVector.x(), sizeVector.y(), sizeVector.z(), centered);
    }

}
