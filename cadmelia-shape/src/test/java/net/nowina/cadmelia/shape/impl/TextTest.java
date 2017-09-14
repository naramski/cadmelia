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
package net.nowina.cadmelia.shape.impl;

import javafx.embed.swing.JFXPanel;
import org.fxyz3d.shapes.primitives.helper.LineSegment;
import org.fxyz3d.shapes.primitives.helper.Text3DHelper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class TextTest {

    @BeforeClass
    public static void setup() {
        // this starts the JavaFX platform
        new JFXPanel();
    }

    @Test
    public void testI() throws Exception {

        Text3DHelper helper = new Text3DHelper("I", "Arial", 20);

        // One line segment per letter / point (lower case i is two segments, upper case I is one segment)
        List<LineSegment> segments = helper.getLineSegment();

        Assert.assertEquals(1, segments.size());
        for (LineSegment segment : segments) {

            Assert.assertEquals(0, segment.getHoles().size());

            // points are in segment.getPoints();

        }

    }

    @Test
    public void testMN() throws Exception {

        Text3DHelper helper = new Text3DHelper("MN", "Arial", 20);

        // One line segment per letter / point (lower case i is two segments, upper case I is one segment)
        List<LineSegment> segments = helper.getLineSegment();

        Assert.assertEquals(2, segments.size());
        for (LineSegment segment : segments) {

            Assert.assertEquals(0, segment.getHoles().size());

            // points are in segment.getPoints();

        }

    }

    @Test
    public void testO() throws Exception {

        Text3DHelper helper = new Text3DHelper("O", "Arial", 20);

        // One line segment per letter / point (lower case i is two segments, upper case I is one segment)
        List<LineSegment> segments = helper.getLineSegment();

        Assert.assertEquals(1, segments.size());
        for (LineSegment segment : segments) {

            Assert.assertEquals(1, segment.getHoles().size());

            // points are in segment.getPoints();

        }

    }

    @Test
    public void testOB() throws Exception {

        Text3DHelper helper = new Text3DHelper("OB", "Arial", 20);

        // One line segment per letter / point (lower case i is two segments, upper case I is one segment)
        List<LineSegment> segments = helper.getLineSegment();

        Assert.assertEquals(2, segments.size());

        LineSegment segment = segments.get(0);

        // For a strange reason, the letter B is before O
        Assert.assertEquals(2, segment.getHoles().size());

        segment = segments.get(1);

        Assert.assertEquals(1, segment.getHoles().size());

    }

    @Test
    public void testHelloWorld() throws Exception {

        ShapeImplBuilder builder = new ShapeImplBuilder();
        ShapeImpl shape = (ShapeImpl) builder.text("Hello World !", 12, "Arial");

        Assert.assertEquals(12, shape.getPolygons().size());
        int holes = 0;
        for (int i = 0; i < shape.getPolygons().size(); i++) {
            holes += shape.getPolygons().get(i).getHoles().size();
        }

        Assert.assertEquals(4, holes);

    }

}
