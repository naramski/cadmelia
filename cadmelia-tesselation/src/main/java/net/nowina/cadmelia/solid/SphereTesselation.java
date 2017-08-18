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
import net.nowina.cadmelia.construction.Solid;
import net.nowina.cadmelia.construction.Vector;

import java.util.ArrayList;
import java.util.List;

public class SphereTesselation<T extends Solid> extends Tesselation<T> {

    private double radius;
    private int slices;
    private int stacks;

    public SphereTesselation(double radius, int slices, int stacks, MeshToInternal<T> meshToSolid) {
        super(meshToSolid);
        this.radius = radius;
        this.slices = slices;
        this.stacks = stacks;
    }

    @Override
    protected List<Triangle> getPolygons() {

        List<Triangle> polygons = new ArrayList<>();

        for (int i = 0; i < slices; i++) {

            for (int j = 0; j < stacks; j++) {

                double theta = (double) i * Math.PI * 2 / slices;
                double phi = (double) j * Math.PI / stacks;
                double thetaNext = (double) (i + 1) * Math.PI * 2 / slices;
                double phiNext = (double) (j + 1) * Math.PI / stacks;

                if (j == 0) {

                    Vector v1 = sphereVertex(theta, phi);
                    Vector v2 = sphereVertex(thetaNext, phiNext);
                    Vector v3 = sphereVertex(theta, phiNext);

                    polygons.add(new Triangle(v1, v2, v3));

                } else if (j == stacks - 1) {

                    Vector v1 = sphereVertex(theta, phi);
                    Vector v2 = sphereVertex(thetaNext, phi);
                    Vector v3 = sphereVertex(theta, phiNext);
                    polygons.add(new Triangle(v1, v2, v3));

                } else {

                    Vector v1 = sphereVertex(theta, phi);
                    Vector v2 = sphereVertex(thetaNext, phi);
                    Vector v3 = sphereVertex(thetaNext, phiNext);
                    Vector v4 = sphereVertex(theta, phiNext);

                    polygons.add(new Triangle(v1, v2, v3));
                    polygons.add(new Triangle(v1, v3, v4));
                }

            }

        }

        return polygons;
    }

    private Vector sphereVertex(double theta, double phi) {
        Vector dir = new Vector(Math.cos(theta) * Math.sin(phi), Math.cos(phi), Math.sin(theta) * Math.sin(phi));
        return dir.times(radius);
    }

}
