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

import java.util.ArrayList;
import java.util.List;

public class AddPolygonsToNodeIterativeExecutor extends AddPolygonsToNodeExecutor {

    private List<PolygonFragment> fragments = new ArrayList<>();

    public AddPolygonsToNodeIterativeExecutor(Factory factory) {
        super(factory);
    }

    @Override
    public void execute(PolygonList polygonsToAdd, Node node) {

        for(PolygonListBrowser p = polygonsToAdd.browse(); p.hasNext(); p.next()) {
            fragments.add(new PolygonFragment(p.get(), node));

            while(!fragments.isEmpty()) {

                PolygonFragment f = fragments.remove(0);
                addPolygonToNode(f.polygon, f.node);

            }

        }

    }

    protected void addFrontPolygon(Polygon polygon, Node node) {
        if (node.getFront() == null) {
            node.setFront(new Node(getFactory()));
        }
        fragments.add(new PolygonFragment(polygon, node.getFront()));
    }

    protected void addBackPolygon(Polygon polygon, Node node) {
        if (node.getBack() == null) {
            node.setBack(new Node(getFactory()));
        }
        fragments.add(new PolygonFragment(polygon, node.getBack()));
    }

    static class PolygonFragment {

        private Polygon polygon;

        private Node node;

        public PolygonFragment(Polygon polygon, Node node) {
            this.polygon = polygon;
            this.node = node;
        }

    }

}
