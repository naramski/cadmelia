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

public class FactoryBuilder {

    private static FactoryBuilder INSTANCE = null;

    private List<ShapeFactory> shapeFactories = new ArrayList<>();

    private List<SolidFactory> solidFactories = new ArrayList<>();

    public static FactoryBuilder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FactoryBuilder();
        }
        return INSTANCE;
    }

    public ShapeFactory createShapeFactory() {
        if (shapeFactories.isEmpty()) {
            throw new RuntimeException("No shapeBuilder defined");
        }
        return shapeFactories.get(0);
    }

    public SolidFactory createSolidFactory() {
        if (solidFactories.isEmpty()) {
            throw new RuntimeException("No solidBuilder defined");
        }
        return solidFactories.get(0);
    }

    public static void registerShapeFactory(ShapeFactory factory) {
        getInstance().shapeFactories.add(factory);
    }

    public static void registerSolidFactory(SolidFactory factory) {
        getInstance().solidFactories.add(factory);
    }

}
