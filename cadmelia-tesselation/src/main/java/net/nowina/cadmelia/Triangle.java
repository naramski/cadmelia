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
package net.nowina.cadmelia;

import net.nowina.cadmelia.construction.Vector;

import java.util.List;

public class Triangle {

    Vector normal;

    Vector points[];

    public Triangle(Vector p1, Vector p2, Vector p3) {
        this.points = new Vector[]{p1, p2, p3};
        this.normal = p2.minus(p1).crossed(p3.minus(p1)).normalized();
    }

    public Triangle(Vector normal, List<Vector> points) {
        this.points = points.toArray(new Vector[points.size()]);
        this.normal = normal;
    }

    private Vector p1() {
        return points[0];
    }

    private Vector p2() {
        return points[1];
    }

    private Vector p3() {
        return points[2];
    }

    /**
     * Check if this triangle has an adjacent edge to the other triangle.
     *
     * @param other
     * @return
     */
    public boolean touchEdge(Triangle other) {
        Vector p1 = p1();
        Vector p2 = p2();
        Vector p3 = p3();
        Vector otherP1 = other.p1();
        Vector otherP2 = other.p2();
        Vector otherP3 = other.p3();
        if (p1 == otherP1 || p1 == otherP2 || p1 == otherP3) {
            if (p2 == otherP1 || p2 == otherP2 || p2 == otherP3) {
                return true;
            } else if (p3 == otherP1 || p3 == otherP2 || p3 == otherP3) {
                return true;
            }
        } else if (p2 == otherP1 || p2 == otherP2 || p2 == otherP3) {
            if (p3 == otherP1 || p3 == otherP2 || p3 == otherP3) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Triangle[" + p1() + "," + p2() + "," + p3() + "]";
    }

    public Vector getNormal() {
        return normal;
    }

    public Vector[] getPoints() {
        return points;
    }

}
