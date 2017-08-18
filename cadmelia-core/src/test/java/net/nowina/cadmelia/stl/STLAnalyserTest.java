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

import net.nowina.cadmelia.TriangleMesh;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.List;

public class STLAnalyserTest {

    @Test
    public void testCube() throws Exception {
        try (FileInputStream in = new FileInputStream("src/test/resources/cube.stl")) {
            STLAnalyser analyser = new STLAnalyser();
            analyser.readSTL(in);
            Assert.assertEquals(8, analyser.points.size());
            Assert.assertEquals(12, analyser.polygons.size());
            List<TriangleMesh> solids = analyser.toPolyhedrons();
            Assert.assertEquals(1, solids.size());
        }
    }
}
