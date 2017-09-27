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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public class Node {

    private static final Logger LOGGER = LoggerFactory.getLogger(Node.class);

    private static final AtomicLong INSTANCE_COUNT = new AtomicLong();

    static final int COPLANAR = 0;
    static final int FRONT = 1;
    static final int BACK = 2;
    static final int SPANNING = 3;

    private Factory factory;

    PolygonList polygons;

    private Plane plane;

    Node left;

    Node right;

    boolean inverted = false;

    final long id;

    Node(Factory factory) {
        this.factory = factory;
        id = INSTANCE_COUNT.getAndIncrement();

    }

    public Node(Factory factory, PolygonList polygons) {

        this(factory);

        if (polygons == null) {
            throw new NullPointerException();
        }
        if (polygons.isEmpty()) {
            throw new IllegalArgumentException("List of polygons is empty");
        }

        addAll(polygons);
    }

    public int polygonsCount() {
        if(polygons == null) {
            return 0;
        } else {
            return polygons.size();
        }
    }

    public void addFirstPolygon(Polygon polygon) {
        if(polygons != null || plane != null) {
            throw new IllegalStateException();
        }
        polygons = factory.newPolygonList();
        polygons.addPolygon(polygon);
        plane = new Plane(polygon);
    }

    public Plane getPlane() {
        return plane;
    }

    public void addAll(PolygonList polygons) {
        factory.getAddPolygonsToNodeExecutor().execute(polygons, this);
    }

    /**
     * Return the list of all the polygons contained in this Node and its children
     *
     * @return
     */
    public PolygonList allPolygons() {
        return factory.getGetAllPolygonsExecutor().execute(this);
    }

    /**
     * Converts solid space to empty space and vice verca.
     */
    public void invert() {
        factory.getInvertNodeExecutor().execute(this);
    }

    public void clipTo(Node toNode) {
        LOGGER.debug("Node " + this + " NODE CLIP " + toNode);
        factory.getClipNodeToNodeExecutor().execute(this, toNode);
    }

    public PolygonList clipPolygons(PolygonList polygons) {
        LOGGER.debug("Node " + this + " POLYGON CLIP " + polygons.size() + " #polygons");
        PolygonList result = factory.getClipPolygonsToNodeExecutor().execute(this, polygons);
        LOGGER.debug("Node " + this + " Resulting in " + result.size() + " #polygons");
        return result;
    }

    public Node getFront() {
        if(inverted) {
            return right;
        } else {
            return left;
        }
    }

    public void setFront(Node front) {
        if(inverted) {
            right = front;
        } else {
            this.left = front;
        }
    }

    public Node getBack() {
        if(inverted) {
            return left;
        } else {
            return right;
        }
    }

    public void setBack(Node back) {
        if (inverted) {
            left = back;
        } else {
            this.right = back;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return id == node.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

}
