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
package net.nowina.cadmelia.shape;

import net.nowina.cadmelia.construction.FactoryBuilder;
import net.nowina.cadmelia.construction.ShapeFactory;
import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.shape.impl.ShapeImplFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShapeUtilTest {

    @BeforeClass
    public static void setUp() {
        FactoryBuilder.registerShapeFactory(new ShapeImplFactory());
    }

    @Test
    public void testCW() throws Exception {

        List<Vector> points = Arrays.asList(new Vector(5, 0), new Vector(6, 4),
                new Vector(4, 5), new Vector(1, 5), new Vector(1, 0));

        ShapeFactory builder = FactoryBuilder.getInstance().createShapeFactory();

        Assert.assertTrue(ShapeUtil.isCCW(builder.polygon(points).getPolygons().get(0).getExteriorRing()));

        Collections.reverse(points);

        Assert.assertFalse(ShapeUtil.isCCW(builder.polygon(points).getPolygons().get(0).getExteriorRing()));

        points = Arrays.asList(new Vector(1, 1), new Vector(3, 0), new Vector(-1, 0));
        Assert.assertFalse(ShapeUtil.isCCW(builder.polygon(points).getPolygons().get(0).getExteriorRing()));

    }

}
