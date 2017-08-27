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
package net.nowina.cadmelia.solid.bspcsg;

import net.nowina.cadmelia.stl.STLWriter;
import org.junit.Test;

import java.io.IOException;

public class SpheresUnionTest {

    @Test
    public void test() throws IOException {

        CSGSolidBuilder builder = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(true).build();

        CSGSolid spheres = null;

        double radius = 1.2;
        double spacing = 0.25;

        for (int x = 0; x < 3; x++) {

            CSGSolid sphere = (CSGSolid) new Sphere(builder, radius, 16, 8).buildSolid()
                    .translate((x - 5) * (radius * 2 + spacing), 0, 0.0);

            if (spheres == null) {
                spheres = sphere;
            } else {
                spheres = (CSGSolid) spheres.union(sphere);
            }

        }


        STLWriter.writeToFile("build/union-sphere.stl", spheres);

    }
}
