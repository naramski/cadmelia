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

import net.nowina.cadmelia.construction.*;
import org.fxyz3d.shapes.primitives.helper.LineSegment;
import org.fxyz3d.shapes.primitives.helper.Text3DHelper;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ShapeImplBuilder implements ShapeBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShapeImplBuilder.class);

    @Override
    public Shape offset(Construction c, double offset) {

        Shape shape = (Shape) c;
        ShapeImpl jts = (ShapeImpl) shape;
        return new ShapeImpl(jts.getGeometry().buffer(offset));

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

        return new ShapeImpl(factory.createPolygon(shell));
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

        return new ShapeImpl(factory.createPolygon(shell));
    }

    @Override
    public Shape polygon(List<net.nowina.cadmelia.construction.Vector> points) {

        Coordinate[] shell = new Coordinate[points.size() + 1];
        for (int i = 0; i < points.size(); i++) {
            shell[i] = new Coordinate(points.get(i).x(), points.get(i).y());
        }
        shell[points.size()] = shell[0];


        GeometryFactory factory = new GeometryFactory();
        return new ShapeImpl(factory.createPolygon(shell));
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
            for (Vector p : segment.getPoints()) {
                shell.add(new Coordinate(p.x(), - p.y()));
            }

            LinearRing polygon = factory.createLinearRing(shell.toCoordinateArray());

            List<LinearRing> holes = new ArrayList<>();
            for (LineSegment holeSegment : segment.getHoles()) {

                CoordinateList hole = new CoordinateList();
                for (Vector p : holeSegment.getPoints()) {
                    hole.add(new Coordinate(p.x(), - p.y()));
                }

                holes.add(factory.createLinearRing(hole.toCoordinateArray()));

            }

            LinearRing[] holesArray = holes.toArray(new LinearRing[holes.size()]);

            Polygon polygon2 = factory.createPolygon(polygon, holesArray);

            polygons.add(polygon2);

        }

        MultiPolygon text = factory.createMultiPolygon(polygons.toArray(new Polygon[polygons.size()]));
        return new ShapeImpl(text);

    }

}
