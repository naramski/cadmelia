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
import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExtrusionTesselation<T extends Solid> extends Tesselation<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtrusionTesselation.class);

    private final Shape shape;

    private final double height;

    public ExtrusionTesselation(Shape shape, double height, MeshToInternal<T> meshToSolid) {
        super(meshToSolid);
        this.shape = shape;
        this.height = height;
    }

    @Override
    protected List<Triangle> getPolygons() {
        List<Triangle> triangles = new ArrayList<>();

        LOGGER.info("Extrude " + shape + " with height " + height);
        List<PolygonWithHoles> polygons = shape.getPolygons();

        for (PolygonWithHoles polygon : polygons) {

            triangles.addAll(triangulate(polygon, true, 0));

            List<Vector> bottom = toCCW(polygon.getExteriorRing());
            List<Vector> top = newRing(bottom);
            triangles.addAll(buildSides(bottom, top, true));

            triangles.addAll(triangulate(polygon, false, height));

            for (Polygon hole : polygon.getHoles()) {

                List<Vector> bottomHole = toCW(hole);
                List<Vector> topHole = newRing(bottomHole);
                triangles.addAll(buildSides(bottomHole, topHole, true));

            }

        }

        return triangles;
    }

    List<Triangle> triangulate(PolygonWithHoles polygonWithHoles, boolean invert, double height) {

        org.poly2tri.geometry.polygon.Polygon polygon = cameliaToPoly2Tri(polygonWithHoles);

        Poly2Tri.triangulate(polygon);

        List<Triangle> triangles = new ArrayList<>();

        for (DelaunayTriangle t : polygon.getTriangles()) {

            Vector v1 = new Vector(t.points[0].getX(), t.points[0].getY(), height);
            Vector v2 = new Vector(t.points[1].getX(), t.points[1].getY(), height);
            Vector v3 = new Vector(t.points[2].getX(), t.points[2].getY(), height);

            if (!invert) {
                triangles.add(new Triangle(v1, v2, v3));
            } else {
                triangles.add(new Triangle(v3, v2, v1));
            }

        }

        return triangles;

    }

    private org.poly2tri.geometry.polygon.Polygon cameliaToPoly2Tri(PolygonWithHoles polygonWithHoles) {

        org.poly2tri.geometry.polygon.Polygon polygon = cameliaToPoly2Tri(polygonWithHoles.getExteriorRing());

        for (Polygon p : polygonWithHoles.getHoles()) {
            org.poly2tri.geometry.polygon.Polygon hole = cameliaToPoly2Tri(p);
            polygon.addHole(hole);
        }

        return polygon;
    }

    private org.poly2tri.geometry.polygon.Polygon cameliaToPoly2Tri(Polygon polygon) {

        List<PolygonPoint> points = new ArrayList<>();

        for (Vector v : polygon.getPoints()) {
            PolygonPoint vp = new PolygonPoint(v.x(), v.y(), v.z());
            points.add(vp);
        }

        return new org.poly2tri.geometry.polygon.Polygon(points);
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
            transformed.add(point.plus(new Vector(0, 0, height)));
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
