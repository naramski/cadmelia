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
import net.nowina.cadmelia.construction.Vector;

public class Plane {

    public static double EPSILON = 1e-8;

    private Vector normal;

    private double dist;

    public Plane(Polygon polygon) {
        VectorList vectorList = polygon.getVertices();

        /* Let a,b,c the three first vertices. The normal is b.minus(a).crossed(c.minus(a)).normalized(). Those
           computation are made with the Browser for performance.
         */
        VectorListBrowser browser = vectorList.browse();

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

        this.normal = new Vector(dx / length, dy / length, dz / length);

        this.dist = normal.x() * ax + normal.y() * ay + normal.z() * az;
    }

    public void flip() {
        normal = normal.negated();
        dist = -dist;
    }

    public double getDist() {
        return dist;
    }

    public Vector getNormal() {
        return normal;
    }

}
