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

import net.nowina.bspcsg.collection.PolygonList;
import net.nowina.bspcsg.collection.VectorList;
import net.nowina.bspcsg.collection.VectorListBrowser;
import net.nowina.cadmelia.construction.Vector;

import java.util.ArrayList;
import java.util.List;

public class ClippingOperationExecutor extends BSPTreeOperationExecutor {

    private Factory factory;

    private final boolean discardingInvalidPolygon;

    public ClippingOperationExecutor(Factory factory) {
        this.factory = factory;
        this.discardingInvalidPolygon = factory.isDiscardingInvalidPolygon();
    }

    protected void splitPolygon(Polygon polygon, PolygonList front, PolygonList back, Node thisNode) {

        VectorList vertices = polygon.getVertices();
        List<Integer> vertexPosition = new ArrayList<>(vertices.size());
        int polygonType = polygonType(vertexPosition, vertices, thisNode);

        switch (polygonType) {
            case Node.COPLANAR:
                double orientation = thisNode.getPlane().getNormal().dot(polygon.getNormal());
                if (orientation > 0) {
                    front.addPolygon(polygon);
                } else {
                    back.addPolygon(polygon);
                }
                break;
            case Node.FRONT:
                front.addPolygon(polygon);
                break;
            case Node.BACK:
                back.addPolygon(polygon);
                break;
            case Node.SPANNING:
                cutPolygon(polygon, front, back, vertexPosition, thisNode);
                break;
        }
    }

    protected void cutPolygon(Polygon polygon, PolygonList front, PolygonList back, List<Integer> vertexPosition, Node thisNode) {

        VectorList f = factory.netVectorList();
        VectorList b = factory.netVectorList();

        for (VectorListBrowser browser = polygon.getVertices().browse(); browser.hasNext(); browser.next()) {

            int i = browser.getIndex();
            int j = browser.getNextIndex();

            /* Type for point can only be FRONT, BACK or COPLANAR */
            int ti = vertexPosition.get(i);
            int tj = vertexPosition.get(j);

            if (ti != Node.BACK) {
                /* Front or coplanar */
                f.addVector(browser);
            }

            if (ti != Node.FRONT) {
                /* Back or coplanar */
                b.addVector(browser);
            }

            /* Only true when the two points are on both sides of the Plane. They need to be cut */
            if ((ti | tj) == Node.SPANNING) {
                /* (ti FRONT and tj BACK) or (ti BACK and tj FRONT) */

                /* Distance between vi and the plane, projected on the normal of the plane */
                double distI = thisNode.getPlane().getDist() - thisNode.getPlane().getNormal().dot(browser.x(), browser.y(), browser.z());

                /* Vector between IJ */
                Vector vj = browser.getNext();
                double vectorIJx = vj.x() - browser.x();
                double vectorIJy = vj.y() - browser.y();
                double vectorIJz = vj.z() - browser.z();
                double distIJ = thisNode.getPlane().getNormal().dot(vectorIJx, vectorIJy, vectorIJz);

                double t = distI / distIJ;

                /* New vertex on the plane, added on the two other polygon */
                double vx = browser.x() + (vj.x() - browser.x()) * t;
                double vy = browser.y() + (vj.y() - browser.y()) * t;
                double vz = browser.z() + (vj.z() - browser.z()) * t;
                Vector v = new Vector(vx, vy, vz);

                f.addVector(v);
                b.addVector(v);

            }

        }

        if (f.size() >= 3) {
            Vector normal = Polygon.buildNormal(f);
            if(discardingInvalidPolygon) {
                if (Polygon.isValid(normal)) {
                    Polygon p = factory.newChildPolygon(f, polygon);
                    front.addPolygon(p);
                }
            } else {
                Polygon p = factory.newChildPolygon(f, polygon);
                front.addPolygon(p);
            }
        }

        if (b.size() >= 3) {
            Vector normal = Polygon.buildNormal(b);
            if(discardingInvalidPolygon) {
                if (Polygon.isValid(normal)) {
                    Polygon p = factory.newChildPolygon(b, polygon);
                    back.addPolygon(p);
                }
            } else {
                Polygon p = factory.newChildPolygon(b, polygon);
                back.addPolygon(p);
            }
        }

    }

}
