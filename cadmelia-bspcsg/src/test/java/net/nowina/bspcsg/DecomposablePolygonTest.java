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
import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.solid.bspcsg.FactoryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class DecomposablePolygonTest {

    Factory factory = new FactoryBuilder().usingDecomposablePolygon(true).build();

    DecomposablePolygon sq, sq1, sq2, sq3, sq4, sq11, sq12, sq13, sq14, sq21, sq22, sq23, sq24;

    PolygonList polygons;

    @Before
    public void setup() {

        VectorList list = factory.netVectorList(new Vector(0, 0), new Vector(2, 0), new Vector(2, 2), new Vector(0, 2));

        sq = (DecomposablePolygon) factory.newPolygon(list);

        VectorList l1 = factory.netVectorList(new Vector(0, 0), new Vector(1, 0), new Vector(1, 1), new Vector(0, 1));
        sq1 = (DecomposablePolygon) factory.newChildPolygon(l1, sq);

        VectorList l2 = factory.netVectorList(new Vector(1, 0), new Vector(2, 0), new Vector(2, 1), new Vector(0, 1));
        sq2 = (DecomposablePolygon) factory.newChildPolygon(l2, sq);

        VectorList l3 = factory.netVectorList(new Vector(1, 1), new Vector(2, 1), new Vector(2, 2), new Vector(1, 2));
        sq3 = (DecomposablePolygon) factory.newChildPolygon(l3, sq);

        VectorList l4 = factory.netVectorList(new Vector(0, 1), new Vector(1, 1), new Vector(1, 2), new Vector(0, 2));
        sq4 = (DecomposablePolygon) factory.newChildPolygon(l4, sq);

        VectorList l11 = factory.netVectorList(new Vector(0, 0), new Vector(.5, 0), new Vector(.5, .5), new Vector(0, .5));
        sq11 = (DecomposablePolygon) factory.newChildPolygon(l11, sq1);

        VectorList l12 = factory.netVectorList(new Vector(.5, 0), new Vector(1, 0), new Vector(1, .5), new Vector(0, .5));
        sq12 = (DecomposablePolygon) factory.newChildPolygon(l12, sq1);

        VectorList l13 = factory.netVectorList(new Vector(.5, .5), new Vector(1, .5), new Vector(1, 1), new Vector(.5, 1));
        sq13 = (DecomposablePolygon) factory.newChildPolygon(l13, sq1);

        VectorList l14 = factory.netVectorList(new Vector(0, .5), new Vector(.5, .5), new Vector(.5, 1), new Vector(0, 1));
        sq14 = (DecomposablePolygon) factory.newChildPolygon(l14, sq1);

        VectorList l21 = factory.netVectorList(new Vector(1, 0), new Vector(1.5, 0), new Vector(1.5, .5), new Vector(1, .5));
        sq21 = (DecomposablePolygon) factory.newChildPolygon(l21, sq2);

        VectorList l22 = factory.netVectorList(new Vector(1.5, 0), new Vector(2, 0), new Vector(2, .5), new Vector(1, .5));
        sq22 = (DecomposablePolygon) factory.newChildPolygon(l22, sq2);

        VectorList l23 = factory.netVectorList(new Vector(1.5, .5), new Vector(2, .5), new Vector(2, 1), new Vector(1.5, 1));
        sq23 = (DecomposablePolygon) factory.newChildPolygon(l23, sq2);

        VectorList l24 = factory.netVectorList(new Vector(1, .5), new Vector(1.5, .5), new Vector(1.5, 1), new Vector(1, 1));
        sq24 = (DecomposablePolygon) factory.newChildPolygon(l24, sq2);

        polygons = factory.newPolygonList();
        for (Polygon p : Arrays.asList(sq21, sq22, sq23, sq3, sq4, sq11, sq12, sq13, sq14, sq24)) {
            polygons.addPolygon(p);
        }
    }

    @Test
    public void test1() throws Exception {

        Assert.assertEquals(4, sq.getChildren().size());
        Assert.assertNull(sq.getParent());
        Assert.assertEquals(4, sq1.getChildren().size());
        Assert.assertTrue(sq == sq1.getParent());
        Assert.assertEquals(4, sq2.getChildren().size());
        Assert.assertTrue(sq == sq2.getParent());
        Assert.assertEquals(0, sq3.getChildren().size());
        Assert.assertTrue(sq == sq3.getParent());
        Assert.assertEquals(0, sq4.getChildren().size());
        Assert.assertTrue(sq == sq4.getParent());
        Assert.assertEquals(0, sq11.getChildren().size());
        Assert.assertTrue(sq1 == sq11.getParent());
        Assert.assertEquals(0, sq12.getChildren().size());
        Assert.assertTrue(sq1 == sq12.getParent());
        Assert.assertEquals(0, sq13.getChildren().size());
        Assert.assertTrue(sq1 == sq13.getParent());
        Assert.assertEquals(0, sq14.getChildren().size());
        Assert.assertTrue(sq1 == sq14.getParent());

    }

    @Test
    public void test2() throws Exception {

        Assert.assertTrue(sq == sq11.getBiggestComplete());
        Assert.assertTrue(sq == sq12.getBiggestComplete());
        Assert.assertTrue(sq == sq13.getBiggestComplete());
        Assert.assertTrue(sq == sq14.getBiggestComplete());
        Assert.assertTrue(sq == sq1.getBiggestComplete());
        Assert.assertTrue(sq == sq2.getBiggestComplete());
        Assert.assertTrue(sq == sq3.getBiggestComplete());
        Assert.assertTrue(sq == sq4.getBiggestComplete());
        Assert.assertTrue(sq == sq.getBiggestComplete());

        Assert.assertEquals(10, polygons.size());
        PolygonList filtered = new GetAllPolygonsRecurviseExecutor(factory).recomposePolygon(polygons);
        Assert.assertEquals(1, filtered.size());
    }

    @Test
    public void test3() throws Exception {

        // polygons.remove(sq11);
        sq11.removeFromParent();

        Assert.assertTrue(sq12 == sq12.getBiggestComplete());
        Assert.assertTrue(sq13 == sq13.getBiggestComplete());
        Assert.assertTrue(sq14 == sq14.getBiggestComplete());

        Assert.assertTrue(sq2 == sq2.getBiggestComplete());
        Assert.assertTrue(sq3 == sq3.getBiggestComplete());
        Assert.assertTrue(sq4 == sq4.getBiggestComplete());

        Assert.assertEquals(10, polygons.size());
        PolygonList filtered = new GetAllPolygonsRecurviseExecutor(factory).recomposePolygon(polygons);
        Assert.assertEquals(7, filtered.size());

    }

    @Test
    public void test4() throws Exception {

        // polygons.remove(sq11);
        sq21.removeFromParent();

        Assert.assertTrue(sq1 == sq11.getBiggestComplete());
        Assert.assertTrue(sq1 == sq12.getBiggestComplete());
        Assert.assertTrue(sq1 == sq13.getBiggestComplete());
        Assert.assertTrue(sq1 == sq14.getBiggestComplete());

        Assert.assertTrue(sq2 == sq2.getBiggestComplete());
        Assert.assertTrue(sq21 == sq21.getBiggestComplete());

        Assert.assertTrue(sq3 == sq3.getBiggestComplete());
        Assert.assertTrue(sq4 == sq4.getBiggestComplete());

        Assert.assertEquals(10, polygons.size());
        PolygonList filtered = new GetAllPolygonsRecurviseExecutor(factory).recomposePolygon(polygons);
        Assert.assertEquals(7, filtered.size());

    }

}
