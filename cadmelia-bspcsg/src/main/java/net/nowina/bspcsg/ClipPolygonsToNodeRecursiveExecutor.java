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
import net.nowina.bspcsg.collection.VectorList;
import net.nowina.bspcsg.collection.VectorListBrowser;
import net.nowina.cadmelia.construction.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ClipPolygonsToNodeRecursiveExecutor extends ClipPolygonsToNodeExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClipPolygonsToNodeRecursiveExecutor.class);

    private Factory factory;

    public ClipPolygonsToNodeRecursiveExecutor(Factory factory) {
        super(factory);
        this.factory = factory;
    }

    @Override
    public PolygonList execute(Node node, PolygonList polygons) {

        if (node.plane == null) {
            throw new IllegalStateException("Node should not be used before creating the first Plane");
        }

        PolygonList frontP = factory.newPolygonList();
        PolygonList backP = factory.newPolygonList();

        for (PolygonListBrowser browser = polygons.browse(); browser.hasNext(); browser.next()) {
            Polygon polygon = browser.get();
            splitPolygon(polygon, frontP, backP, node);
        }
        LOGGER.debug("Split " + polygons.size() + " polygons with node " + node.id + " in " + frontP.size() + " polygons in front - " + backP.size() + " polygons in back");

        if (node.getFront() != null) {
            frontP = execute(node.getFront(), frontP);
        }

        if (node.getBack() != null) {
            backP = execute(node.getBack(), backP);
        }

        if (node.getBack() != null) {
            frontP.addAll(backP);
            return frontP;
        } else {
            /* This is where polygon behind are removed. If back == null, only the front node are stored. That means
             * that all the polygons from the backP are discared */
            if (factory.isUsingDecomposablePolygon()) {
                for (PolygonListBrowser browser = backP.browse(); browser.hasNext(); browser.next()) {
                    DecomposablePolygon d = (DecomposablePolygon) browser.get();
                    /* If this polygon is a child of another polygon we must remove this polygon from the parent */
                    d.removeFromParent();
                }
            }
            return frontP;
        }

    }

}
