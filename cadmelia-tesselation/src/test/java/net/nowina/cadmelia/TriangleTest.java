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
package net.nowina.cadmelia;

import net.nowina.cadmelia.construction.Vector;
import org.junit.Assert;
import org.junit.Test;

public class TriangleTest {

    @Test
    public void testAdjacent() {
        Vector x = new Vector(1, 1, 1);
        Vector y = new Vector(3, 4, 5);
        Vector z = new Vector(0, 0, 0);
        Vector z2 = new Vector(2, 2, 2);
        Vector y2 = new Vector(2, 1, 2);

        Assert.assertTrue(new Triangle(x, y, z).touchEdge(new Triangle(y, z, x)));
        Assert.assertTrue(new Triangle(x, y, z).touchEdge(new Triangle(z, x, y)));
        Assert.assertTrue(new Triangle(x, y, z).touchEdge(new Triangle(z, y, x)));
        Assert.assertTrue(new Triangle(x, y, z).touchEdge(new Triangle(x, y, z2)));
        Assert.assertTrue(new Triangle(x, y, z).touchEdge(new Triangle(z2, y, z)));
        Assert.assertTrue(new Triangle(x, y, z).touchEdge(new Triangle(x, z2, z)));
        Assert.assertFalse(new Triangle(x, y, z).touchEdge(new Triangle(x, y2, z2)));
        Assert.assertFalse(new Triangle(x, y, z).touchEdge(new Triangle(y2, x, z2)));
        Assert.assertFalse(new Triangle(x, y, z).touchEdge(new Triangle(y2, z2, x)));
        Assert.assertFalse(new Triangle(x, y, z).touchEdge(new Triangle(z2, y2, x)));
    }

}
