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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NodeTaskExecutor {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

    private Factory factory;

    public NodeTaskExecutor(Factory factory) {
        if (factory == null) {
            throw new NullPointerException();
        }
        this.factory = factory;
    }

    public Future<Node> buildNode(CSG csg) {
        return EXECUTOR_SERVICE.submit(new NodeBuildTask(factory, csg));
    }

    public Future<Node> clip(Node thisNode, Node otherNode) {
        return EXECUTOR_SERVICE.submit(new NodeClipTask(thisNode, otherNode));
    }

    static class NodeBuildTask implements Callable<Node> {

        private Factory builder;

        private CSG csg;

        public NodeBuildTask(Factory builder, CSG csg) {
            if (builder == null) {
                throw new NullPointerException();
            }
            this.builder = builder;

            if (csg.getPolygons().isEmpty()) {
                throw new IllegalArgumentException("Polygon list is empty");
            }
            this.csg = csg;
        }

        @Override
        public Node call() throws Exception {
            if (csg.cachedNode != null) {
                return csg.cachedNode;
            } else {
                Node node = builder.newNode(csg.getPolygons());
                csg.cachedNode = node;
                return node;
            }
        }

    }

    static class NodeClipTask implements Callable<Node> {

        private Node node;

        private Node other;

        public NodeClipTask(Node node, Node other) {
            this.node = node;
            this.other = other;
        }

        @Override
        public Node call() throws Exception {
            node.clipTo(other);
            return node;
        }

    }

}
