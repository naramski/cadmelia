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
import net.nowina.cadmelia.construction.*;
import net.nowina.cadmelia.math.Transformation;

import java.util.List;

public class MockSolid implements Solid {

    private List<Triangle> polygons;

    public MockSolid(List<Triangle> polygons) {
        this.polygons = polygons;
    }

    @Override
    public void visit(MeshVisitor visitor) {
        for(Triangle p : polygons) {
            visitor.startFacet(p.getNormal());
            visitor.triangle(p.getPoints()[0], p.getPoints()[1], p.getPoints()[2]);
            visitor.endFacet();
        }
    }

    @Override
    public Construction union(Construction other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Construction difference(Construction other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Construction intersection(Construction other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Construction transform(Transformation tx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Construction rotate(Vector angle) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Construction translate(Vector vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Construction scale(Vector scale) {
        throw new UnsupportedOperationException();
    }
}
