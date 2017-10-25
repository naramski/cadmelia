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
import java.util.List;

public class Command extends Instruction {

    final String name;

    final List<Argument> args = new ArrayList<>();

    final List<Command> operations = new ArrayList<>();

    public Command(String name) {
        super(InstructionType.COMMAND);
        this.name = name;
    }

    public void addArg(Argument arg) {
        args.add(arg);
    }

    public void addArg(Expression exp) {
        args.add(new Argument(null, exp));
    }

    public Expression getArg(String name) {
        for (Argument arg : args) {
            if (name.equals(arg.getName())) {
                return arg.getExpression();
            }
        }
        return null;
    }

    public Expression getArg(int index) {
        if (index >= args.size()) {
            return null;
        }
        return args.get(index).getExpression();
    }

    public Expression getFirstUnamedArg() {
        for (Argument arg : args) {
            if (arg.getName() == null) {
                return arg.getExpression();
            }
        }
        return null;
    }

    public int getArgCount() {
        return args.size();
    }

    /**
     * Returns the amount of unamed argument before the first named argument
     * @return
     */
    public int getUnamedArgCount() {
        int count = 0;
        for (Argument arg : args) {
            if (arg.getName() == null) {
                count++;
            } else {
                return count;
            }
        }
        return count;
    }

    public String getName() {
        return name;
    }

    public void addOperation(Command operation) {
        operations.add(operation);
    }

    public List<Command> getOperations() {
        return operations;
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", parameters=" + args +
                '}';
    }

}
