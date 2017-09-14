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

}
