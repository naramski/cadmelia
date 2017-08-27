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

        CSGSolidBuilder builder = new FactoryBuilder().build();

        Sphere sphere = new Sphere(builder, 1, 16, 8);

        STLWriter.writeToFile("build/test-sphere.stl", sphere.buildSolid());

    }

    @Test
    public void testUnion() throws Exception {

        CSGSolidBuilder builder = new FactoryBuilder().usingComposite(false).usingComposite(true).build();

        CSGSolid sphere = (CSGSolid) builder.sphere(1, 16, 8).translate(-2, 0, 0);
        Assert.assertEquals(224, sphere.getCSG().getPolygons().size());

        CSGSolid sphere2 = (CSGSolid) builder.sphere(1, 16, 8).translate(2, 0, 0);
        Assert.assertEquals(224, sphere2.getCSG().getPolygons().size());

        CSGSolid union = (CSGSolid) sphere.union(sphere2);
        Assert.assertEquals(448, union.getCSG().getPolygons().size());

    }

    @Test
    public void testUnion2() throws Exception {

        CSGSolidBuilder builder = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(true).usingComposite(false).build();

        CSGSolid sphere = (CSGSolid) builder.sphere(1, 16, 8).translate(-2, 0, 0);
        Assert.assertEquals(224, sphere.getCSG().getPolygons().size());

        CSGSolid sphere2 = (CSGSolid) builder.sphere(1, 16, 8).translate(2, 0, 0);
        Assert.assertEquals(224, sphere2.getCSG().getPolygons().size());

        CSGSolid union = (CSGSolid) sphere.union(sphere2);
        Assert.assertEquals(448, union.getCSG().getPolygons().size());

    }

    @Test
    public void testDifference1() throws Exception {

        CSGSolidBuilder factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(false).build();

        Construction sphere = factory.sphere(2, 32, 16).translate(0, 0, -1);
        Construction small = factory.sphere(1.5, 32, 16);

        Solid difference = (Solid) sphere.difference(small);

        STLWriter.writeToFile("build/difference-spheres-1.stl", difference);

    }

    @Test
    public void testDifference2() throws Exception {

        CSGSolidBuilder factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(true).build();

        Construction sphere = factory.sphere(2, 32, 16).translate(0, 0, -1);
        Construction small = factory.sphere(1.5, 32, 16);

        Solid difference = (Solid) sphere.difference(small);

        STLWriter.writeToFile("build/difference-spheres-2.stl", difference);

    }

    @Test
    public void testDifference3() throws Exception {

        CSGSolidBuilder factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(true).withoutRecursion(true).build();

        Construction sphere = factory.sphere(2, 160, 80).translate(0, 0, -1);
        Construction small = factory.sphere(1.5, 160, 80);

        Solid difference = (Solid) sphere.difference(small);

        STLWriter.writeToFile("build/difference-spheres-3.stl", difference);

    }

}
