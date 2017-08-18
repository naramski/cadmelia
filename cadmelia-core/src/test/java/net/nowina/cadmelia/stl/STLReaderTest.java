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
package net.nowina.cadmelia.stl;

import org.junit.Test;

import java.io.File;

public class STLReaderTest {

    @Test
    public void testCube() throws Exception {
        STLReader.parse(new File("src/test/resources/cube.stl"), new STLEventHandler() {
        });
    }

    @Test
    public void testSphere() throws Exception {
        STLReader.parse(new File("src/test/resources/sphere.stl"), new STLEventHandler() {
        });
    }

    @Test
    public void test2Objects() throws Exception {
        STLReader.parse(new File("src/test/resources/2objects.stl"), new STLEventHandler() {
        });
    }

}
