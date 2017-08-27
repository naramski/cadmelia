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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClipNodeToNodeIterativeExecutor extends ClipNodeToNodeExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClipPolygonsToNodeIterativeExecutor.class);

    public ClipNodeToNodeIterativeExecutor(Factory factory) {
        super(factory);
    }

    @Override
    public void execute(Node root, Node to) {

        NodeOperation operation = new NodeOperation() {
            @Override
            public void executeOperation(Node node) {
                LOGGER.debug("CLIP " + node.id + " TO " + to.id + "/ request clipping of " + node.polygons.size() + " #polygons");
                node.polygons = to.clipPolygons(node.polygons);
            }
        };

        preOrderTraversal(root, operation);

    }

}