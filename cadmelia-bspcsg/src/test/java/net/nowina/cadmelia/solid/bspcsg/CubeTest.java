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

import net.nowina.cadmelia.construction.Construction;
import net.nowina.cadmelia.construction.Solid;
import net.nowina.cadmelia.stl.STLWriter;
import org.junit.Test;

public class CubeTest {

    @Test
    public void test1() throws Exception {

        CSGSolidBuilder factory = new FactoryBuilder().build();
        Cube cube = new Cube(factory, 1, 1, 1, true);

        STLWriter.writeToFile("build/test-cube.stl", cube.buildSolid());

    }

    @Test
    public void testDifference1() throws Exception {

        CSGSolidBuilder factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(false).build();

        Construction cube1 = factory.cube(2, 2, 2, false);
        Construction cube2 = factory.cube(2, 2, 2, true);

        Solid difference = (Solid) cube2.difference(cube1);

        STLWriter.writeToFile("build/difference-cube-1.stl", difference);

    }

    @Test
    public void testDifference2() throws Exception {

        CSGSolidBuilder factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(true).build();

        Construction cube1 = factory.cube(2, 2, 2, false);
        Construction cube2 = factory.cube(2, 2, 2, true);

        Solid difference = (Solid) cube2.difference(cube1);

        STLWriter.writeToFile("build/difference-cube-2.stl", difference);

    }

    @Test
    public void testDifference3() throws Exception {

        CSGSolidBuilder factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(true).build();

        Construction cube1 = factory.cube(2, 2, 2, true).translate(1.5, 1.5, 1.5);
        Construction cube2 = factory.cube(2, 2, 2, true).translate(-1.5, -1.5, -1.5);
        Construction cube3 = factory.cube(2, 2, 2, true).translate(1.5, 1.5, -1.5);
        Construction cube4 = factory.cube(2, 2, 2, true).translate(1.5, -1.5, 1.5);
        Construction cube5 = factory.cube(2, 2, 2, true).translate(1.5, -1.5, -1.5);
        Construction cube6 = factory.cube(2, 2, 2, true);

        Solid difference = (Solid) cube6.difference(cube1).difference(cube2).difference(cube3).difference(cube4.union(cube5));

        STLWriter.writeToFile("build/difference-cube-3.stl", difference);

    }

    @Test
    public void testDifference4() throws Exception {

        CSGSolidBuilder factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(true).build();

        Construction cube1 = factory.cube(2, 2, 2, true).translate(1.5, 1.5, 1.5);
        Construction cube2 = factory.cube(2, 2, 2, true).translate(-1.5, -1.5, -1.5);
        Construction cube6 = factory.cube(2, 2, 2, true);

        Solid difference = (Solid) cube6.difference(cube1).difference(cube2);

        STLWriter.writeToFile("build/difference-cube-4.stl", difference);

    }

}
