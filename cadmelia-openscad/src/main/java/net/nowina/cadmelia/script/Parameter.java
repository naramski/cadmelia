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

public class Parameter {

    private final String name;

    private final Expression defaultValue;

    public Parameter(String name, Expression defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public Parameter(String name, Double defaultValue) {
        this(name, ExpressionBuilder.element(defaultValue));
    }

    public Parameter(String name) {
        this(name, (Expression) null);
    }

    public String getName() {
        return name;
    }

    public Expression getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        if (defaultValue == null) {
            return name;
        } else {
            return name + "=" + defaultValue;
        }
    }

}
