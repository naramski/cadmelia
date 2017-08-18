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
import net.nowina.cadmelia.construction.*;
import org.fxyz3d.geometry.Point3D;
import org.fxyz3d.shapes.primitives.helper.LineSegment;
import org.fxyz3d.shapes.primitives.helper.Text3DHelper;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JTSClipperShapeBuilder implements ShapeBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(JTSClipperShapeBuilder.class);

    @Override
    public Shape offset(Construction c, double offset) {

        Shape shape = (Shape) c;

        ClipperOffset clipper = new ClipperOffset();

        for (PolygonWithHoles polygon : shape.getPolygons()) {

            Path path = new Path();
            for (Vector v : polygon.getExteriorRing().getPoints()) {
                path.add(new Point(v.x(), v.y()));
            }

            clipper.addPath(path, Clipper.JoinType.MITER, Clipper.EndType.CLOSED_POLYGON);

        }

        Paths solution = new Paths();
        clipper.execute(solution, offset);

        GeometryFactory factory = new GeometryFactory();

        Geometry union = null;

        for (Path path : solution) {

            List<Coordinate> shell = new ArrayList<>();
            for (Point p : path) {
                shell.add(new Coordinate(p.getX(), p.getY()));
            }
            shell.add(shell.get(0));

            Geometry geometry = factory.createPolygon(shell.toArray(new Coordinate[shell.size()]));
            if (union == null) {
                union = geometry;
            } else {
                union = union.union(geometry);
            }

        }

        return new JTSClipperShape(union);
    }

    @Override
    public Shape circle(double radius, int slices) {

        if (radius < 0) {
            throw new IllegalArgumentException("Radius must be > 0");
        }

        if (slices < 3) {
            throw new IllegalArgumentException("Slices must be at least 3");
        }

        LOGGER.info("Create circle of radius " + radius + " with " + slices + " slices.");

        Coordinate[] shell = new Coordinate[slices + 1];

        for (int i = 0; i < slices; i++) {
            double theta = Math.PI * 2 * i / (double) slices;
            shell[i] = new Coordinate(Math.cos(theta) * radius, Math.sin(theta) * radius);
        }

        shell[slices] = shell[0];

        GeometryFactory factory = new GeometryFactory();

        return new JTSClipperShape(factory.createPolygon(shell));
    }

    @Override
    public Shape square(double sizeX, double sizeY, boolean centered) {

        if (sizeX < 0) {
            throw new IllegalArgumentException("sizeX must be > 0");
        }

        if (sizeY < 0) {
            throw new IllegalArgumentException("sizeY must be > 0");
        }

        Coordinate[] shell = new Coordinate[5];
        shell[0] = new Coordinate(centered ? sizeX / 2 : sizeX, centered ? sizeY / 2 : sizeY);
        shell[1] = new Coordinate(centered ? sizeX / 2 : sizeX, centered ? -sizeY / 2 : 0);
        shell[2] = new Coordinate(centered ? -sizeX / 2 : 0, centered ? -sizeY / 2 : 0);
        shell[3] = new Coordinate(centered ? -sizeX / 2 : 0, centered ? sizeY / 2 : sizeY);
        shell[4] = shell[0];

        GeometryFactory factory = new GeometryFactory();

        return new JTSClipperShape(factory.createPolygon(shell));
    }

    @Override
    public Shape polygon(List<Vector> points) {

        Coordinate[] shell = new Coordinate[points.size() + 1];
        for (int i = 0; i < points.size(); i++) {
            shell[i] = new Coordinate(points.get(i).x(), points.get(i).y());
        }
        shell[points.size()] = shell[0];


        GeometryFactory factory = new GeometryFactory();
        return new JTSClipperShape(factory.createPolygon(shell));
    }

    @Override
    public Shape text(String message, int fontSize, String font) {

        GeometryFactory factory = new GeometryFactory();

        Text3DHelper helper = new Text3DHelper(message, font, fontSize);

        List<Polygon> polygons = new ArrayList<>();

        // One line segment per letter / point (lower case i is two segments, upper case I is one segment)
        List<LineSegment> segments = helper.getLineSegment();
        for (LineSegment segment : segments) {

            CoordinateList shell = new CoordinateList();
            for (Point3D p : segment.getPoints()) {
                shell.add(new Coordinate(p.x, p.y));
            }

            LinearRing polygon = factory.createLinearRing(shell.toCoordinateArray());

            List<LinearRing> holes = new ArrayList<>();
            for (LineSegment holeSegment : segment.getHoles()) {

                CoordinateList hole = new CoordinateList();
                for (Point3D p : holeSegment.getPoints()) {
                    hole.add(new Coordinate(p.x, p.y));
                }

                holes.add(factory.createLinearRing(hole.toCoordinateArray()));

            }

            LinearRing[] holesArray = holes.toArray(new LinearRing[holes.size()]);

            Polygon polygon2 = factory.createPolygon(polygon, holesArray);

            polygons.add(polygon2);

        }

        MultiPolygon text = factory.createMultiPolygon(polygons.toArray(new Polygon[polygons.size()]));
        return new JTSClipperShape(text);

    }

}
