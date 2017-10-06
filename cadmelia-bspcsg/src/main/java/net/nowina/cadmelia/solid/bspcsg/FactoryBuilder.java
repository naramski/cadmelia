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
package net.nowina.cadmelia.solid.bspcsg;

public class FactoryBuilder {

    private boolean usingDecomposablePolygon = true;

    private boolean addingEqualsVertex = false;

    private boolean addingNearlyEqualsVertex = false;

    private boolean usingComposite = true;

    private boolean withoutRecursion = true;

    private boolean discardingInvalidPolygon = true;

    public CSGSolidFactory build() {
        return new CSGSolidFactory(usingComposite, usingDecomposablePolygon, withoutRecursion, discardingInvalidPolygon);
    }

    public FactoryBuilder usingDecomposablePolygon(boolean value) {
        usingDecomposablePolygon = value;
        return this;
    }

    public FactoryBuilder usingComposite(boolean value) {
        usingComposite = value;
        return this;
    }

    public FactoryBuilder withoutRecursion(boolean value) {
        withoutRecursion = value;
        return this;
    }

    public FactoryBuilder discardingInvalidPolygon(boolean value) {
        discardingInvalidPolygon = value;
        return this;
    }

}
