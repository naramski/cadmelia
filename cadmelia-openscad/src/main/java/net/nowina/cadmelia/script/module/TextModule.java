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
import net.nowina.cadmelia.construction.ShapeBuilder;
import net.nowina.cadmelia.script.Command;
import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.ModuleExec;
import net.nowina.cadmelia.script.ScriptContext;

public class TextModule extends ModuleExec {

    private BuilderFactory factory;

    public TextModule(BuilderFactory factory) {
        super("text");
        this.factory = factory;
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {

        ShapeBuilder builder = factory.createShapeBuilder();

        String text = null;
        Expression textExpr = op.getFirstUnamedArg();
        if (textExpr != null) {
            text = (String) textExpr.evaluate(context);
        } else {
            textExpr = op.getArg("text");
            if (textExpr != null) {
                text = (String) textExpr.evaluate(context);
            }
        }

        int size = 10;
        Expression sizeExpr = op.getArg("size");
        if (sizeExpr != null) {
            size = sizeExpr.evaluateAsInteger(context);
        }

        return builder.text(text, size, "Arial");
    }

}
