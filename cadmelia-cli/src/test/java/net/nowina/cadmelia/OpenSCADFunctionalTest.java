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
package net.nowina.cadmelia;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

public class OpenSCADFunctionalTest {

    private CompileToSTLApp app;

    @Before
    public void setup() {
        app = new CompileToSTLApp();
    }

    @Test(expected = IllegalStateException.class)
    public void testTwoBinding() throws Exception {

        app.render(new StringReader("a = 1; a = 2;"));
    }

    @Test(expected = IllegalStateException.class)
    public void testTwoBindingSameScope() throws Exception {

        app.render(new StringReader("a = 1; if(a==1) a = 2;"));
    }

    @Test
    public void testTwoBindingInnerScope() throws Exception {

        app.render(new StringReader("b = 1; if(b==1) { b = 2; }"));
    }

}
