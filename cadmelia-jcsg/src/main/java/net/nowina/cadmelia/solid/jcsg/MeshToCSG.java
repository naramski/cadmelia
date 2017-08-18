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
package net.nowina.cadmelia.solid.jcsg;

import eu.mihosoft.jcsg.CSG;
import eu.mihosoft.jcsg.Polygon;
import eu.mihosoft.jcsg.Vertex;
import eu.mihosoft.vvecmath.Vector3d;
import net.nowina.cadmelia.Triangle;
import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.solid.MeshToInternal;

import java.util.ArrayList;
import java.util.List;

public class MeshToCSG implements MeshToInternal<JCSGSolid> {

    @Override
    public JCSGSolid convert(List<Triangle> mesh) {

        List<Polygon> polygons = new ArrayList<>();
        for (Triangle t : mesh) {

            List<Vertex> points = new ArrayList<>();
            for (Vector v : t.getPoints()) {
                points.add(new Vertex(Vector3d.xyz(v.x(), v.y(), v.z()), null));
            }

            polygons.add(new Polygon(points));
        }

        return new JCSGSolid(CSG.fromPolygons(polygons));
    }

}
