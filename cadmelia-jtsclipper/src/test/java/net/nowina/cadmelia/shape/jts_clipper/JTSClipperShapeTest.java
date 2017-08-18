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
package net.nowina.cadmelia.shape.jts_clipper;

import net.nowina.cadmelia.construction.BuilderFactory;
import net.nowina.cadmelia.construction.Shape;
import net.nowina.cadmelia.construction.ShapeBuilder;
import net.nowina.cadmelia.construction.Vector;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

import java.util.Arrays;

public class JTSClipperShapeTest {

    @BeforeClass
    public static void setUp() {
        BuilderFactory.registerShapeBuilder(new JTSClipperShapeBuilder());
    }

    @Test
    public void testUnion() {
        ShapeBuilder builder = BuilderFactory.getInstance().createShapeBuilder();
        Shape square1 = (Shape) builder.square(2, 2, true).translate(Vector.X.times(-2d));
        System.out.println(square1.getPolygons().get(0).getExteriorRing().getPoints());
        Shape square2 = (Shape) builder.square(2, 2, true).translate(Vector.X.times(2d));
        System.out.println(square2.getPolygons().get(0).getExteriorRing().getPoints());

        JTSClipperShape shape = (JTSClipperShape) square1.union(square2);

        System.out.println(shape.getPoints());

        Geometry g = shape.getGeometry();
        // There is two separated squares, so two geometries
        Assert.assertEquals(2, g.getNumGeometries());

        // There is one more point in the square, as the loop is closed
        Assert.assertEquals(5, g.getGeometryN(0).getNumPoints());
        System.out.println(g.getGeometryN(0).isEmpty());
        Assert.assertEquals(5, g.getGeometryN(1).getNumPoints());
        System.out.println(g.getGeometryN(0).isEmpty());
    }

    @Test
    public void testPolygon() {
        ShapeBuilder builder = BuilderFactory.getInstance().createShapeBuilder();
        Shape square1 = (Shape) builder.polygon(Arrays.asList(new Vector(-1, 1), new Vector(-1, -1), new Vector(1, -1), new Vector(1, 1))).translate(Vector.X.times(-2d));
        System.out.println(square1.getPolygons().get(0).getExteriorRing().getPoints());
        Shape square2 = (Shape) builder.polygon(Arrays.asList(new Vector(1, -1), new Vector(-1, -1), new Vector(-1, 1), new Vector(1, 1))).translate(Vector.X.times(2d));
        System.out.println(square2.getPolygons().get(0).getExteriorRing().getPoints());

        JTSClipperShape shape = (JTSClipperShape) square1.union(square2);

        System.out.println(shape.getPoints());

        Geometry g = shape.getGeometry();
        MultiPolygon p = (MultiPolygon) g;
        System.out.println(p.getGeometryType());
        // Enveloppe is the boundary of all the union
        System.out.println(Arrays.asList(g.getEnvelope().getCoordinates()));
        System.out.println(Arrays.asList(g.getCoordinates()));

        // There is two separated squares, so two geometries
        Assert.assertEquals(2, g.getNumGeometries());

        // There is one more point in the square, as the loop is closed
        Assert.assertEquals(5, g.getGeometryN(0).getNumPoints());
        System.out.println(g.getGeometryN(0).isEmpty());
        Assert.assertEquals(5, g.getGeometryN(1).getNumPoints());
        System.out.println(g.getGeometryN(0).isEmpty());
    }

    @Test
    public void testDifference() {
        ShapeBuilder builder = BuilderFactory.getInstance().createShapeBuilder();
        Shape square1 = builder.square(4, 4, true);
        System.out.println(square1.getPolygons().get(0).getExteriorRing().getPoints());
        Shape square2 = builder.square(2, 2, true);
        System.out.println(square2.getPolygons().get(0).getExteriorRing().getPoints());

        JTSClipperShape shape = (JTSClipperShape) square1.difference(square2);

        System.out.println(shape.getPoints());

        Geometry g = shape.getGeometry();
        Assert.assertEquals(1, g.getNumGeometries());
        System.out.println(Arrays.asList(g.getCoordinates()));

        // There is one more point in the square, as the loop is closed
        System.out.println(g.getClass());
        Polygon p = (Polygon) g;
        System.out.println(p.getNumInteriorRing());

        Assert.assertEquals(1, p.getNumInteriorRing());

        Assert.assertEquals(10, g.getGeometryN(0).getNumPoints());
    }

    @Test
    public void testSquare() {
        ShapeBuilder builder = BuilderFactory.getInstance().createShapeBuilder();
        Shape square1 = (Shape) builder.square(2, 2, true).translate(Vector.X.times(-2d));

        System.out.println(square1.getPolygons().get(0).getExteriorRing().getPoints());

    }

    @Test
    public void testUnion3SeparatedShapes() {
        ShapeBuilder builder = BuilderFactory.getInstance().createShapeBuilder();
        Shape square1 = (Shape) builder.square(2, 2, true).translate(Vector.X.times(-2d));
        System.out.println(square1.getPolygons().get(0).getExteriorRing().getPoints());
        Shape square2 = (Shape) builder.square(2, 2, true).translate(Vector.X.times(2d));
        System.out.println(square2.getPolygons().get(0).getExteriorRing().getPoints());
        Shape square3 = (Shape) builder.square(2, 2, true).translate(Vector.X.times(5d));
        System.out.println(square3.getPolygons().get(0).getExteriorRing().getPoints());

        JTSClipperShape shape = (JTSClipperShape) square1.union(square2).union(square3);

        System.out.println(shape.getPoints());

        Geometry g = shape.getGeometry();

        if (g instanceof MultiPolygon) {
            System.out.println("Multiploygon " + g.getGeometryType());
            MultiPolygon mp = (MultiPolygon) g;
            Assert.assertEquals(3, mp.getNumGeometries());

            for (int i = 0; i < mp.getNumGeometries(); i++) {

                Geometry g1 = mp.getGeometryN(i);
                System.out.println(g1.getGeometryType());

            }

        }

        // There is two separated squares, so two geometries
        Assert.assertEquals(3, g.getNumGeometries());

        // There is one more point in the square, as the loop is closed
        Assert.assertEquals(5, g.getGeometryN(0).getNumPoints());
        System.out.println(g.getGeometryN(0).isEmpty());
        Assert.assertEquals(5, g.getGeometryN(1).getNumPoints());
        System.out.println(g.getGeometryN(0).isEmpty());

        Shape bar = builder.square(12, 1, true);
        System.out.println(bar.getPolygons().get(0).getExteriorRing().getPoints());
        Shape united = shape.union(bar);
        System.out.println(united.getPolygons().get(0).getExteriorRing().getPoints());
        System.out.println(((JTSClipperShape) united).getGeometry().getGeometryType());

        Assert.assertEquals(1, ((JTSClipperShape) united).getGeometry().getNumGeometries());

    }
}
