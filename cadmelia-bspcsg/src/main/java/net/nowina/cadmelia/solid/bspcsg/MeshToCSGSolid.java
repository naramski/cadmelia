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

import net.nowina.bspcsg.CSG;
import net.nowina.bspcsg.collection.PolygonList;
import net.nowina.bspcsg.collection.VectorList;
import net.nowina.cadmelia.Triangle;
import net.nowina.cadmelia.solid.MeshToInternal;

import java.util.List;

public class MeshToCSGSolid implements MeshToInternal<CSGSolid> {

    private CSGSolidBuilder builder;

    public MeshToCSGSolid(CSGSolidBuilder builder) {
        this.builder = builder;
    }

    @Override
    public CSGSolid convert(List<Triangle> mesh) {

        PolygonList polygons = builder.newPolygonList();
        for (Triangle triangle : mesh) {
            VectorList vectors = builder.netVectorList(triangle.getPoints());
            polygons.addPolygon(builder.newPolygon(vectors));
        }

        CSG csg = builder.newCSG(polygons);
        return new CSGSolid(builder, csg);
    }

}