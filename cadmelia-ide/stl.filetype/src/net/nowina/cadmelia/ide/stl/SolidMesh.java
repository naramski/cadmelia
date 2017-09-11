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

import java.io.IOException;
import java.io.InputStream;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import net.nowina.cadmelia.construction.Solid;
import net.nowina.cadmelia.stl.STLReader;
import net.nowina.cadmelia.stl.parser.ParseException;

public class SolidMesh {

    private final MeshView meshView;
    private final double sizeX;
    private final double sizeY;
    private final double sizeZ;

    SolidMesh(Solid solid) {

        TriangleMesh mesh = new TriangleMesh();
        ConvertSolidToJavaFX converter = new ConvertSolidToJavaFX(mesh);
        solid.visit(converter);

        sizeX = converter.maxX - converter.minX;
        sizeY = converter.maxY - converter.minY;
        sizeZ = converter.maxZ - converter.minZ;

        Material mat = new PhongMaterial(Color.YELLOW);

        meshView = new MeshView(mesh);
        meshView.setMaterial(mat);
        meshView.setCullFace(CullFace.NONE);

    }

    SolidMesh(STLDataObject obj) throws IOException, ParseException {

        TriangleMesh mesh = new TriangleMesh();

        final ConvertSTLToJavaFX converter = new ConvertSTLToJavaFX(mesh);
        try(InputStream in = obj.getPrimaryEntry().getFile().getInputStream()) {
            STLReader.parseAscii(in, converter);
        }

        sizeX = converter.maxX - converter.minX;
        sizeY = converter.maxY - converter.minY;
        sizeZ = converter.maxZ - converter.minZ;

        Material mat = new PhongMaterial(Color.YELLOW);

        meshView = new MeshView(mesh);
        meshView.setMaterial(mat);
        meshView.setCullFace(CullFace.NONE);

    }

    public double getSizeX() {
        return sizeX;
    }

    public double getSizeY() {
        return sizeY;
    }

    public double getSizeZ() {
        return sizeZ;
    }

    public MeshView getMeshView() {
        return meshView;
    }

}