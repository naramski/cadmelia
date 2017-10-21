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
package net.nowina.cadmelia.script.expression;

import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.ScriptContext;

public class ExpressionVector extends Expression {

    final Expression x;

    final Expression y;

    final Expression z;

    public ExpressionVector(Expression x, Expression y, Expression z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    protected Vector doEvaluation(ScriptContext context) {
        Double valX = x.evaluateAsDouble(context);
        Double valY = y.evaluateAsDouble(context);
        if (z != null) {
            Double valZ = z.evaluateAsDouble(context);
            return new Vector(valX, valY, valZ);
        } else {
            return new Vector(valX, valY);
        }
    }

    @Override
    public String toString() {
        return "[" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ']';
    }

}
