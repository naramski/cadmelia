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

import java.util.List;
import java.util.Stack;

public class BSPTreeOperationExecutor {

    public interface NodeOperation {

        void executeOperation(Node node);

    }

    protected void morrisTraversal(Node root, NodeOperation operation) {

        /* Algorithm is Morris Traversal */
        Node cur = root;

        while (cur != null) {

            if (cur.left == null) {

                operation.executeOperation(cur);
                cur = cur.right;

            } else {

                Node pre = cur.left;
                while (pre.right != null && pre.right != cur) {
                    pre = pre.right;
                }

                if (pre.right == cur) {

                    pre.right = null;
                    operation.executeOperation(cur);
                    cur = cur.right;

                } else {

                    pre.right = cur;
                    cur = cur.left;
                }

            }

        }

    }

    public void preOrderTraversal(Node root, NodeOperation operation) {

        // Base Case
        if (root == null) {
            return;
        }

        // Create an empty stack and push root to it
        Stack<Node> stack = new Stack<>();
        stack.push(root);

        /* Pop all items one by one. Do following for every popped item
         a) print it
         b) push its back child
         c) push its front child
         Note that back child is pushed first so that right is processed first */
        while (!stack.isEmpty()) {

            // Pop the top item from stack and print it
            Node node = stack.peek();

            operation.executeOperation(node);

            stack.pop();

            // Push back and right children of the popped node to stack
            if (node.getFront() != null) {
                stack.push(node.getFront());
            }
            if (node.getBack() != null) {
                stack.push(node.getBack());
            }
        }

    }

    /**
     * This traversal first looks to the front node, then the back node and the then parent node.
     *
     * @param root
     * @param operation
     */
    public void postOrderTraversal(Node root, NodeOperation operation) {
        if (root == null) return;

        Stack<Node> stack = new Stack<>();
        Node current = root;

        while (true) {

            if (current != null) {
                if (current.getBack() != null) {
                    stack.push(current.getBack());
                }
                stack.push(current);
                current = current.getFront();
                continue;
            }

            if (stack.isEmpty()) {
                break;
            }
            current = stack.pop();

            if (current.getBack() != null && !stack.isEmpty() && current.getBack() == stack.peek()) {
                stack.pop();
                stack.push(current);
                current = current.getBack();
            } else {
                operation.executeOperation(current);
                current = null;
            }

        }
    }

    protected int polygonType(List<Integer> types, VectorList vertices, Node node) {

        int polygonType = 0;

        /* We want to now the type with testing the distance from the vertex with the plane
           and seeing if this distance means the vertex is in the plane (with a tolerance of
           EPSILON) or in FRONT or BACK.

            double t = normal.dot(browser.x(), browser.y(), browser.z()) - plane.getDist();
            int type = (t < -Plane.EPSILON) ? BACK : (t > Plane.EPSILON) ? FRONT : COPLANAR;

         */

        double back = node.plane.getDist() - Plane.EPSILON;
        double front = node.plane.getDist() + Plane.EPSILON;
        Vector normal = node.plane.getNormal();
        for (VectorListBrowser browser = vertices.browse(); browser.hasNext(); browser.next()) {
            double t = normal.dot(browser.x(), browser.y(), browser.z());
            int type = (t < back) ? Node.BACK : (t > front) ? Node.FRONT : Node.COPLANAR;
            polygonType |= type;
            types.add(type);
        }

        return polygonType;
    }

}
