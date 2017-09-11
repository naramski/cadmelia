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
package net.nowina.cadmelia.ide.stl;

import javafx.scene.shape.TriangleMesh;
import net.nowina.cadmelia.construction.MeshVisitor;
import net.nowina.cadmelia.construction.Vector;

/**
 *
 * @author david
 */
public class ConvertSolidToJavaFX implements MeshVisitor {

    private final TriangleMesh mesh;
    int counter;

    double minX;
    double minY;
    double minZ;

    double maxX;
    double maxY;
    double maxZ;

    public ConvertSolidToJavaFX(TriangleMesh mesh) {
        this.mesh = mesh;
        counter = 0;
        minX = Double.POSITIVE_INFINITY;
        minY = Double.POSITIVE_INFINITY;
        minZ = Double.POSITIVE_INFINITY;
        maxX = Double.NEGATIVE_INFINITY;
        maxY = Double.NEGATIVE_INFINITY;
        maxZ = Double.NEGATIVE_INFINITY;
    }

    @Override
    public void startFacet(Vector normal) {

    }

    @Override
    public void triangle(Vector p1, Vector p2, Vector p3) {
        mesh.getPoints().addAll(
                (float) p1.x(), (float) p1.y(), (float) p1.z(),
                (float) p2.x(), (float) p2.y(), (float) p2.z(),
                (float) p3.x(), (float) p3.y(), (float) p3.z());

        minX = min(minX, p1.x(), p2.x(), p3.x());
        maxX = max(maxX, p1.x(), p2.x(), p3.x());
        minY = min(minY, p1.y(), p2.y(), p3.y());
        maxY = max(maxY, p1.y(), p2.y(), p3.y());
        minZ = min(minZ, p1.z(), p2.z(), p3.z());
        maxZ = max(maxZ, p1.z(), p2.z(), p3.z());
    }

    @Override
    public void endFacet() {

        mesh.getTexCoords().addAll(0); // texture (not covered)
        mesh.getTexCoords().addAll(0);

        mesh.getFaces().addAll(
                counter, // first vertex
                0, // texture (not covered)
                counter + 1, // second vertex
                0, // texture (not covered)
                counter + 2, // third vertex
                0 // texture (not covered)
        );
        counter += 3;

    }

    public double min(double... values) {
        double min = values[0];
        for (int i = 1; i < values.length; i++) {
            min = Math.min(min, values[i]);
        }
        return min;
    }

    public double max(double... values) {
        double max = values[0];
        for (int i = 1; i < values.length; i++) {
            max = Math.max(max, values[i]);
        }
        return max;
    }

}

