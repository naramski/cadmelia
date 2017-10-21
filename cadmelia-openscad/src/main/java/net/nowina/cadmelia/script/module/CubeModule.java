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
import net.nowina.cadmelia.construction.SolidFactory;
import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.script.*;

public class CubeModule extends ModuleExec {

    public static final String PARAM_SIZE = "size";

    private FactoryBuilder factory;

    public CubeModule(FactoryBuilder factory) {
        super("cube");
        this.factory = factory;
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {

        SolidFactory builder = factory.createSolidFactory();

        Vector sizeVector = new Vector(1, 1, 1);
        Expression unamedSize = op.getFirstUnamedArg();
        if (unamedSize != null) {
            Literal sizeParam = unamedSize.evaluate(context);
            if (sizeParam.isDouble()) {
                double sizeDouble = sizeParam.asDouble();
                sizeVector = new Vector(sizeDouble, sizeDouble, sizeDouble);
            } else {
                sizeVector = sizeParam.asVector();
            }
        } else {
            Expression size = op.getArg(PARAM_SIZE);
            if (size != null) {
                Literal sizeParam = size.evaluate(context);
                if (sizeParam.isDouble()) {
                    double sizeDouble = sizeParam.asDouble();
                    sizeVector = new Vector(sizeDouble, sizeDouble, sizeDouble);
                } else {
                    sizeVector = sizeParam.asVector();
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
