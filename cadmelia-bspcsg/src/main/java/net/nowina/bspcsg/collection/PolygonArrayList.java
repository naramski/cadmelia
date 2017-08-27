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
package net.nowina.bspcsg.collection;

import net.nowina.bspcsg.Factory;
import net.nowina.bspcsg.Polygon;

import java.util.ArrayList;

public class PolygonArrayList extends ArrayList<Polygon> implements PolygonList {

    private Factory builder;

    public PolygonArrayList(Factory builder) {
        this.builder = builder;
    }

    private PolygonArrayList(Factory builder, PolygonList originalPolygons) {

        for (PolygonListBrowser browser = originalPolygons.browse(); browser.hasNext(); browser.next()) {
            Polygon p = browser.get();
            addPolygon(p.copy());
        }
        this.builder = builder;
    }

    @Override
    public Polygon getPolygon(int i) {
        return get(i);
    }

    @Override
    public void addPolygon(Polygon p) {
        add(p);
    }

    @Override
    public PolygonListBrowser browse() {
        return new Browser();
    }

    @Override
    public void addAll(PolygonList list) {
        for (PolygonListBrowser browser = list.browse(); browser.hasNext(); browser.next()) {
            add(browser.get());
        }
    }

    @Override
    public PolygonList copy() {
        return new PolygonArrayList(builder, this);
    }

    class Browser implements PolygonListBrowser {

        private int index;

        @Override
        public int size() {
            return PolygonArrayList.this.size();
        }

        @Override
        public Polygon get() {
            return PolygonArrayList.this.get(index);
        }

        @Override
        public boolean hasNext() {
            return index < size();
        }

        @Override
        public void next() {
            index++;
        }

        @Override
        public VectorListBrowser browseVertices() {
            return get().getVertices().browse();
        }

    }

}
