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

public class Cylinder extends ModelSolid {

    private double bottomRadius;

    private double topRadius;

    private double height;

    private int slices;

    private boolean centered;

    public Cylinder(double bottomRadius, double topRadius, double height, int slices, boolean centered) {
        this.bottomRadius = bottomRadius;
        this.topRadius = topRadius;
        this.height = height;
        this.slices = slices;
        this.centered = centered;
    }

    @Override
    public String toString() {
        return "Cylinder{" +
                "bottomRadius=" + bottomRadius +
                ", topRadius=" + topRadius +
                ", height=" + height +
                ", slices=" + slices +
                ", centered=" + centered +
                '}';
    }

    public double getBottomRadius() {
        return bottomRadius;
    }

    public double getTopRadius() {
        return topRadius;
    }

    public double getHeight() {
        return height;
    }

    public int getSlices() {
        return slices;
    }

    public boolean isCentered() {
        return centered;
    }
}
