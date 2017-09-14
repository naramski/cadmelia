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
package net.nowina.cadmelia.solid;

import net.nowina.cadmelia.construction.*;
import net.nowina.cadmelia.shape.impl.ShapeImplBuilder;
import net.nowina.cadmelia.stl.STLWriter;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ExtrusionTest {

    @Test
    public void testSquare() throws Exception {
        ShapeBuilder shapeBuilder = new ShapeImplBuilder();
        Shape shape = shapeBuilder.square(5, 5, true);
        ExtrusionTesselation extrusion = new ExtrusionTesselation(shape, 4, new MockMeshToSolid());
        Solid solid = extrusion.buildSolid();

        STLWriter.writeToFile("build/extrusion-square.stl", solid);
    }

    @Test
    public void testCircle() throws Exception {
        ShapeBuilder shapeBuilder = new ShapeImplBuilder();
        Shape shape = shapeBuilder.circle(4, 16);
        ExtrusionTesselation extrusion = new ExtrusionTesselation(shape, 4, new MockMeshToSolid());
        Solid solid = extrusion.buildSolid();

        STLWriter.writeToFile("build/extrusion-circle.stl", solid);
    }

    @Test
    public void testBatteryHolder() throws Exception {

        ShapeBuilder shapeBuilder = new ShapeImplBuilder();

        double connectorDepth = 25;

        double batteryHeight = 22;
        double batteryLength = 54;

        double footHeight = 25;
        double footSize = 10;

        double th = 3;
        double smh = 8.11;
        double mth = 3.0;

        double pth = 0.3;
        double ph = 1;
        double po = 0.6;

        double o = 13;

        List<Vector> points = Arrays.asList(
                new Vector(-th, -th),
                new Vector(smh + pth + ph+o, -th),
                new Vector(smh + pth + Math.max(ph / 3, 0.4)+o, 0 + po),
                new Vector(smh + pth+o, 0 + po),
                new Vector(smh+o, 0),
                new Vector(0+o, 0),
                new Vector(0+o, mth),
                new Vector(smh+o, mth),
                new Vector(smh+o, mth + th),
                new Vector(0, mth + th),
                new Vector(0, mth + th + batteryHeight),
                new Vector(batteryLength, mth + th + batteryHeight),
                new Vector(batteryLength, mth + th + batteryHeight * 0.3),
                new Vector(batteryLength + th, mth + th + batteryHeight * 0.3),
                new Vector(batteryLength + th, mth + th + batteryHeight + th),
                new Vector(0, mth + th + batteryHeight + th),
                new Vector(0, mth + th + batteryHeight + th + footHeight - th * 2),
                new Vector(footSize, mth + th + batteryHeight + th + footHeight - th),
                new Vector(footSize, mth + th + batteryHeight + th + footHeight),
                new Vector(-th, mth + th + batteryHeight + th + footHeight)
        );

        Shape shape = shapeBuilder.polygon(points);

        ExtrusionTesselation extrusion = new ExtrusionTesselation(shape, 25, new MockMeshToSolid());
        Solid solid = extrusion.buildSolid();

        STLWriter.writeToFile("build/extrusion-battery-holder.stl", solid);

    }

    @Test
    public void testDonuts() throws Exception {
        ShapeBuilder shapeBuilder = new ShapeImplBuilder();
        Shape shape1 = shapeBuilder.square(4, 4, true);
        Shape shape2 = shapeBuilder.square(2, 2, true);

        Shape shape = (Shape) shape1.difference(shape2);
        ExtrusionTesselation extrusion = new ExtrusionTesselation(shape, 4, new MockMeshToSolid());
        Solid solid = extrusion.buildSolid();

        STLWriter.writeToFile("build/extrusion-donuts.stl", solid);
    }

    @Test
    public void testTwoHoles() throws Exception {
        ShapeBuilder shapeBuilder = new ShapeImplBuilder();
        Shape shape1 = (Shape) shapeBuilder.square(6, 4, true);
        Shape shape2 = (Shape) shapeBuilder.square(2, 2, true).translate(new Vector(-1.5, 0, 0));
        Shape shape3 = (Shape) shapeBuilder.square(2, 2, true).translate(new Vector(1.5, 0, 0));

        Shape shape = (Shape) shape1.difference(shape2).difference(shape3);
        ExtrusionTesselation extrusion = new ExtrusionTesselation(shape, 4, new MockMeshToSolid());
        Solid solid = extrusion.buildSolid();

        STLWriter.writeToFile("build/extrusion-two-holes.stl", solid);
    }

}
