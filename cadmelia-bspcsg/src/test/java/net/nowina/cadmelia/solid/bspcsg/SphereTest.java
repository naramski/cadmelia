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
import org.junit.Assert;
import org.junit.Test;

public class SphereTest {

    @Test
    public void test2() throws Exception {

        CSGSolidFactory factory = new FactoryBuilder().build();

        Sphere sphere = new Sphere(factory, 1, 16, 8);

        STLWriter.writeToFile("target/test-sphere.stl", sphere.buildSolid());

    }

    @Test
    public void testUnion() throws Exception {

        CSGSolidFactory factory = new FactoryBuilder().usingComposite(false).usingComposite(true).build();

        CSGSolid sphere = (CSGSolid) factory.sphere(1, 16, 8).translate(-2, 0, 0);
        Assert.assertEquals(224, sphere.getCSG().getPolygons().size());

        CSGSolid sphere2 = (CSGSolid) factory.sphere(1, 16, 8).translate(2, 0, 0);
        Assert.assertEquals(224, sphere2.getCSG().getPolygons().size());

        CSGSolid union = (CSGSolid) sphere.union(sphere2);
        Assert.assertEquals(448, union.getCSG().getPolygons().size());

    }

    @Test
    public void testUnion2() throws Exception {

        CSGSolidFactory factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(true).usingComposite(false).build();

        CSGSolid sphere = (CSGSolid) factory.sphere(1, 16, 8).translate(-2, 0, 0);
        Assert.assertEquals(224, sphere.getCSG().getPolygons().size());

        CSGSolid sphere2 = (CSGSolid) factory.sphere(1, 16, 8).translate(2, 0, 0);
        Assert.assertEquals(224, sphere2.getCSG().getPolygons().size());

        CSGSolid union = (CSGSolid) sphere.union(sphere2);
        Assert.assertEquals(448, union.getCSG().getPolygons().size());

    }

    @Test
    public void testDifference1() throws Exception {

        CSGSolidFactory factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(false).build();

        Construction sphere = factory.sphere(2, 32, 16).translate(0, 0, -1);
        Construction small = factory.sphere(1.5, 32, 16);

        Solid difference = (Solid) sphere.difference(small);

        STLWriter.writeToFile("target/difference-spheres-1.stl", difference);

    }

    @Test
    public void testDifference2() throws Exception {

        CSGSolidFactory factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(true).build();

        Construction sphere = factory.sphere(2, 32, 16).translate(0, 0, -1);
        Construction small = factory.sphere(1.5, 32, 16);

        Solid difference = (Solid) sphere.difference(small);

        STLWriter.writeToFile("target/difference-spheres-2.stl", difference);

    }

    // This one takes time, it is normal. The intended purpose is to see if we survive with a huge mesh
    @Test
    public void testDifference3() throws Exception {

        CSGSolidFactory factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(true).withoutRecursion(true).build();

        Construction sphere = factory.sphere(2, 80, 40).translate(0, 0, -1);
        Construction small = factory.sphere(1.5, 80, 40);

        Solid difference = (Solid) sphere.difference(small);

        STLWriter.writeToFile("target/difference-spheres-3.stl", difference);

    }

    @Test
    public void testDifference4() throws Exception {

        CSGSolidFactory factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(false).withoutRecursion(true).build();

        Construction cyl1 = factory.cylinder(4, 66.4633, 63.50, 6, false);
        Construction cyl2 = factory.cylinder(1, 62.4633, 63.6, 6, false).translate(0, 0, -0.05);

        Solid difference = (Solid) cyl1.difference(cyl2);

        STLWriter.writeToFile("target/difference-cone.stl", difference);

    }

}
