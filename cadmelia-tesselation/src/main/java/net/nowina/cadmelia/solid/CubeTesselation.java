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

import net.nowina.cadmelia.Triangle;
import net.nowina.cadmelia.construction.Solid;
import net.nowina.cadmelia.construction.Vector;

import java.util.ArrayList;
import java.util.List;

public class CubeTesselation<T extends Solid> extends Tesselation<T> {

    private final Vector min;
    private final Vector max;

    public CubeTesselation(Vector min, Vector max, MeshToInternal<T> meshToSolid) {
        super(meshToSolid);
        this.min = min;
        this.max = max;
    }

    @Override
    protected List<Triangle> getPolygons() {

        List<Triangle> polygons = new ArrayList<>();

        Vector p0 = min;
        Vector p1 = new Vector(max.x(), min.y(), min.z());
        Vector p2 = new Vector(max.x(), max.y(), min.z());
        Vector p3 = new Vector(min.x(), max.y(), min.z());
        Vector p4 = new Vector(min.x(), min.y(), max.z());
        Vector p5 = new Vector(max.x(), min.y(), max.z());
        Vector p6 = max;
        Vector p7 = new Vector(min.x(), max.y(), max.z());

        // bottom
        polygons.add(new Triangle(p0, p2, p1));
        polygons.add(new Triangle(p2, p0, p3));

        // top
        polygons.add(new Triangle(p4, p6, p7));
        polygons.add(new Triangle(p6, p4, p5));

        // front
        polygons.add(new Triangle(p4, p1, p5));
        polygons.add(new Triangle(p1, p4, p0));

        // back
        polygons.add(new Triangle(p3, p6, p2));
        polygons.add(new Triangle(p6, p3, p7));

        // right
        polygons.add(new Triangle(p5, p2, p6));
        polygons.add(new Triangle(p2, p5, p1));

        // left
        polygons.add(new Triangle(p3, p4, p7));
        polygons.add(new Triangle(p4, p3, p0));

        return polygons;
    }

}
