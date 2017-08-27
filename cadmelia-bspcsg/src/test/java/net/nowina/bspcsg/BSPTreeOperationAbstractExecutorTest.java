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

import net.nowina.cadmelia.solid.bspcsg.FactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BSPTreeOperationAbstractExecutorTest {

    @Test
    public void test() {

        BSPTreeOperationExecutor executor = new BSPTreeOperationExecutor();

        Factory factory = new FactoryBuilder().build();

        Node n1 = new Node(factory);
        n1.left = new Node(factory);
        n1.left.left = new Node(factory);
        n1.left.right = new Node(factory);

        n1.right = new Node(factory);
        n1.right.left = new Node(factory);
        n1.right.right = new Node(factory);
        n1.right.right.right = new Node(factory);
        n1.right.right.right.right = new Node(factory);

        List<Node> list = new ArrayList<>();
        executor.postOrderTraversal(n1, node -> {
            list.add(node);
        });

        Assert.assertEquals(9, list.size());
    }

    /**
     * Test from https://www.youtube.com/watch?v=wGXB9OWhPTg
     */
    @Test
    public void test2() {

        BSPTreeOperationExecutor executor = new BSPTreeOperationExecutor();

        Factory factory = new FactoryBuilder().build();

        Node n10 = new Node(factory);
        Node n5 = n10.left = new Node(factory);
        Node nm2 = n5.left = new Node(factory);
        Node n2 = nm2.right = new Node(factory);
        Node nm1 = n2.left = new Node(factory);
        Node n6 = n5.right = new Node(factory);
        Node n8 = n6.right = new Node(factory);
        Node n30 = n10.right = new Node(factory);
        Node n40 = n30.right = new Node(factory);

        final List<Node> list = new ArrayList<>();
        executor.morrisTraversal(n10, node -> {
            list.add(node);
        });
        Assert.assertEquals(9, list.size());
        Assert.assertArrayEquals(new Object[] { nm2, nm1, n2, n5, n6, n8, n10, n30, n40}, list.toArray());

        list.clear();
        executor.preOrderTraversal(n10, node -> {
            list.add(node);
        });
        Assert.assertEquals(9, list.size());
        System.out.println(list);
        Assert.assertArrayEquals(new Object[] { n10, n30, n40, n5, n6, n8, nm2, n2, nm1}, list.toArray());

        list.clear();
        executor.postOrderTraversal(n10, node -> {
            list.add(node);
        });
        Assert.assertEquals(9, list.size());
        System.out.println(list);
        Assert.assertArrayEquals(new Object[] { nm1, n2, nm2, n8, n6, n5, n40, n30, n10}, list.toArray());

    }

}
