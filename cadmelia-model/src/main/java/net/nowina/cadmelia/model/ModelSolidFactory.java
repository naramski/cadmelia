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
package net.nowina.cadmelia.model;

import net.nowina.cadmelia.construction.Construction;
import net.nowina.cadmelia.construction.Solid;
import net.nowina.cadmelia.construction.SolidFactory;

public class ModelSolidFactory implements SolidFactory {

    @Override
    public Solid cube(double sizeX, double sizeY, double sizeZ, boolean centered) {
        return new Cube(sizeX, sizeY, sizeZ, centered);
    }

    @Override
    public Solid sphere(double radius, int slices, int stacks) {
        return new Sphere(radius, slices, stacks);
    }

    @Override
    public Solid cylinder(double bottomRadius, double topRadius, double height, int slices, boolean centered) {
        return new Cylinder(bottomRadius, topRadius, height, slices, centered);
    }

    @Override
    public Solid extrude(Construction shape, double height, double scale) {
        return new Extrusion(shape, height, scale);
    }

    @Override
    public Solid hull(Construction solid) {
        return new Hull(solid);
    }

}
