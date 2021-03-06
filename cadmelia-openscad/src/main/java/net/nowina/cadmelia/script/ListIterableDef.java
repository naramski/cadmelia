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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListIterableDef extends IterableDef {

    private final List<Expression> list;

    public ListIterableDef(List<Expression> list) {
        this.list = list;
    }

    public ListIterableDef() {
        this(new ArrayList<>());
    }

    public ListIterableDef(Expression... list) {
        this(Arrays.asList(list));
    }

    public void add(Expression exp) {
        list.add(exp);
    }

    public Iterable<Expression> iterable(ScriptContext ctx) {
        return list;
    }

    @Override
    protected Object doEvaluation(ScriptContext context) {
        List<Object> list = new ArrayList<>();
        for(Expression e : iterable(context)) {
            list.add(e.evaluate(context));
        }
        return list;
    }

}
