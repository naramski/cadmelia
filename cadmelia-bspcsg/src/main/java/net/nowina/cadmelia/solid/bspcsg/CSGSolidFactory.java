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

import com.github.quickhull3d.Point3d;
import com.github.quickhull3d.QuickHull3D;
import net.nowina.bspcsg.CSG;
import net.nowina.bspcsg.Factory;
import net.nowina.bspcsg.Polygon;
import net.nowina.bspcsg.collection.PolygonList;
import net.nowina.bspcsg.collection.PolygonListBrowser;
import net.nowina.bspcsg.collection.VectorList;
import net.nowina.bspcsg.collection.VectorListBrowser;
import net.nowina.cadmelia.construction.*;

import java.util.ArrayList;
import java.util.List;

public class CSGSolidFactory extends Factory implements SolidFactory {

    public CSGSolidFactory(boolean usingComposite, boolean usingDecomposablePolygon, boolean withoutRecursion, boolean discardingInvalidPolygon) {
        super(usingComposite, usingDecomposablePolygon, withoutRecursion, discardingInvalidPolygon);
    }

    public CSGSolid newCSGSolid(CSG csg) {
        return new CSGSolid(this, csg);
    }

    @Override
    public Solid cube(double sizeX, double sizeY, double sizeZ, boolean centered) {
        return new Cube(this, sizeX, sizeY, sizeZ, centered).buildSolid();
    }

    @Override
    public Solid sphere(double radius, int slices, int stacks) {
        return new Sphere(this, radius, slices, stacks).buildSolid();
    }

    @Override
    public Solid cylinder(double bottomRadius, double topRadius, double height, int slices, boolean center) {
        return new Cylinder(this, bottomRadius, topRadius, height, slices, center).buildSolid();
    }

    @Override
    public Solid extrude(Construction shape, double height) {

        if (!shape.isShape()) {
            return (Solid) shape;
        }

        Solid solid = new Extrusion(this, (Shape) shape, height).buildSolid();
        return solid;
    }

    @Override
    public Solid hull(Construction solid) {

        CSGSolid solid2 = (CSGSolid) solid;
        CSG csg = solid2.getCSG();

        List<Vector> points = new ArrayList<>(csg.getPolygons().size() * 3);

        for (PolygonListBrowser browserPolygons = csg.getPolygons().browse(); browserPolygons.hasNext(); browserPolygons.next()) {
            Polygon p = browserPolygons.get();

            for (VectorListBrowser browser = p.browseVertices(); browser.hasNext(); browser.next()) {
                Vector v = browser.get();
                points.add(v);
            }
        }

        csg = hull(points);

        return new CSGSolid(this, csg);
    }


    public CSG hull(List<Vector> points) {

        Point3d[] hullPoints = points.stream().map((vec) -> new Point3d(vec.x(), vec.y(), vec.z())).toArray(Point3d[]::new);

        QuickHull3D hull = new QuickHull3D();
        hull.build(hullPoints);
        hull.triangulate();

        int[][] faces = hull.getFaces();

        PolygonList polygons = newPolygonList();

        VectorList vertices = netVectorList();

        for (int[] verts : faces) {
            for (int i : verts) {
                vertices.addVector(points.get(hull.getVertexPointIndices()[i]));
            }
            polygons.addPolygon(newPolygon(vertices));
            vertices = netVectorList();
        }

        return newCSG(polygons);
    }

}
