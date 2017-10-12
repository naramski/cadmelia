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

import net.nowina.cadmelia.construction.FactoryBuilder;
import net.nowina.cadmelia.script.module.*;

public class ScriptScene extends CommandInterpreter {

    private FactoryBuilder factory;

    public ScriptScene(FactoryBuilder factory) {
        this(false, factory);
    }

    public ScriptScene(boolean preview, FactoryBuilder factory) {
        super(preview, new ScriptContext(), factory);

        this.factory = factory;

        getContext().registerModule(new CircleModule(factory));
        getContext().registerModule(new CubeModule(factory));
        getContext().registerModule(new CylinderModule(factory));
        getContext().registerModule(new ColorModule());
        getContext().registerModule(new DifferenceModule());
        getContext().registerModule(new EchoModule());
        getContext().registerModule(new ForModule());
        getContext().registerModule(new LinearExtrudeModule(factory));
        getContext().registerModule(new HullModule(factory));
        getContext().registerModule(new IfModule());
        getContext().registerModule(new IntersectionModule());
        getContext().registerModule(new OffsetModule(factory));
        getContext().registerModule(new PolygonModule(factory));
        getContext().registerModule(new RotateModule());
        getContext().registerModule(new ScaleModule());
        getContext().registerModule(new SphereModule(factory));
        getContext().registerModule(new SquareModule(factory));
        getContext().registerModule(new TextModule(factory));
        getContext().registerModule(new TranslateModule());
        getContext().registerModule(new UnionModule());
    }

    public void executeScript(Script script) {

        for (Module module : script.getModules()) {
            getContext().registerModule(new ScriptModule(module, isPreview(), factory));
        }

        for (Instruction instruction : script.getInstructions()) {
            if (instruction.getType() == InstructionType.FUNCTION) {
                Function fun = (Function) instruction;
                getContext().registerFunction(fun);
            }
        }

        for (Instruction instruction : script.getInstructions()) {
            if (instruction.getType() == InstructionType.DEFINE) {
                Define define = (Define) instruction;
                getContext().defineVariableValue(define.getName(), define.getExpression().evaluate(getContext()));
            }
        }

        for (Instruction instruction : script.getInstructions()) {

            if (instruction.getType() == InstructionType.COMMAND) {
                Command command = (Command) instruction;
                execute(command);
            }

        }

    }

}
