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

import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.Literal;
import net.nowina.cadmelia.script.ScriptContext;

import java.util.ArrayList;
import java.util.List;

public class ListExpression extends Expression {

    final List<Expression> list;

    public ListExpression(List<Expression> list){
        this.list = list;
    }

    @Override
    protected Object doEvaluation(ScriptContext scriptContext) {
        List<Object> values = new ArrayList<>();
        for(Expression e : list) {
            if(e == null) {
                throw new NullPointerException("A element of the list is null ?!");
            }
            Literal evaluate = e.evaluate(scriptContext);
            if(evaluate == null) {
                throw new NullPointerException("Evaluation of " + evaluate + " is null");
            }
            values.add(evaluate.getValue());
        }
        return values;
    }

    @Override
    public String toString() {
        return list.toString();
    }

}
