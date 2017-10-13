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

public class RangeIterableDef implements IterableDef {

    private static final Expression DEFAULT_INCREMENT = Expression.element(1.0d);

    private final Expression start;

    private final Expression end;

    private final Expression increment;

    public RangeIterableDef(Expression start, Expression end, Expression increment) {
        this.start = start;
        this.end = end;
        this.increment = increment != null ? increment : DEFAULT_INCREMENT;
    }

    public RangeIterableDef(Expression start, Expression end) {
        this(start, end, DEFAULT_INCREMENT);
    }

    public Iterable<Expression> evaluate(ScriptContext ctx) {
        double start = this.start.evaluateAsDouble(ctx);
        double end = this.end.evaluateAsDouble(ctx);
        double increment = this.increment.evaluateAsDouble(ctx);
        return new Range(start, end, increment);
    }

}
