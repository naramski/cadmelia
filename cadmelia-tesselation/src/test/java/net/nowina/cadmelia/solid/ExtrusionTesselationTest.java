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
package net.nowina.cadmelia.solid;

import javafx.embed.swing.JFXPanel;
import net.nowina.cadmelia.TriangleMesh;
import net.nowina.cadmelia.construction.Shape;
import net.nowina.cadmelia.construction.ShapeFactory;
import net.nowina.cadmelia.shape.impl.ShapeImplFactory;
import net.nowina.cadmelia.stl.STLWriter;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.PrintWriter;

public class ExtrusionTesselationTest {

    @BeforeClass
    public static void setupJavaFXPlatform() {
        // starts the JavaFX platform
        new JFXPanel();
    }

    @Test
    public void testHelloWorld() throws Exception {

        ShapeFactory shapeBuilder = new ShapeImplFactory();
        Shape shape = shapeBuilder.text("Hello World !", 12, "Arial");

        ExtrusionTesselation<?> extrusion = new ExtrusionTesselation<>(shape, 3, new MockMeshToSolid());

        try (PrintWriter writer = new PrintWriter(new FileOutputStream("build/hello-world.stl"))) {
            new STLWriter().write(new TriangleMesh(extrusion.getPolygons()), writer);
        }

    }

}
