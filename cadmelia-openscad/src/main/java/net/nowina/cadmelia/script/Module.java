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

public class Module extends Instruction {

    private String name;

    final List<Parameter> args = new ArrayList<>();

    private List<Instruction> instructions;

    public Module(String name) {
        super(InstructionType.MODULE);
        this.name = name;
        this.instructions = new ArrayList<>();
    }

    public void addInstruction(Instruction i) {
        this.instructions.add(i);
    }

    public void addParam(Parameter arg) {
        args.add(arg);
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public String getName() {
        return name;
    }

    public List<Parameter> getArgs() {
        return args;
    }

}
