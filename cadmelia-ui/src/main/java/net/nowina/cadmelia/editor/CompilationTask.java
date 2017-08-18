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
package net.nowina.cadmelia.editor;

import javafx.concurrent.Task;
import net.nowina.cadmelia.construction.BuilderFactory;
import net.nowina.cadmelia.construction.Construction;
import net.nowina.cadmelia.construction.Solid;
import net.nowina.cadmelia.script.Script;
import net.nowina.cadmelia.script.ScriptScene;
import net.nowina.cadmelia.script.parser.ScriptParser;

import java.io.StringReader;

public class CompilationTask extends Task<Solid> {

    private String code;

    private boolean preview;

    public CompilationTask(String code, boolean preview) {
        this.code = code;
        this.preview = preview;
    }

    @Override
    protected Solid call() throws Exception {

        BuilderFactory factory = BuilderFactory.getInstance();
        ScriptScene scene = new ScriptScene(preview, factory);

        ScriptParser parser = new ScriptParser(new StringReader(code));
        Script script = parser.Script();
        scene.executeScript(script);

        Construction construction = scene.getRoot();
        if (construction.isSolid()) {
            return (Solid) construction;
        } else {
            throw new IllegalStateException("Result must be a solid");
        }
    }

}
