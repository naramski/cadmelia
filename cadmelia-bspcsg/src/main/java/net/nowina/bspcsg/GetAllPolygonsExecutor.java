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
package net.nowina.bspcsg;

import net.nowina.bspcsg.collection.PolygonList;
import net.nowina.bspcsg.collection.PolygonListBrowser;

public abstract class GetAllPolygonsExecutor extends BSPTreeOperationExecutor {

    private Factory factory;

    public GetAllPolygonsExecutor(Factory factory) {
        this.factory = factory;
    }

    public abstract PolygonList execute(Node root);

    public PolygonList recomposePolygon(PolygonList collection) {
        /* We will remove all polygon that are part of bigger complete polygon */

        /* First all the flag are reseted */
        for (PolygonListBrowser browser = collection.browse(); browser.hasNext(); browser.next()) {
            DecomposablePolygon d = (DecomposablePolygon) browser.get();
            d.clearAddedFlag();
        }

        /* We addPolygonToNode only the biggest complete polygon and remove all the childre */
        PolygonList filtered = factory.newPolygonList();
        for (PolygonListBrowser browser = collection.browse(); browser.hasNext(); browser.next()) {
            DecomposablePolygon d = (DecomposablePolygon) browser.get();
            if (!d.isAdded()) {
                DecomposablePolygon complete = d.getBiggestComplete();
                filtered.addPolygon(complete);
                complete.markAdded();
                complete.detach();
            }
        }
        return filtered;
    }

    protected Factory getFactory() {
        return factory;
    }

}
