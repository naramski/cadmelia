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

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;

public class TextViewConsole extends OutputStream {

    private TextArea output;

    private StringBuffer buffer = new StringBuffer();

    public TextViewConsole(TextArea console) {
        this.output = console;
    }

    @Override
    public void write(int i) throws IOException {
        buffer.append((char) i);
        if (i == '\n') {
            String line = buffer.toString();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    output.appendText(line);
                }
            });
            buffer.setLength(0);
        }
    }

}
