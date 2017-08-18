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
import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.script.Command;
import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.ModuleExec;
import net.nowina.cadmelia.script.ScriptContext;

public class SquareModule extends ModuleExec {

    public static final String MODULE_NAME = "square";

    private BuilderFactory factory;

    public SquareModule(BuilderFactory factory) {
        super(MODULE_NAME);
        this.factory = factory;
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {

        Vector sizeV = new Vector(1, 1, 0);

        Expression size = op.getArg("size");
        if (size == null && op.getArgCount() > 1) {
            size = op.getArg(0);
        }

        if (size != null) {
            sizeV = size.evaluateAsVector(context);
        }

        boolean centered = false;

        Expression center = op.getArg("center");

        if (center != null) {
            centered = center.evaluateAsBoolean(context);
        }

        return factory.createShapeBuilder().square(sizeV.x(), sizeV.y(), centered);
    }

}
