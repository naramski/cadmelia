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
package net.nowina.cadmelia.shape.impl;

import net.nowina.cadmelia.construction.Shape;
import net.nowina.cadmelia.construction.ShapeBuilder;
import net.nowina.cadmelia.construction.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public abstract class ShapeBuilderTestAbstract {

    public static double EPSILON = 1e-8;

    private ShapeBuilder builder;

    @Before
    public void setUp() {
        builder = createShapeBuilder();
    }

    protected abstract ShapeBuilder createShapeBuilder();

    @Test
    public void testCreateSuare() {
        Shape shape = builder.square(10, 10, true);
        List<Vector> pts = shape.getPolygons().get(0).getExteriorRing().getPoints();
        Assert.assertEquals(4, pts.size());
        Vector centroid = centroid(pts);
        Assert.assertEquals(0.0d, centroid.x(), EPSILON);
        Assert.assertEquals(0.0d, centroid.y(), EPSILON);
    }

    private Vector centroid(List<Vector> pts) {
        Vector centroid = Vector.ZERO;
        for (Vector p : pts) {
            centroid = centroid.plus(p);
        }
        return centroid;
    }

    @Test
    public void testCircle() {
        Shape shape = builder.circle(10, 16);
        List<Vector> pts = shape.getPolygons().get(0).getExteriorRing().getPoints();
        Assert.assertEquals(16, pts.size());
        Vector centroid = centroid(pts);
        Assert.assertEquals(0.0d, centroid.x(), EPSILON);
        Assert.assertEquals(0.0d, centroid.y(), EPSILON);
    }

    /**
     * Difference between two squares result in one donuts.
     */
    @Test
    public void testDifference() {
        Shape square1 = builder.square(4, 4, true);
        Shape square2 = builder.square(2, 2, true);

        Shape shape = (Shape) square1.difference(square2);

        Assert.assertEquals(1, shape.getPolygons().size());
        List<Vector> pts = shape.getPolygons().get(0).getExteriorRing().getPoints();
        System.out.println(pts);
        Assert.assertEquals(4, pts.size());

        Assert.assertEquals(1, shape.getPolygons().get(0).getHoles().size());

        Vector centroid = centroid(pts);
        Assert.assertEquals(0.0d, centroid.x(), EPSILON);
        Assert.assertEquals(0.0d, centroid.y(), EPSILON);
    }

    @Test
    public void testUnion() {
        Shape square1 = (Shape) builder.square(2, 2, true).translate(Vector.X.times(-2d));
        Shape square2 = (Shape) builder.square(2, 2, true).translate(Vector.X.times(2d));

        Shape shape = (Shape) square1.union(square2);

        Assert.assertEquals(2, shape.getPolygons().size());

        List<Vector> pts = shape.getPolygons().get(0).getExteriorRing().getPoints();
        System.out.println(pts);
        Assert.assertEquals(4, pts.size());

    }

}
