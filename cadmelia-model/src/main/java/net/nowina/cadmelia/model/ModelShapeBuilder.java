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
import net.nowina.cadmelia.construction.Shape;
import net.nowina.cadmelia.construction.ShapeBuilder;
import net.nowina.cadmelia.construction.Vector;

import java.util.List;

public class ModelShapeBuilder implements ShapeBuilder {

    @Override
    public Shape offset(Construction c, double offset) {
        return new Offset(c, offset);
    }

    @Override
    public Shape circle(double radius, int slices) {
        return new Circle(radius, slices);
    }

    @Override
    public Shape square(double sizeX, double sizeY, boolean centered) {
        return new Square(sizeX, sizeY, centered);
    }

    @Override
    public Shape polygon(List<Vector> points) {
        return new Polygon(points);
    }

    @Override
    public Shape text(String text, int size, String font) {
        return new Text(text, font, size);
    }

}
