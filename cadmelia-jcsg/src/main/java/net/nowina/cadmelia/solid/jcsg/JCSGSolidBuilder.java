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

import eu.mihosoft.jcsg.Cube;
import eu.mihosoft.jcsg.Cylinder;
import eu.mihosoft.jcsg.PropertyStorage;
import eu.mihosoft.jcsg.Sphere;
import eu.mihosoft.jcsg.ext.quickhull3d.HullUtil;
import eu.mihosoft.vvecmath.Vector3d;
import net.nowina.cadmelia.construction.Construction;
import net.nowina.cadmelia.construction.Shape;
import net.nowina.cadmelia.construction.Solid;
import net.nowina.cadmelia.construction.SolidBuilder;

public class JCSGSolidBuilder implements SolidBuilder {

    @Override
    public Solid cube(double sizeX, double sizeY, double sizeZ, boolean centered) {
        Cube cube = new Cube(sizeX, sizeY, sizeZ);
        if (!centered) {
            cube.noCenter();
        }
        return new JCSGSolid(cube.toCSG());
    }

    @Override
    public Solid sphere(double radius, int slices, int stacks) {
        Sphere sphere = new Sphere(radius, slices, stacks);
        return new JCSGSolid(sphere.toCSG());
    }

    @Override
    public Solid cylinder(double bottomRadius, double topRadius, double height, int slices, boolean center) {
        Vector3d start = Vector3d.xyz(0, 0, center ? -height / 2 : 0);
        Vector3d stop = Vector3d.xyz(0, 0, center ? height / 2 : height);
        Cylinder cylinder = new Cylinder(start, stop, bottomRadius, topRadius, slices);
        return new JCSGSolid(cylinder.toCSG());
    }

    @Override
    public Solid extrude(Construction construction, double height) {

        if (!construction.isShape()) {
            return (Solid) construction;
        }

        JCSGExtrusion extrusion = new JCSGExtrusion((Shape) construction, height);
        return extrusion.buildSolid();

    }

    @Override
    public Solid hull(Construction solid) {

        JCSGSolid solid2 = (JCSGSolid) solid;

        return new JCSGSolid(HullUtil.hull(solid2.getCSG(), new PropertyStorage()));
    }

}
