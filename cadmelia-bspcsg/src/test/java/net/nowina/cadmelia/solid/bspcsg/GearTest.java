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

public class GearTest {

    @Test
    public void test1() throws Exception {

        // difference() {
        //    cylinder(r1=4,r2=66.4634,h=63.5,$fn=6);
        //    translate([0,0,-.05])cylinder(r1=0,r2=62.4634,h=63.6,$fn=6);
        // }

        FactoryBuilder builder = new FactoryBuilder();
        builder.withoutRecursion(false);
        builder.usingDecomposablePolygon(false);
        builder.usingComposite(false);
        builder.discardingInvalidPolygon(false);

        CSGSolidFactory factory = builder.build();

        Solid c1 = factory.cylinder(4, 66.4634, 63.5, 6, false);
        Solid c2 = factory.cylinder(0, 62.4634, 63.6, 6, false);

        Construction diff = c1.difference(c2.translate(0, 0, -0.05));

        STLWriter.writeToFile("target/test-gear-cone.stl", diff);

    }

    @Test
    public void test2() throws Exception {

        FactoryBuilder builder = new FactoryBuilder();
        builder.withoutRecursion(false);
        builder.usingDecomposablePolygon(false);
        builder.usingComposite(false);
        builder.discardingInvalidPolygon(false);

        CSGSolidFactory factory = builder.build();

        Solid c1 = factory.cylinder(0, 60, 60, 6, false);
        Solid c2 = factory.cylinder(0, 58, 60, 6, false);

        Construction diff = c1.difference(c2);

        STLWriter.writeToFile("target/test-gear-cone-2.stl", diff);

    }

    @Test
    public void test3() throws Exception {

        FactoryBuilder builder = new FactoryBuilder();
        builder.withoutRecursion(false);
        builder.usingDecomposablePolygon(false);
        builder.usingComposite(false);
        builder.discardingInvalidPolygon(false);

        CSGSolidFactory factory = builder.build();

        Solid c1 = factory.cylinder( 60, 0,60, 6, false);
        Solid c2 = factory.cylinder( 58, 0,60, 6, false);

        Construction diff = c1.difference(c2);

        STLWriter.writeToFile("target/test-gear-cone-3.stl", diff);

    }

    @Test
    public void testCone() throws Exception {

        FactoryBuilder builder = new FactoryBuilder();
        builder.withoutRecursion(false);
        builder.usingDecomposablePolygon(false);
        builder.usingComposite(false);
        builder.discardingInvalidPolygon(false);

        CSGSolidFactory factory = builder.build();

        Solid c1 = factory.cylinder(0, 66.4634, 63.5, 6, false);

        STLWriter.writeToFile("target/test-cone.stl", c1);

    }

}
