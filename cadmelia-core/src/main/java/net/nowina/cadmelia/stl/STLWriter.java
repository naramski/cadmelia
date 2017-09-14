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
import net.nowina.cadmelia.construction.Construction;
import net.nowina.cadmelia.construction.MeshVisitor;
import net.nowina.cadmelia.construction.Vector;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class STLWriter {

    public static void writeToFile(String filePath, Construction solid) throws IOException {
        Path p = Paths.get(filePath);
        writeToFile(p, solid);
    }

    public static void writeToFile(Path p, Construction solid) throws IOException {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(p, Charset.forName("UTF-8"),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            new STLWriter().write(solid, writer);
        }
    }

    public void write(TriangleMesh polyhedron, PrintWriter output) {
        output.write("solid Solid\n");
        for (Triangle p : polyhedron.getPolygons()) {
            output.write("facet normal ");
            write(p.getNormal(), output);
            output.write("outer loop\n");
            for (Vector v : p.getPoints()) {
                output.write("vertex ");
                write(v, output);
            }
            output.write("endloop\nendfacet\n");
        }
        output.write("endsolid Solid\n");
    }

    public void write(Construction solid, PrintWriter output) {
        output.write("solid Solid\n");
        solid.visit(new MeshVisitor() {
            @Override
            public void startFacet(Vector normal) {
                output.write("facet normal ");
                write(normal, output);
                output.write("outer loop\n");
            }

            @Override
            public void triangle(Vector p1, Vector p2, Vector p3) {
                output.write("vertex ");
                write(p1, output);
                output.write("vertex ");
                write(p2, output);
                output.write("vertex ");
                write(p3, output);
            }

            @Override
            public void endFacet() {
                output.write("endloop\nendfacet\n");
            }
        });
        output.write("endsolid Solid\n");
    }

    void write(Vector vector, PrintWriter output) {
        output.write(Double.toString(vector.x()));
        output.write(" ");
        output.write(Double.toString(vector.y()));
        output.write(" ");
        output.write(Double.toString(vector.z()));
        output.write("\n");
    }

}
