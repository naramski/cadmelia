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
package net.nowina.cadmelia.stl;

import net.nowina.cadmelia.Triangle;
import net.nowina.cadmelia.TriangleMesh;
import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.stl.parser.ParseException;
import net.nowina.cadmelia.stl.parser.STLParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class STLAnalyser {

    Map<Vector, Vector> points;

    List<Triangle> polygons;

    public void readSTL(InputStream stream) throws ParseException {
        points = new HashMap<>();
        polygons = new ArrayList<>();
        STLParser parser = new STLParser(stream);
        parser.setEventHandler(new AnalyserEventHandler());
        parser.Solid();
    }

    public List<TriangleMesh> toPolyhedrons() {
        List<TriangleMesh> solids = new ArrayList<>();
        for (Triangle candidate : polygons) {
            List<TriangleMesh> found = new ArrayList<>();
            for (TriangleMesh solid : solids) {
                for (Triangle part : solid.getPolygons()) {
                    if (part.touchEdge(candidate)) {
                        found.add(solid);
                        break;
                    }
                }
            }
            if (!found.isEmpty()) {
                TriangleMesh solid = new TriangleMesh(found.toArray(new TriangleMesh[found.size()]));
                solids.add(solid);
                solid.getPolygons().add(candidate);
                for (TriangleMesh p : found) {
                    solids.remove(p);
                }
            }
            if (found.isEmpty()) {
                TriangleMesh solid = new TriangleMesh();
                solid.getPolygons().add(candidate);
                solids.add(solid);
            }
        }
        return solids;
    }

    Vector addPoint(Vector v) {
        Vector contained = points.get(v);
        if (contained == null) {
            points.put(v, v);
            return v;
        } else {
            return contained;
        }
    }

    class AnalyserEventHandler implements STLEventHandler {

        private List<Vector> loop = new ArrayList<>();

        private Vector lastNormal;

        @Override
        public void onFacetStart(double normalX, double normalY, double normalZ) {
            lastNormal = new Vector(normalX, normalY, normalZ);
        }

        @Override
        public void onVertex(double x, double y, double z) {
            Vector v = new Vector(x, y, z);
            v = addPoint(v);
            loop.add(v);
        }

        @Override
        public void onFacetEnd() {
            polygons.add(new Triangle(lastNormal, loop));
            loop.clear();
            lastNormal = null;
        }

    }

}
