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
import net.nowina.bspcsg.collection.PolygonListBrowser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ClipPolygonsToNodeIterativeExecutor extends ClipPolygonsToNodeExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClipPolygonsToNodeIterativeExecutor.class);

    private Factory factory;

    public ClipPolygonsToNodeIterativeExecutor(Factory factory) {
        super(factory);
        this.factory = factory;
    }

    @Override
    public PolygonList execute(Node root, PolygonList polygonList) {

        final Map<Node, PolygonList> data = new HashMap<>();
        data.put(root, polygonList);

        preOrderTraversal(root, node -> {

            if (node.plane == null) {
                throw new IllegalStateException("Node should not be used before creating the first Plane");
            }

            PolygonList polygons = data.get(node);
            PolygonList frontP = factory.newPolygonList();
            PolygonList backP = factory.newPolygonList();

            for (PolygonListBrowser browser = polygons.browse(); browser.hasNext(); browser.next()) {
                Polygon polygon = browser.get();
                splitPolygon(polygon, frontP, backP, node);
            }
            LOGGER.debug("Split " + polygons.size() + " polygons with node " + node.id + " in " + frontP.size() + " polygons in front - " + backP.size() + " polygons in back");

            if (node.getFront() != null) {
                data.put(node.getFront(), frontP);
            } else {
                data.put(node, frontP);
            }

            if (node.getBack() != null) {
                data.put(node.getBack(), backP);
            } else {
                /* This is where polygon behind are removed. If back == null, only the front polygons are stored. That means
                 * that all the polygons from the backP are discared */
                if (factory.isUsingDecomposablePolygon()) {
                    for (PolygonListBrowser browser = backP.browse(); browser.hasNext(); browser.next()) {
                        DecomposablePolygon d = (DecomposablePolygon) browser.get();
                            /* If this polygon is a child of another polygon we must remove this polygon from the parent */
                        d.removeFromParent();
                    }
                }
            }

        });

        postOrderTraversal(root, node -> {

            PolygonList frontP = null;
            PolygonList backP = null;

            if (node.getFront() != null) {
                frontP = data.get(node.getFront());
            } else {
                frontP = data.get(node);
            }

            if (node.getBack() != null) {
                backP = data.get(node.getBack());
            }

            if (node.getBack() != null) {
                frontP.addAll(backP);
                data.put(node, frontP);

            } else {

                data.put(node, frontP);

            }

        });

        PolygonList result = data.get(root);
        return result;

    }

}
