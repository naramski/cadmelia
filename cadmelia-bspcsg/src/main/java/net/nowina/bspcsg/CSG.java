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

import javafx.geometry.BoundingBox;
import net.nowina.bspcsg.collection.PolygonList;
import net.nowina.bspcsg.collection.PolygonListBrowser;
import net.nowina.bspcsg.collection.VectorListBrowser;
import net.nowina.cadmelia.Transformation;
import net.nowina.cadmelia.construction.Vector;

import java.util.concurrent.Future;

public class CSG {

    private Factory factory;

    private NodeTaskExecutor nodeTaskExecutor;

    private final PolygonList polygons;

    Node cachedNode;

    CSG(Factory factory, PolygonList polygons, Node node) {
        this.factory = factory;
        this.polygons = polygons;
        if(!factory.isUsingDecomposablePolygon()) {
            /* The Node cannot be cached when using DecomposablePolygons */
            this.cachedNode = node;
        }
        this.nodeTaskExecutor = new NodeTaskExecutor(factory);
    }

    public CSG copy() {
        return new CSG(factory, polygons.copy(), cachedNode);
    }

    private Node getNode() {
        return factory.newNode(polygons.copy());
    }

    public PolygonList getPolygons() {
        return polygons;
    }

    public CSG union(CSG csg) {

        if (getPolygons().isEmpty()) {
            return csg;
        }

        if (csg.getPolygons().isEmpty()) {
            return this;
        }

        Node a = getNode();
        Node b = csg.getNode();

        a.clipTo(b);
        b.clipTo(a);

        b.invert();
        b.clipTo(a);
        b.invert();
        a.addAll(b.allPolygons());

        return factory.newCSG(a);

    }

    public CSG difference(CSG csg) {

        if (getPolygons().isEmpty()) {
            return this;
        }

        if (csg.getPolygons().isEmpty()) {
            return this;
        }

        Node a = getNode();
        Node b = csg.getNode();

        a.invert();

        a.clipTo(b);
        b.clipTo(a);

        b.invert();
        b.clipTo(a);
        b.invert();
        a.addAll(b.allPolygons());
        a.invert();

        return factory.newCSG(a);

    }

    public CSG intersect(CSG csg) {

        if (getPolygons().isEmpty()) {
            return this;
        }

        if (csg.getPolygons().isEmpty()) {
            return this;
        }

        Node a = getNode();
        Node b = csg.getNode();

        a.invert();
        b.clipTo(a);
        b.invert();

        a.clipTo(b);
        b.clipTo(a);

        a.addAll(b.allPolygons());
        a.invert();

        return factory.newCSG(a);

    }

    public CSG transformed(Transformation transform) {

        if (polygons.isEmpty()) {
            return copy();
        }

        PolygonList newPolygons = factory.newPolygonList();
        for (PolygonListBrowser polygons = getPolygons().browse(); polygons.hasNext(); polygons.next()) {
            Polygon p = polygons.get();
            newPolygons.addPolygon(p.transformed(transform));
        }

        return factory.newCSG(newPolygons);
    }

    public BoundingBox getBounds() {

        if (polygons.isEmpty()) {
            return new BoundingBox(0, 0, 0, 0, 0, 0);
        }

        Vector initial = polygons.getPolygon(0).getVertices().getVector(0);

        double minX = initial.x();
        double minY = initial.y();
        double minZ = initial.z();

        double maxX = initial.x();
        double maxY = initial.y();
        double maxZ = initial.z();

        PolygonList polygons = getPolygons();
        for (PolygonListBrowser p = polygons.browse(); p.hasNext(); p.next()) {

            for (VectorListBrowser v = p.browseVertices(); v.hasNext(); v.next()) {

                minX = Math.min(minX, v.x());
                minY = Math.min(minY, v.y());
                minZ = Math.min(minZ, v.z());
                maxX = Math.max(maxX, v.x());
                maxY = Math.max(maxY, v.y());
                maxZ = Math.max(maxZ, v.z());

            }

        }

        return new BoundingBox(minX, minY, minZ, maxX - minX, maxY - minY, maxZ - minZ);
    }

    protected Factory getFactory() {
        return factory;
    }

}
