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
import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.ModuleExec;
import net.nowina.cadmelia.script.ScriptContext;

public class CylinderModule extends ModuleExec {

    public static final String MODULE_NAME = "cylinder";

    private BuilderFactory factory;

    public CylinderModule(BuilderFactory factory) {
        super(MODULE_NAME);
        this.factory = factory;
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {

        boolean unamed = op.getUnamedArgCount() == op.getArgCount();

        double height = 1;
        Expression heightParam = unamed ? op.getArg(0) : op.getArg("h");
        if (heightParam != null) {
            height = heightParam.evaluateAsDouble(context);
        }

        Expression radiusParam = unamed ? op.getArg(1) : op.getArg("r");
        double radius = 0.5;
        if (radiusParam != null) {
            radius = radiusParam.evaluateAsDouble(context);
        }

        Expression radius1Param = op.getArg("r1");
        double bottomRadius = radius;
        if (radius1Param != null) {
            bottomRadius = radius1Param.evaluateAsDouble(context);
        }

        double topRadius = radius;
        Expression radius2Param = unamed ? op.getArg(2) : op.getArg("r2");
        if (radius2Param != null) {
            topRadius = radius2Param.evaluateAsDouble(context);
        }

        Expression centerParam = op.getArg("center");
        boolean center = false;
        if (centerParam != null) {
            center = centerParam.evaluateAsBoolean(context);
        }

        Expression fragmentParam = op.getArg("$fa");
        int fragment = 16;
        if (fragmentParam != null) {
            fragment = fragmentParam.evaluateAsInteger(context);
        }

        return factory.createSolidBuilder().cylinder(bottomRadius, topRadius, height, fragment, center);

    }

}
