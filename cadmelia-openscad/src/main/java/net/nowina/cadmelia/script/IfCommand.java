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

public class IfCommand extends Command {

    private Expression condition;

    private Instruction thenScope;

    private Instruction elseScope;

    public IfCommand() {
        super("if");
    }

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public Instruction getThenScope() {
        return thenScope;
    }

    public void setThenScope(Instruction thenScope) {
        this.thenScope = thenScope;
    }

    public Instruction getElseScope() {
        return elseScope;
    }

    public void setElseScope(Instruction elseScope) {
        this.elseScope = elseScope;
    }

}
