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

import de.lighti.clipper.*;
import de.lighti.clipper.Point;
import org.junit.Test;
import org.locationtech.jts.geom.*;

public class STLGeomTest {

    @Test
    public void testUnion() {

        Geometry union = createGeometry();
        Coordinate[] points;

        points = union.getCoordinates();
        for (Coordinate c : points) {
            System.out.println("+++ " + c.x + " - " + c.y);
        }

    }

    @Test
    public void testOffset() {

        ClipperOffset offset = new ClipperOffset();

        Path rectangle = new Path();
        Geometry geom = createGeometry();
        for (int i = 0; i < geom.getNumPoints(); i++) {
            Coordinate c = geom.getCoordinates()[i];
            Point e = new Point();
            e.setX(c.x * 10);
            e.setY(c.y * 10);
            rectangle.add(e);
        }

        offset.addPath(rectangle, Clipper.JoinType.MITER, Clipper.EndType.CLOSED_POLYGON);

        Paths paths = new Paths();
        offset.execute(paths, 2);

        Paths result = paths;
        System.out.println(result.size() + " path(s)");
        Path path = result.get(0);
        for (Point p : path) {
            System.out.println("Result " + p.getX() + " - " + p.getY());
        }

    }


    @Test
    public void testOffset2() {

        ClipperOffset offset = new ClipperOffset();

        Path subj = new Path();
        subj.add(new Point(348, 257));
        subj.add(new Point(362, 148));
        subj.add(new Point(326, 241));
        subj.add(new Point(295, 219));
        subj.add(new Point(258, 88));
        subj.add(new Point(440, 129));
        subj.add(new Point(370, 196));
        subj.add(new Point(372, 275));

        offset.addPath(subj, Clipper.JoinType.MITER, Clipper.EndType.CLOSED_POLYGON);

        Paths solution = new Paths();
        offset.execute(solution, -7.0);

        Paths result = solution;
        System.out.println(result.size() + " path(s)");
        Path path = result.get(0);
        for (Point p : path) {
            System.out.println("Result " + p.getX() + " - " + p.getY());
        }

    }

    private Geometry createGeometry() {
        GeometryFactory factory = new GeometryFactory();

        Coordinate[] coords =
                new Coordinate[]{new Coordinate(4, 0), new Coordinate(2, 2),
                        new Coordinate(4, 4), new Coordinate(6, 2), new Coordinate(4, 0)};

        LinearRing ring = factory.createLinearRing(coords);
        LinearRing holes[] = null; // use LinearRing[] to represent holes
        Polygon polygon = factory.createPolygon(ring, holes);

        Coordinate[] points = polygon.getCoordinates();
        for (Coordinate c : points) {
            System.out.println("*** " + c.x + " - " + c.y);
        }


        coords =
                new Coordinate[]{new Coordinate(6, 0), new Coordinate(4, 2),
                        new Coordinate(6, 4), new Coordinate(8, 2), new Coordinate(6, 0)};

        ring = factory.createLinearRing(coords);
        Polygon rectangle = factory.createPolygon(ring, holes);

        points = rectangle.getCoordinates();
        for (Coordinate c : points) {
            System.out.println(c.x + " - " + c.y);
        }

        GeometryCollection collection = new GeometryCollection(new Geometry[]{rectangle, polygon}, factory);
        return collection.union();
    }

    @Test
    public void testOffset3() {

        ClipperOffset offset = new ClipperOffset();

        Path rectangle = new Path();
        Geometry geom = createGeometry();
        for (int i = 0; i < geom.getNumPoints(); i++) {
            Coordinate c = geom.getCoordinates()[i];
            Point e = new Point();
            e.setX(c.x);
            e.setY(c.y);
            rectangle.add(e);
        }

        offset.addPath(rectangle, Clipper.JoinType.MITER, Clipper.EndType.CLOSED_POLYGON);

        Paths paths = new Paths();
        offset.execute(paths, +0.3);

        Paths result = paths;
        System.out.println(result.size() + " path(s)");
        Path path = result.get(0);
        for (Point p : path) {
            System.out.println("Result " + p.getX() + " - " + p.getY());
        }

    }

}
