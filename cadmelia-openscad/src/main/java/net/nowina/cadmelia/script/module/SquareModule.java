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
import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.script.*;

public class SquareModule extends ModuleExec {

    public static final String MODULE_NAME = "square";

    private FactoryBuilder factory;

    public SquareModule(FactoryBuilder factory) {
        super(MODULE_NAME);
        this.factory = factory;
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {

        Vector sizeV = new Vector(1, 1, 0);

        Expression sizeExpr = op.getArg("size");
        if (sizeExpr == null && op.getArgCount() > 1) {
            sizeExpr = op.getArg(0);
        }

        if (sizeExpr != null) {
            Literal obj = sizeExpr.evaluate(context);
            if(obj.isVector()) {
                sizeV = obj.asVector();
            } else {
                double size = obj.asDouble();
                sizeV = new Vector(size, size, 0);
            }
        }

        boolean centered = false;

        Expression center = op.getArg("center");

        if (center != null) {
            centered = center.evaluateAsBoolean(context);
        }

        return factory.createShapeFactory().square(sizeV.x(), sizeV.y(), centered);
    }

}
