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
import net.nowina.bspcsg.collection.PolygonListBrowser;
import net.nowina.cadmelia.Triangle;
import net.nowina.cadmelia.TriangleMesh;
import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.solid.bspcsg.CSGSolid;
import net.nowina.cadmelia.solid.bspcsg.CSGSolidBuilder;
import net.nowina.cadmelia.solid.bspcsg.Cube;
import net.nowina.cadmelia.solid.bspcsg.FactoryBuilder;
import net.nowina.cadmelia.stl.STLWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class NodeTest {

    @Test
    public void testRecursion() throws Exception {

        CSGSolidBuilder builder = new FactoryBuilder().withoutRecursion(false).usingComposite(false).usingDecomposablePolygon(false).build();

        CSGSolid cube1 = (CSGSolid) new Cube(builder, 2, 2, 2, true).buildSolid().translate(new Vector(1, 1, 1));
        CSGSolid cube2 = (CSGSolid) new Cube(builder, 2, 2, 2, true).buildSolid().translate(new Vector(2, 2, 2));

        Node node1 = new Node(builder, cube1.getCSG().getPolygons());

        PolygonList polygons1 = node1.allPolygons();
        Assert.assertEquals(12, polygons1.size());

        Node node2 = new Node(builder, cube2.getCSG().getPolygons());

        PolygonList polygons2 = node2.allPolygons();
        Assert.assertEquals(12, polygons1.size());

        node1.clipTo(node2);

        polygons1 = node1.allPolygons();
        Assert.assertEquals(21, polygons1.size());

        List<Triangle> triangles = new ArrayList<>();
        PolygonList node1Polygons = node1.allPolygons();

        for (PolygonListBrowser node1Browser = node1Polygons.browse(); node1Browser.hasNext(); node1Browser.next()) {
            Polygon p = node1Browser.get();
            triangles.addAll(p.toTriangle());
        }

        TriangleMesh mesh = new TriangleMesh(triangles);

        try (PrintWriter out = new PrintWriter(new FileOutputStream("build/node.stl"))) {
            new STLWriter().write(mesh, out);
        }

    }

    @Test
    public void testIteration() throws Exception {

        CSGSolidBuilder builder = new FactoryBuilder().withoutRecursion(true).usingComposite(false).usingDecomposablePolygon(false).build();

        CSGSolid cube1 = (CSGSolid) new Cube(builder, 2, 2, 2, true).buildSolid().translate(new Vector(1, 1, 1));
        CSGSolid cube2 = (CSGSolid) new Cube(builder, 2, 2, 2, true).buildSolid().translate(new Vector(2, 2, 2));

        Node node1 = new Node(builder, cube1.getCSG().getPolygons());

        PolygonList polygons1 = node1.allPolygons();
        Assert.assertEquals(12, polygons1.size());

        Node node2 = new Node(builder, cube2.getCSG().getPolygons());

        PolygonList polygons2 = node2.allPolygons();
        Assert.assertEquals(12, polygons1.size());

        node1.clipTo(node2);

        polygons1 = node1.allPolygons();
        Assert.assertEquals(21, polygons1.size());

        List<Triangle> triangles = new ArrayList<>();
        PolygonList node1Polygons = node1.allPolygons();

        for (PolygonListBrowser node1Browser = node1Polygons.browse(); node1Browser.hasNext(); node1Browser.next()) {
            Polygon p = node1Browser.get();
            triangles.addAll(p.toTriangle());
        }

        TriangleMesh mesh = new TriangleMesh(triangles);

        try (PrintWriter out = new PrintWriter(new FileOutputStream("build/node-iteration.stl"))) {
            new STLWriter().write(mesh, out);
        }

    }

    @Test
    public void testRecursionRecomposablePolygons() throws Exception {

        CSGSolidBuilder builder = new FactoryBuilder().withoutRecursion(false).usingComposite(false).usingDecomposablePolygon(true).build();

        CSGSolid cube1 = (CSGSolid) new Cube(builder, 2, 2, 2, true).buildSolid().translate(new Vector(1, 1, 1));
        CSGSolid cube2 = (CSGSolid) new Cube(builder, 2, 2, 2, true).buildSolid().translate(new Vector(2, 2, 2));

        Node node1 = new Node(builder, cube1.getCSG().getPolygons());

        PolygonList polygons1 = node1.allPolygons();
        Assert.assertEquals(12, polygons1.size());

        Node node2 = new Node(builder, cube2.getCSG().getPolygons());

        PolygonList polygons2 = node2.allPolygons();
        Assert.assertEquals(12, polygons1.size());

        node1.clipTo(node2);

        polygons1 = node1.allPolygons();
        Assert.assertEquals(15, polygons1.size());

        List<Triangle> triangles = new ArrayList<>();
        PolygonList node1Polygons = node1.allPolygons();

        for (PolygonListBrowser node1Browser = node1Polygons.browse(); node1Browser.hasNext(); node1Browser.next()) {
            Polygon p = node1Browser.get();
            triangles.addAll(p.toTriangle());
        }

        TriangleMesh mesh = new TriangleMesh(triangles);

        try (PrintWriter out = new PrintWriter(new FileOutputStream("build/node-recomposed.stl"))) {
            new STLWriter().write(mesh, out);
        }

    }

    @Test
    public void testIterationRecomposablePolygons() throws Exception {

        CSGSolidBuilder builder = new FactoryBuilder().withoutRecursion(true).usingComposite(false).usingDecomposablePolygon(true).build();

        CSGSolid cube1 = (CSGSolid) new Cube(builder, 2, 2, 2, true).buildSolid().translate(new Vector(1, 1, 1));
        CSGSolid cube2 = (CSGSolid) new Cube(builder, 2, 2, 2, true).buildSolid().translate(new Vector(2, 2, 2));

        Node node1 = new Node(builder, cube1.getCSG().getPolygons());

        PolygonList polygons1 = node1.allPolygons();
        Assert.assertEquals(12, polygons1.size());

        Node node2 = new Node(builder, cube2.getCSG().getPolygons());

        PolygonList polygons2 = node2.allPolygons();
        Assert.assertEquals(12, polygons1.size());

        node1.clipTo(node2);

        polygons1 = node1.allPolygons();
        // Assert.assertEquals(15, polygons1.size());

        List<Triangle> triangles = new ArrayList<>();
        PolygonList node1Polygons = node1.allPolygons();

        for (PolygonListBrowser node1Browser = node1Polygons.browse(); node1Browser.hasNext(); node1Browser.next()) {
            Polygon p = node1Browser.get();
            triangles.addAll(p.toTriangle());
        }

        TriangleMesh mesh = new TriangleMesh(triangles);

        try (PrintWriter out = new PrintWriter(new FileOutputStream("build/node-iteration-recomposed.stl"))) {
            new STLWriter().write(mesh, out);
        }

    }

}
