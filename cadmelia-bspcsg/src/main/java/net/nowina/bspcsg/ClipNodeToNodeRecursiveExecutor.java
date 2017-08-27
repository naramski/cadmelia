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

public class ClipNodeToNodeRecursiveExecutor extends ClipNodeToNodeExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClipNodeToNodeRecursiveExecutor.class);

    public ClipNodeToNodeRecursiveExecutor(Factory factory) {
        super(factory);
    }

    @Override
    public void execute(Node node, Node to) {

        LOGGER.debug("CLIP " + node.id + " TO " + to.id + "/ request clipping of " + node.polygons.size() + " #polygons");
        node.polygons = to.clipPolygons(node.polygons);

        if (node.getFront() != null) {
            execute(node.getFront(), to);
        }
        if (node.getBack() != null) {
            execute(node.getBack(), to);
        }

    }

}
