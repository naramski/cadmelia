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

public class CylinderTesselation<T extends Solid> extends Tesselation<T> {

    private double bottomRadius;
    private double topRadius;
    private double height;
    private int slices;
    private boolean centered;

    public CylinderTesselation(double bottomRadius, double topRadius, double height, int slices, boolean centered, MeshToInternal<T> meshToSolid) {
        super(meshToSolid);
        this.bottomRadius = bottomRadius;
        this.topRadius = topRadius;
        this.height = height;
        this.slices = slices;
        this.centered = centered;
    }

    @Override
    protected List<Triangle> getPolygons() {

        Vector centerBottom = new Vector(0, 0, centered ? -height / 2 : 0);
        Vector centerTop = new Vector(0, 0, centered ? height / 2 : height);

        List<Triangle> polygons = new ArrayList<>();

        for (int i = 0; i < slices; i++) {

            int t0 = i;
            int t1 = (i + 1) % slices;

            polygons.add(new Triangle(
                    centerBottom,
                    vertex(false, t1),
                    vertex(false, t0)
            ));

            polygons.add(new Triangle(
                    vertex(false, t0),
                    vertex(false, t1),
                    vertex(true, t1)
            ));

            polygons.add(new Triangle(
                    vertex(false, t0),
                    vertex(true, t1),
                    vertex(true, t0)
            ));

            polygons.add(new Triangle(
                    centerTop,
                    vertex(true, t0),
                    vertex(true, t1)
            ));

        }

        return polygons;
    }

    private Vector vertex(boolean top, int slice) {
        double a = slice * Math.PI * 2 / slices;
        double h = top ? (centered ? height / 2 : height) : (centered ? -height / 2 : 0);
        double r = top ? topRadius : bottomRadius;
        return new Vector(Math.cos(a) * r, Math.sin(a) * r, h);
    }

}
