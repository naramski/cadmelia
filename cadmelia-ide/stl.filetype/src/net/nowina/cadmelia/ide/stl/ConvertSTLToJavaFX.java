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
import net.nowina.cadmelia.stl.STLEventHandler;

/**
 *
 * @author david
 */
public class ConvertSTLToJavaFX implements STLEventHandler {

    private final TriangleMesh mesh;
    int counter;

    double minX;
    double minY;
    double minZ;

    double maxX;
    double maxY;
    double maxZ;

    public ConvertSTLToJavaFX(TriangleMesh mesh) {
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
    public void onVertex(double x, double y, double z) {
        
        mesh.getPoints().addAll((float) x, (float) y, (float) z);
        
        minX = Math.min(minX, x);
        minY = Math.min(minY, y);
        minZ = Math.min(minZ, z);
        
        maxX = Math.max(maxX, x);
        maxY = Math.max(maxY, y);
        maxZ = Math.max(maxZ, z);

    }

    @Override
    public void onFacetStart(double normalX, double normalY, double normalZ) {
    }
    
    

    @Override
    public void onFacetEnd() {
        mesh.getTexCoords().addAll(0); // texture (not covered)
        mesh.getTexCoords().addAll(0);

        mesh.getFaces().addAll(
                counter, 
                0, // texture (not covered)
                counter + 1, // second vertex
                0, // texture (not covered)
                counter + 2, // third vertex
                0 // texture (not covered)
        );
        counter += 3;
    }

}

