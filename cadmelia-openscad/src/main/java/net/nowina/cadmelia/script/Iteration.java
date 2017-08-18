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

public class Iteration extends Command {

    private String variable;

    private Expression start;

    private Expression end;

    private Expression increment;

    public Iteration(String variable, Expression start, Expression increment, Expression end) {
        super("for");
        this.variable = variable;
        this.start = start;
        this.increment = increment;
        this.end = end;
    }

    public Expression getStart() {
        return start;
    }

    public Expression getEnd() {
        return end;
    }

    public Expression getIncrement() {
        return increment;
    }

    public String getVariable() {
        return variable;
    }

}
