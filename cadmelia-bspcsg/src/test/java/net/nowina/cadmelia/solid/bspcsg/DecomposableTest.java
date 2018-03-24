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
package net.nowina.cadmelia.solid.bspcsg;

import net.nowina.cadmelia.construction.Construction;
import net.nowina.cadmelia.construction.Solid;
import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.shape.impl.ShapeImplFactory;
import net.nowina.cadmelia.stl.STLWriter;
import org.junit.Test;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class DecomposableTest {

    @Test
    public void testWithDecomposable() throws IOException {

        CSGSolidFactory factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(true).build();

        Construction sphere = factory.sphere(25, 10, 5);

        Solid cyl1 = (Solid) factory.cylinder(13, 13, 100, 10, true).rotate(90, 0, 0);

        Solid logo = (Solid) sphere.difference(cyl1);

        try(PrintWriter w = new PrintWriter(new FileOutputStream("target/logo-decomposed-1.stl"))) {
            new STLWriter().write(logo, w);
        }

        Solid cyl2 = factory.cylinder(13, 13, 100, 10, true);

        logo = (Solid) logo.difference(cyl2);

        try(PrintWriter w = new PrintWriter(new FileOutputStream("target/logo-decomposed-2.stl"))) {
            new STLWriter().write(logo, w);
        }

        Solid cyl3 = (Solid) factory.cylinder(13, 13, 100, 10, true).rotate(0, 90, 0);

        logo = (Solid) logo.difference(cyl3);

        try(PrintWriter w = new PrintWriter(new FileOutputStream("target/logo-decomposed-3.stl"))) {
            new STLWriter().write(logo, w);
        }

        logo = (Solid) logo.intersection(factory.cube(35, 35, 35, true)).union(factory.sphere(10, 30, 15));

        try(PrintWriter w = new PrintWriter(new FileOutputStream("target/logo-decomposed-4.stl"))) {
            new STLWriter().write(logo, w);
        }

    }

    @Test
    public void testWithoutDecomposable() throws IOException {

        CSGSolidFactory factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(false).build();

        Construction sphere = factory.sphere(25, 10, 5);

        Solid cyl1 = (Solid) factory.cylinder(13, 13, 100, 10, true).rotate(90, 0, 0);

        Solid logo = (Solid) sphere.difference(cyl1);

        try(PrintWriter w = new PrintWriter(new FileOutputStream("target/logo-decomposed-normal-1.stl"))) {
            new STLWriter().write(logo, w);
        }

        Solid cyl2 = factory.cylinder(13, 13, 100, 10, true);

        logo = (Solid) logo.difference(cyl2);

        try(PrintWriter w = new PrintWriter(new FileOutputStream("target/logo-decomposed-normal-2.stl"))) {
            new STLWriter().write(logo, w);
        }

        Solid cyl3 = (Solid) factory.cylinder(13, 13, 100, 10, true).rotate(0, 90, 0);

        logo = (Solid) logo.difference(cyl3);

        try(PrintWriter w = new PrintWriter(new FileOutputStream("target/logo-decomposed-normal-3.stl"))) {
            new STLWriter().write(logo, w);
        }

    }

    @Test
    public void testExtrude() throws IOException {

        ShapeImplFactory shapeBuilder = new ShapeImplFactory();
        CSGSolidFactory factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(true).build();

        Construction extrude1 = factory.extrude(shapeBuilder.square(5, 5, true), 5, 1.0);

        Construction extrude2 = factory.extrude(shapeBuilder.circle(1.5, 30), 10, 1.0);

        Construction extrude3 = factory.extrude(shapeBuilder.circle(1.5, 30), 10, 1.0).rotate(90, 0, 0);

        Construction sphere = factory.sphere(3, 16, 8);

        Construction result = extrude1.difference(extrude2).difference(extrude3).intersection(sphere);

        try(PrintWriter w = new PrintWriter(new FileOutputStream("target/extrude.stl"))) {
            new STLWriter().write((Solid) result, w);
        }

    }

    @Test
    public void testErrorDecomposable() throws IOException {

        ShapeImplFactory shapeBuilder = new ShapeImplFactory();
        CSGSolidFactory factory = new FactoryBuilder().usingComposite(false).usingDecomposablePolygon(true).build();


        Construction c1 =
            factory.extrude(shapeBuilder.polygon(Arrays.asList(new Vector(-3.5, 0), new Vector(3.5, 0), new Vector(-1.75, 12))), 2, 1.0);

        Construction c2 =
            factory.extrude(shapeBuilder.polygon(Arrays.asList(new Vector(3.5, 2), new Vector(-1, 2), new Vector(3.5, 5))), 2.5, 1.0).rotate(-90, 0, 0).translate(0, 0, -1.25).rotate(0, 0, -90);

        Construction total = c1.union(c2);

        for(int i=1;i<=2;i++) {

            Construction c3 =
                    factory.extrude(shapeBuilder.polygon(Arrays.asList(new Vector(-3.5, 0), new Vector(3.5, 0), new Vector(-1.75, 12))), 2, 1.0);

            Construction c4 =
                    factory.extrude(shapeBuilder.polygon(Arrays.asList(new Vector(3.5, 2), new Vector(-1, 2), new Vector(3.5, 5))), 2.5, 1.0).rotate(-90, 0, 0).translate(0, 0, -1.25).rotate(0, 0, -90);

            total = total.union(c3.union(c4).rotate(0, 0, 120 * i));

        }

        total = total.difference(factory.cylinder(1.25, 1.25, 6, 16, false));

        try(PrintWriter w = new PrintWriter(new FileOutputStream("target/error-decomposable.stl"))) {
            new STLWriter().write((Solid) total, w);
        }

    }

}
