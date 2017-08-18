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
import eu.mihosoft.vvecmath.Transform;
import eu.mihosoft.vvecmath.Vector3d;
import net.nowina.cadmelia.construction.Construction;
import net.nowina.cadmelia.construction.MeshVisitor;
import net.nowina.cadmelia.construction.Solid;
import net.nowina.cadmelia.construction.Vector;

import java.util.List;

public class JCSGSolid implements Solid {

    private final CSG csg;

    public JCSGSolid(CSG csg) {
        this.csg = csg;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public boolean isShape() {
        return false;
    }

    public CSG getCSG() {
        return csg;
    }

    @Override
    public Construction union(Construction other) {
        if (!other.isSolid()) {
            return this;
        }

        JCSGSolid solid = (JCSGSolid) other;
        return new JCSGSolid(getCSG().union(solid.getCSG()));
    }

    @Override
    public Construction difference(Construction other) {
        if (!other.isSolid()) {
            return this;
        }

        JCSGSolid solid = (JCSGSolid) other;
        return new JCSGSolid(getCSG().difference(solid.getCSG()));
    }

    @Override
    public Construction intersection(Construction other) {
        if (!other.isSolid()) {
            return this;
        }

        JCSGSolid solid = (JCSGSolid) other;
        return new JCSGSolid(getCSG().intersect(solid.getCSG()));
    }

    @Override
    public Solid rotate(Vector angle) {

        Transform t = Transform.unity().rot(toVector(angle));
        return new JCSGSolid(getCSG().transformed(t));
    }

    @Override
    public Solid translate(Vector vector) {

        Transform t = Transform.unity().translate(toVector(vector));
        return new JCSGSolid(getCSG().transformed(t));
    }

    @Override
    public Solid scale(Vector scale) {

        Transform t = Transform.unity().scale(toVector(scale));
        return new JCSGSolid(getCSG().transformed(t));
    }

    @Override
    public void visit(MeshVisitor visitor) {

        CSG csg = getCSG();
        for (Polygon p : csg.getPolygons()) {

            List<Vertex> vertices = p.vertices;
            Vector3d n = p.plane.getNormal();
            Vector normal = new Vector(n.getX(), n.getY(), n.getZ());

            if (vertices.size() >= 3) {
                Vertex first = vertices.get(0);
                for (int i = 0; i < vertices.size() - 2; i++) {
                    Vertex second = vertices.get(i + 1);
                    Vertex third = vertices.get(i + 2);

                    visitor.startFacet(normal);

                    Vector v1 = new Vector(first.pos.getX(), first.pos.getY(), first.pos.getZ());
                    Vector v2 = new Vector(second.pos.getX(), second.pos.getY(), second.pos.getZ());
                    Vector v3 = new Vector(third.pos.getX(), third.pos.getY(), third.pos.getZ());

                    visitor.triangle(v1, v2, v3);
                    visitor.endFacet();

                }
            }
        }

    }

    Vector3d toVector(Vector v) {
        return Vector3d.xyz(v.x(), v.y(), v.z());
    }

}
