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
package net.nowina.cadmelia.script;

import net.nowina.cadmelia.script.expression.*;

import java.util.Arrays;
import java.util.List;

public class ExpressionBuilder {

    public static Expression element(double value) {
        return new ExpressionElement<>(value);
    }

    public static Expression elementText(String text) {
        return new ExpressionElement<>(text);
    }

    public static Expression element(String variableName) {
        return new VariableExpression(variableName);
    }

    public static Expression element(Expression x, Expression y, Expression z) {
        if(z == null) {
            return new ListExpression(new ListIterableDef(x, y));
        } else {
            return new ListExpression(new ListIterableDef(x, y, z));
        }
    }

    public static Expression elementFunction(Command command) {
        return new FunctionExpression(command);
    }

    public static Expression inlineIf(Expression condition, Expression thenExpr, Expression elseExpr) {
        return new InlineIfExpression(condition, thenExpr, elseExpr);
    }

    public static Expression element(Boolean value) {
        return new ExpressionElement(value);
    }

    public static Expression element(IterableDef list) {
        return new ListExpression(list);
    }

    public static Expression vectorElement(Expression list, Expression index) {
        return new VectorElementExpression(list, index);
    }

}
