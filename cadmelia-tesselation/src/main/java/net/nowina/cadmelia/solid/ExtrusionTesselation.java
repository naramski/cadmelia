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

import net.nowina.cadmelia.Triangle;
import net.nowina.cadmelia.construction.*;
import net.nowina.cadmelia.shape.ShapeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExtrusionTesselation<T extends Solid> extends Tesselation<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtrusionTesselation.class);

    private final Shape shape;

    private final double height;

    private final double scaleX;

    private final double scaleY;

    private final double slices = 1;

    public ExtrusionTesselation(Shape shape, double height, double scale, MeshToInternal<T> meshToSolid) {
        super(meshToSolid);
        this.shape = shape;
        this.height = height;
        this.scaleX = scale;
        this.scaleY = scale;
    }

    public Vector slice(double slice, Vector p) {
        double x = p.x() * scaleX;
        double y = p.y() * scaleY;
        double z = p.z() + height * slice;
        return new Vector(x, y, z);
    }

    @Override
    protected List<Triangle> getPolygons() {
        List<Triangle> triangles = new ArrayList<>();

        LOGGER.info("Extrude " + shape + " with height " + height);
        List<PolygonWithHoles> polygons = shape.getPolygons();

        shape.visit((p1,p2,p3) -> {
            triangles.add(new Triangle(p3, p2, p1));
            double slice = 1.0;
            triangles.add(new Triangle(slice(slice, p1), slice(slice, p2), slice(slice, p3)));
        });

        for (PolygonWithHoles polygon : polygons) {

            List<Vector> bottom = toCCW(polygon.getExteriorRing());
            List<Vector> top = newRing(bottom);
            triangles.addAll(buildSides(bottom, top, true));

            for (Polygon hole : polygon.getHoles()) {

                List<Vector> bottomHole = toCW(hole);
                List<Vector> topHole = newRing(bottomHole);
                triangles.addAll(buildSides(bottomHole, topHole, true));

            }

        }

        return triangles;
    }

    private List<Triangle> buildSides(List<Vector> bottom, List<Vector> top, boolean invert) {

        List<Triangle> triangles = new ArrayList<>();
        for (int i = 0; i < bottom.size(); i++) {
            int next = (i + 1) % bottom.size();

            Vector b1 = bottom.get(i);
            Vector b2 = bottom.get(next);
            Vector t1 = top.get(i);
            Vector t2 = top.get(next);

            if (!invert) {
                triangles.add(new Triangle(t1, b2, b1));
                triangles.add(new Triangle(t2, b2, t1));
            } else {
                triangles.add(new Triangle(b1, b2, t1));
                triangles.add(new Triangle(t1, b2, t2));
            }


        }

        return triangles;

    }

    private List<Vector> newRing(List<Vector> points) {
        List<Vector> transformed = new ArrayList<>();
        for (Vector point : points) {
            transformed.add(slice(1.0, point));
        }
        return transformed;
    }

    private List<Vector> toCCW(Polygon polygon) {

        List<Vector> result = new ArrayList<>(polygon.getPoints());
        if (ShapeUtil.isCW(polygon)) {
            Collections.reverse(result);
        }

        return result;
    }

    private List<Vector> toCW(Polygon polygon) {

        List<Vector> result = new ArrayList<>(polygon.getPoints());
        if (ShapeUtil.isCCW(polygon)) {
            Collections.reverse(result);
        }

        return result;
    }

}
