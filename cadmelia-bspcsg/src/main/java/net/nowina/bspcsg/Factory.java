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

import net.nowina.bspcsg.collection.PolygonArrayList;
import net.nowina.bspcsg.collection.PolygonList;
import net.nowina.bspcsg.collection.VectorDoubleArrayList;
import net.nowina.bspcsg.collection.VectorList;
import net.nowina.cadmelia.construction.Vector;

public class Factory {

    private boolean usingDecomposablePolygon = true;

    private boolean addingEqualsVertex = false;

    private boolean addingNearlyEqualsVertex = false;

    private boolean usingComposite = true;

    private boolean nodeWithoutRecursion = true;

    private AddPolygonsToNodeExecutor addPolygonsToNodeExecutor;

    private ClipNodeToNodeExecutor clipNodeToNodeExecutor;

    private ClipPolygonsToNodeExecutor clipPolygonsToNodeExecutor;

    private InvertNodeExecutor invertNodeExecutor;

    private GetAllPolygonsExecutor getAllPolygonsExecutor;

    public Factory(boolean usingComposite, boolean usingDecomposablePolygon, boolean withoutRecursion) {
        this.usingComposite = usingComposite;
        this.usingDecomposablePolygon = usingDecomposablePolygon;
        this.nodeWithoutRecursion = withoutRecursion;

        if(withoutRecursion) {
            addPolygonsToNodeExecutor = new AddPolygonsToNodeIterativeExecutor(this);
            invertNodeExecutor = new InvertNodeIterativeExecutor();
            clipNodeToNodeExecutor = new ClipNodeToNodeIterativeExecutor(this);
            clipPolygonsToNodeExecutor = new ClipPolygonsToNodeIterativeExecutor(this);
            getAllPolygonsExecutor = new GetAllPolygonsIterativeExecutor(this);
        } else {
            addPolygonsToNodeExecutor = new AddPolygonsToNodeRecursiveExecutor(this);
            invertNodeExecutor = new InvertNodeRecurviseExecutor();
            clipNodeToNodeExecutor = new ClipNodeToNodeRecursiveExecutor(this);
            clipPolygonsToNodeExecutor = new ClipPolygonsToNodeRecursiveExecutor(this);
            getAllPolygonsExecutor = new GetAllPolygonsRecurviseExecutor(this);
        }

    }

    public Polygon newPolygon(VectorList vertices) {
        if (usingDecomposablePolygon) {
            return new DecomposablePolygon(this, vertices, null, null);
        } else {
            return new Polygon(this, vertices, null);
        }
    }

    public Polygon newChildPolygon(VectorList vertices, Polygon parent) {
        if (usingDecomposablePolygon) {
            DecomposablePolygon p = (DecomposablePolygon) parent;
            DecomposablePolygon d = new DecomposablePolygon(this, vertices, parent.getNormal(), (DecomposablePolygon) parent);
            p.addChild(d);
            return d;
        } else {
            return new Polygon(this, vertices, parent.getNormal());
        }
    }

    public Node newNode(PolygonList polygons) {
        return new Node(this, polygons);
    }

    public CSG newCSG(Node node) {
        return new CSG(this, node.allPolygons(), node);
    }

    public CSG newCSG(PolygonList polygons) {
        return new CSG(this, polygons, null);
    }

    public VectorList netVectorList() {
        return new VectorDoubleArrayList();
    }

    public VectorList netVectorList(Vector... vectors) {
        return new VectorDoubleArrayList(vectors);
    }

    public PolygonList newPolygonList() {
        return new PolygonArrayList(this);
    }

    public boolean isAddingEqualsVertex() {
        return addingEqualsVertex;
    }

    public boolean isAddingNearlyEqualsVertex() {
        return addingNearlyEqualsVertex;
    }

    public boolean isUsingComposite() {
        return usingComposite;
    }

    public boolean isUsingDecomposablePolygon() {
        return usingDecomposablePolygon;
    }

    public boolean isNodeWithoutRecursion() {
        return nodeWithoutRecursion;
    }

    public AddPolygonsToNodeExecutor getAddPolygonsToNodeExecutor() {
        return addPolygonsToNodeExecutor;
    }

    public ClipNodeToNodeExecutor getClipNodeToNodeExecutor() {
        return clipNodeToNodeExecutor;
    }

    public ClipPolygonsToNodeExecutor getClipPolygonsToNodeExecutor() {
        return clipPolygonsToNodeExecutor;
    }

    public InvertNodeExecutor getInvertNodeExecutor() {
        return invertNodeExecutor;
    }

    public GetAllPolygonsExecutor getGetAllPolygonsExecutor() {
        return getAllPolygonsExecutor;
    }

}