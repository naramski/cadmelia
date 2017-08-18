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

import net.nowina.cadmelia.construction.BuilderFactory;
import net.nowina.cadmelia.construction.ShapeBuilder;
import net.nowina.cadmelia.construction.SolidBuilder;

public class ModelBuilderFactory extends BuilderFactory {

    @Override
    public ShapeBuilder createShapeBuilder() {
        return new ModelShapeBuilder();
    }

    @Override
    public SolidBuilder createSolidBuilder() {
        return new ModelSolidBuilder();
    }

}
