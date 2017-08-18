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

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import net.nowina.cadmelia.construction.Solid;

public class CompilationService extends Service<Solid> {

    private String code;

    private boolean preview = false;

    public void setCode(String code) {
        this.code = code;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    @Override
    protected Task<Solid> createTask() {
        return new CompilationTask(code, preview);
    }

}
