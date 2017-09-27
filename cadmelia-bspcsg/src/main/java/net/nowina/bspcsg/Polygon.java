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
package net.nowina.bspcsg;

import net.nowina.bspcsg.collection.VectorList;
import net.nowina.bspcsg.collection.VectorListBrowser;
import net.nowina.cadmelia.Transformation;
import net.nowina.cadmelia.Triangle;
import net.nowina.cadmelia.construction.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon {

    private Factory builder;

    private VectorList vertices;

    private Vector normal;

    public Polygon(Factory builder, VectorList vertices, Vector normal) {
        this.vertices = vertices;
        this.normal = normal == null ? buildNormal(vertices) : normal;
        this.builder = builder;
    }

    private Polygon(Factory builder, Polygon original) {
        this(builder, original.vertices.copy(), original.normal);
    }

    /**
     * Don't forget to override this in the child
     *
     * @return
     */
    public Polygon copy() {
        return new Polygon(builder, this);
    }

    public void flip() {

        vertices.reverse();
        normal = getNormal().negated();
    }

    public List<Triangle> toTriangle() {

        List<Triangle> triangles = new ArrayList<>();
        VectorListBrowser browser = browseVertices();
        if (browser.size() >= 3) {
            Vector first = browser.index(0).get();
            Vector normal = getNormal();
            for (int i = 0; i < browser.size() - 2; i++) {
                Vector second = browser.index(i + 1).get();
                Vector third = browser.index(i + 2).get();
                Triangle triangle = new Triangle(normal, Arrays.asList(first, second, third));
                triangles.add(triangle);
            }
        }
        return triangles;

    }

    protected void transform(Transformation transform) {

        vertices.applyTransformation(transform);

        this.normal = buildNormal(vertices);

        if (transform.matrixDeterminant() < 0) {
            flip();

        }

    }

    public Polygon transformed(Transformation transform) {
        Polygon polygon = this.copy();
        polygon.transform(transform);
        return polygon;
    }

    public static Vector buildNormal(VectorList vertices) {
        /* Let a,b,c the three first vertices. The normal is b.minus(a).crossed(c.minus(a)).normalized(). Those
           computation are made with the Browser for performance.
         */
        VectorListBrowser browser = vertices.browse();

        double ax = browser.x();
        double ay = browser.y();
        double az = browser.z();

        browser.next();
        double bmax = browser.x() - ax;
        double bmay = browser.y() - ay;
        double bmaz = browser.z() - az;

        browser.next();
        double cmax = browser.x() - ax;
        double cmay = browser.y() - ay;
        double cmaz = browser.z() - az;

        // direction of normal.
        double dx = bmay * cmaz - bmaz * cmay;
        double dy = bmaz * cmax - bmax * cmaz;
        double dz = bmax * cmay - bmay * cmax;

        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);

        return new Vector(dx / length, dy / length, dz / length);
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "vertices=" + vertices +
                '}';
    }

    public VectorList getVertices() {
        return vertices;
    }

    public VectorListBrowser browseVertices() {
        return vertices.browse();
    }

    public Vector getNormal() {
        if (normal == null) {
            normal = buildNormal(vertices);
        }
        return normal;
    }

    public boolean isValid() {
        Vector normal = getNormal();
        return isValid(normal);
    }

    public static boolean isValid(Vector normal) {
        return normal.x() != Double.NaN && normal.y() != Double.NaN && normal.z() != Double.NaN;
    }

    public Factory getBuilder() {
        return builder;
    }

}
