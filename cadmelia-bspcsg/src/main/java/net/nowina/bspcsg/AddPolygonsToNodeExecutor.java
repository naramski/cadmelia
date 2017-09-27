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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AddPolygonsToNodeExecutor extends BSPTreeOperationExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddPolygonsToNodeExecutor.class);

    private Factory factory;

    private final boolean discardingInvalidPolygon;

    public AddPolygonsToNodeExecutor(Factory factory) {
        this.factory = factory;
        this.discardingInvalidPolygon = factory.isDiscardingInvalidPolygon();
    }

    public abstract void execute(PolygonList polygonsToAdd, Node node);

    protected void addPolygonToNode(Polygon polygon, Node node) {

        if (polygon == null) {
            throw new NullPointerException();
        }

        /*
        if(discardingInvalidPolygon) {
            if(!polygon.isValid()) {
                return;
            }
        }*/

        if(node.getPlane() == null) {
            node.addFirstPolygon(polygon);
            return;
        }

        List<Integer> vertexTypes = new ArrayList<>();
        int polygonType = polygonType(vertexTypes, polygon.getVertices(), node);

        switch (polygonType) {
            case Node.COPLANAR:
                LOGGER.debug("Polygon is co-planar");
                addCoplanarPolygon(polygon, node);
                break;
            case Node.FRONT:
                LOGGER.debug("Polygon is front");
                addFrontPolygon(polygon, node);
                break;
            case Node.BACK:
                LOGGER.debug("Polygon is back");
                addBackPolygon(polygon, node);
                break;
            case Node.SPANNING:
                LOGGER.debug("Polygon is spanning");
                splitAndAddPolygon(polygon, vertexTypes, node);
                break;
        }

    }

    protected void addCoplanarPolygon(Polygon polygon, Node node) {
        if (node.polygons == null) {
            node.polygons = factory.newPolygonList();
        }
        node.polygons.addPolygon(polygon);
    }

    protected void splitAndAddPolygon(Polygon polygon, List<Integer> vertexPosition, Node node) {

        VectorList frontVertices = factory.netVectorList();
        VectorList backVertices = factory.netVectorList();

        for (VectorListBrowser browser = polygon.getVertices().browse(); browser.hasNext(); browser.next()) {

            int i = browser.getIndex();
            int j = browser.getNextIndex();

            /* Type for point can only be FRONT, BACK or COPLANAR */
            int ti = vertexPosition.get(i);
            int tj = vertexPosition.get(j);

            if (ti != Node.BACK) {
                /* Front or coplanar */
                frontVertices.addVector(browser);
            }

            if (ti != Node.FRONT) {
                /* Back or coplanar */
                backVertices.addVector(browser);
            }

            /* Only true when the two points are on both sides of the Plane. They need to be cut */
            if ((ti | tj) == Node.SPANNING) {
                /* (ti FRONT and tj BACK) or (ti BACK and tj FRONT) */

                /* Distance between vi and the plane, projected on the normal of the plane */
                double distI = node.getPlane().getDist() - node.getPlane().getNormal().dot(browser.x(), browser.y(), browser.z());

                /* Vector between IJ */
                Vector vj = browser.getNext();
                double vectorIJx = vj.x() - browser.x();
                double vectorIJy = vj.y() - browser.y();
                double vectorIJz = vj.z() - browser.z();
                double distIJ = node.getPlane().getNormal().dot(vectorIJx, vectorIJy, vectorIJz);

                double t = distI / distIJ;

                /* New vertex on the plane, added on the two other polygon */
                /* Vector v = vi.interpolate(vj, t); */

                double vx = browser.x() + (vj.x() - browser.x()) * t;
                double vy = browser.y() + (vj.y() - browser.y()) * t;
                double vz = browser.z() + (vj.z() - browser.z()) * t;
                Vector v = new Vector(vx, vy, vz);

                frontVertices.addVector(v);
                backVertices.addVector(v);

            }

        }

        if (frontVertices.size() >= 3) {
            addFrontPolygon(factory.newChildPolygon(frontVertices, polygon), node);
        }

        if (backVertices.size() >= 3) {
            addBackPolygon(factory.newChildPolygon(backVertices, polygon), node);
        }

    }

    protected abstract void addFrontPolygon(Polygon polygon, Node node);

    protected abstract void addBackPolygon(Polygon polygon, Node node);

    protected Factory getFactory() {
        return factory;
    }

}