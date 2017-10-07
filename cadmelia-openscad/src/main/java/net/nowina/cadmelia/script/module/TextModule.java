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

import net.nowina.cadmelia.construction.*;
import net.nowina.cadmelia.script.Command;
import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.ModuleExec;
import net.nowina.cadmelia.script.ScriptContext;

public class TextModule extends ModuleExec {

    private FactoryBuilder factory;

    public TextModule(FactoryBuilder factory) {
        super("text");
        this.factory = factory;
    }

    @Override
    public Construction execute(Command op, ScriptContext context) {

        ShapeFactory builder = factory.createShapeFactory();

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

        String valign = "bottom";
        Expression valignExpr = op.getArg("valign");
        if (valignExpr != null) {
            valign = valignExpr.evaluateAsString(context);
        }

        String halign = "left";
        Expression halignExpr = op.getArg("halign");
        if (halignExpr != null) {
            halign = halignExpr.evaluateAsString(context);
        }

        Construction textShape = builder.text(text, size, "Arial");

        switch (valign) {
            case "bottom":
                // default
                break;
            case "top":
                textShape = textShape.translate(0,  -shapeHeight(textShape), 0);
                break;
            case "center":
                textShape = textShape.translate(0, -shapeHeight(textShape) / 2, 0);
                break;
            default:
                throw new IllegalArgumentException("Invalid value '" + valign + "' for param valign");
        }

        switch (halign) {
            case "left":
                // default
                break;
            case "right":
                textShape = textShape.translate(-shapeWidth(textShape), 0, 0);
                break;
            case "center":
                textShape = textShape.translate(-shapeWidth(textShape) / 2, 0, 0);
                break;
            default:
                throw new IllegalArgumentException("Invalid value '" + halign + "' for param halign");
        }

        return textShape;
    }

    public double shapeHeight(Construction textShape) {
        BoundMeshVisitor visitor = new BoundMeshVisitor();
        textShape.visit(visitor);
        return visitor.height;
    }

    public double shapeWidth(Construction textShape) {
        BoundMeshVisitor visitor = new BoundMeshVisitor();
        textShape.visit(visitor);
        return visitor.width;
    }

    static class BoundMeshVisitor implements MeshVisitor {
        double height = 0.0;
        double width = 0.0;
        @Override
        public void triangle(Vector p1, Vector p2, Vector p3) {
            width = Math.max(width, Math.max(p1.x(), Math.max(p2.x(), p3.x())));
            height = Math.max(height, Math.max(p1.y(), Math.max(p2.y(), p3.y())));
        }
    }

}
