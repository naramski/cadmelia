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

import net.nowina.cadmelia.Transformation;
import net.nowina.cadmelia.construction.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShapeImpl implements Shape, PolygonWithHoles, Polygon {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShapeImpl.class);

    private final Geometry geometry;

    public ShapeImpl(Geometry geometry) {
        if (geometry == null) {
            throw new NullPointerException("geometry must be defined");
        }
        this.geometry = geometry;
    }

    @Override
    public boolean isShape() {
        return true;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public List<PolygonWithHoles> getPolygons() {
        /* If the Shape is composed of several separated polygon, they are separated */
        if (geometry instanceof MultiPolygon) {
            MultiPolygon mp = (MultiPolygon) geometry;
            int numGeometries = mp.getNumGeometries();
            List<PolygonWithHoles> list = new ArrayList<>(numGeometries);
            for (int i = 0; i < numGeometries; i++) {
                list.add(new ShapeImpl(mp.getGeometryN(i)));
            }
            return list;
        } else {
            return Arrays.asList(this);
        }
    }

    @Override
    public Polygon getExteriorRing() {

        org.locationtech.jts.geom.Polygon p = (org.locationtech.jts.geom.Polygon) geometry;

        return new ShapeImpl(p.getExteriorRing());
    }

    @Override
    public List<Polygon> getHoles() {
        org.locationtech.jts.geom.Polygon p = (org.locationtech.jts.geom.Polygon) geometry;

        List<Polygon> polygons = new ArrayList<>();
        for (int i = 0; i < p.getNumInteriorRing(); i++) {
            polygons.add(new ShapeImpl(p.getInteriorRingN(i)));
        }
        return polygons;
    }

    @Override
    public List<Vector> getPoints() {
        List<Vector> points = new ArrayList<>();

        /* In Geometry, the loop are closed, the last en first point are the same, so we iterate from 0 to lenght-1 */
        for (int i = 0; i < getGeometry().getNumPoints() - 1; i++) {
            Coordinate c = getGeometry().getCoordinates()[i];
            points.add(new Vector(c.x, c.y));
        }
        return points;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    @Override
    public Shape union(Construction other) {
        if (!other.isShape()) {
            return this;
        }

        ShapeImpl shape = (ShapeImpl) other;
        LOGGER.info("Union of " + this + " with " + other);
        return new ShapeImpl(getGeometry().union(shape.getGeometry()));
    }

    @Override
    public Shape difference(Construction other) {
        if (!other.isShape()) {
            return this;
        }

        ShapeImpl shape = (ShapeImpl) other;

        return new ShapeImpl(getGeometry().difference(shape.getGeometry()));
    }

    @Override
    public Shape intersection(Construction other) {
        if (!other.isShape()) {
            return this;
        }

        ShapeImpl shape = (ShapeImpl) other;

        return new ShapeImpl(getGeometry().intersection(shape.getGeometry()));
    }

    @Override
    public Shape rotate(Vector angle) {

        Transformation t = Transformation.unity().rot(angle);
        LOGGER.info("Execute rotation " + angle + ":\n" + t);
        return executeTransform(this, t);
    }

    @Override
    public Shape translate(Vector vector) {

        Transformation t = Transformation.unity().translate(vector);
        LOGGER.info("Execute translate " + vector + ":\n" + t);
        Shape transformed = executeTransform(this, t);
        LOGGER.info("Transformed " + transformed + " : " + transformed.getPolygons().get(0).getExteriorRing().getPoints().size());
        return transformed;
    }

    @Override
    public Shape scale(Vector vector) {

        Transformation t = Transformation.unity().scale(vector);
        LOGGER.info("Execute scale " + vector + ":\n" + t);
        return executeTransform(this, t);
    }

    private Shape executeTransform(Construction c, Transformation t) {

        // TODO : rely on underlying framework for transformation
        Shape shape = (Shape) c;

        GeometryFactory factory = new GeometryFactory();

        Geometry union = null;
        for (PolygonWithHoles polygon : shape.getPolygons()) {

            List<Coordinate> shell = new ArrayList<>();
            for (Vector v : polygon.getExteriorRing().getPoints()) {

                Vector tr = t.transform(v);
                shell.add(new Coordinate(tr.x(), tr.y()));

            }

            /* With Geometries, polygon must be closed, and first and last point must be the same */
            shell.add(shell.get(0));

            Geometry geometry = factory.createPolygon(shell.toArray(new Coordinate[shell.size()]));
            if (union == null) {
                union = geometry;
            } else {
                union = union.union(geometry);
            }

        }

        return new ShapeImpl(union);
    }

    @Override
    public void visit(MeshVisitor visitor) {

        List<PolygonWithHoles> polygons = getPolygons();

        for (PolygonWithHoles polygon : polygons) {
            triangulate(polygon, visitor);
        }

    }

    void triangulate(PolygonWithHoles polygonWithHoles, MeshVisitor visitor) {

        org.poly2tri.geometry.polygon.Polygon polygon = cameliaToPoly2Tri(polygonWithHoles);

        Poly2Tri.triangulate(polygon);

        for (DelaunayTriangle t : polygon.getTriangles()) {

            Vector v1 = new Vector(t.points[0].getX(), t.points[0].getY(), 0);
            Vector v2 = new Vector(t.points[1].getX(), t.points[1].getY(), 0);
            Vector v3 = new Vector(t.points[2].getX(), t.points[2].getY(), 0);

            Vector normal = v2.minus(v1).crossed(v3.minus(v1)).normalized();
            visitor.startFacet(normal);
            visitor.triangle(v1, v2, v3);
            visitor.endFacet();

        }

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

}
