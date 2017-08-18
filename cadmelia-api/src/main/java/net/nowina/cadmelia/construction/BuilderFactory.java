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
package net.nowina.cadmelia.construction;

import java.util.ArrayList;
import java.util.List;

public class BuilderFactory {

    private static BuilderFactory INSTANCE = null;

    private List<ShapeBuilder> shapeBuilders = new ArrayList<>();

    private List<SolidBuilder> solidBuilders = new ArrayList<>();

    public static BuilderFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BuilderFactory();
        }
        return INSTANCE;
    }

    public ShapeBuilder createShapeBuilder() {
        if (shapeBuilders.isEmpty()) {
            throw new RuntimeException("No shapeBuilder defined");
        }
        return shapeBuilders.get(0);
    }

    public SolidBuilder createSolidBuilder() {
        if (solidBuilders.isEmpty()) {
            throw new RuntimeException("No solidBuilder defined");
        }
        return solidBuilders.get(0);
    }

    public static void registerShapeBuilder(ShapeBuilder shapeBuilder) {
        getInstance().shapeBuilders.add(shapeBuilder);
    }

    public static void registerSolidBuilder(SolidBuilder solidBuilder) {
        getInstance().solidBuilders.add(solidBuilder);
    }

}
