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

import net.nowina.cadmelia.construction.Vector;

import java.util.HashMap;
import java.util.Map;

public class ScriptContext {

    private Map<String, Object> variables = new HashMap<>();

    private Map<String, ModuleExec> modules = new HashMap<>();

    private Map<String, Function> functions = new HashMap<>();

    private final ScriptContext parent;

    public ScriptContext() {
        this.parent = null;
    }

    public ScriptContext(ScriptContext parent) {
        this.parent = parent;
    }

    public Object getVariableValue(String variable) {
        if("$children".equals(variable)) {
            return Expression.element(0);
        }
        Object local = variables.get(variable);
        if (local == null && parent != null) {
            return parent.getVariableValue(variable);
        }
        return local;
    }

    public Double getVariableAsDouble(String variable) {
        return (Double) getVariableValue(variable);
    }

    public Vector getVariableAsVector(String variable) {
        return (Vector) getVariableValue(variable);
    }

    public void defineVariableValue(String variable, Object valueOrExpr) {
        if(variables.containsKey(variable)) {
            throw new IllegalStateException("Variable '" + variable + "' already defined");
        }
        variables.put(variable, valueOrExpr);
    }

    public void registerModule(ModuleExec module) {

        /* It is possible to override the existing modules */
        modules.put(module.getName(), module);

    }

    public void registerFunction(Function fun) {

        /* It is possible to override the existing modules */
        functions.put(fun.getName(), fun);

    }

    public ModuleExec getModule(String moduleName) {
        ModuleExec exec = modules.get(moduleName);
        if (exec == null && parent != null) {
            return parent.getModule(moduleName);
        }
        return exec;
    }

    public Function getFunction(String functionName) {
        Function exec = functions.get(functionName);
        if (exec == null && parent != null) {
            return parent.getFunction(functionName);
        }
        return exec;
    }

}
