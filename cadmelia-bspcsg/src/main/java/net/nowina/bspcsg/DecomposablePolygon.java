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

import net.nowina.bspcsg.collection.VectorList;
import net.nowina.cadmelia.Transformation;
import net.nowina.cadmelia.construction.Vector;

import java.util.ArrayList;
import java.util.List;

public class DecomposablePolygon extends Polygon {

    private DecomposablePolygon parent;

    private List<DecomposablePolygon> children;

    private boolean complete = true;

    private boolean added = false;

    private boolean flipped = false;

    public DecomposablePolygon(Factory builder, VectorList vertices, Vector normal, DecomposablePolygon parent) {
        super(builder, vertices, normal);
        this.parent = parent;
    }

    @Override
    public DecomposablePolygon copy() {
        return copy(true);
    }

    protected DecomposablePolygon copy(boolean copyChildren) {
        DecomposablePolygon copy = new DecomposablePolygon(getBuilder(), getVertices(), getNormal(), copyChildren ? parent : null);
        if (copyChildren) {
            List<DecomposablePolygon> children = new ArrayList<>();
            if (hasChildren()) {
                for (DecomposablePolygon child : getChildren()) {
                    children.add(child.copy());
                }
            }
            copy.flipped = flipped;
            copy.children = children;
        }
        return copy;
    }

    public DecomposablePolygon getParent() {
        return parent;
    }

    public DecomposablePolygon getBiggestComplete() {
        DecomposablePolygon current = this;

        /**
         * If there is a parent, which is complete, has the same side, like all the other children
         */
        while (current.getParent() != null && current.isParentComplete() && (getNormal().equals(getParent().getNormal()))
                && current.siblingsAreSameSide()) {
            current = current.getParent();
        }
        return current;
    }

    boolean siblingsAreSameSide() {
        return getParent().chilrenAreSameSide(flipped);
    }

    boolean chilrenAreSameSide(boolean firstValue) {
        if(children != null) {
            for (DecomposablePolygon polygon : children) {
                if (polygon.wasFlipped() != firstValue) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public void addChild(DecomposablePolygon child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    public List<DecomposablePolygon> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public void markIncomplete() {
        complete = false;
        if (getParent() != null) {
            getParent().markIncomplete();
        }
    }

    @Override
    public void flip() {
        flipped = !flipped;
        super.flip();
    }

    public void markAdded() {
        added = true;
        if (hasChildren()) {
            for (DecomposablePolygon p : getChildren()) {
                p.markAdded();
            }
        }
    }

    public void detach() {
        parent = null;
        children = null;
    }

    private void removeChild(DecomposablePolygon d) {
        if (children != null) {
            children.remove(d);
        }
        markIncomplete();
    }

    public void removeFromParent() {
        if (getParent() != null) {
            getParent().removeChild(this);
        }
    }

    boolean isParentComplete() {
        return getParent().isComplete();
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isAdded() {
        return added;
    }

    public boolean wasFlipped() {
        return flipped;
    }

    public void clearAddedFlag() {
        added = false;
        if (getParent() != null) {
            getParent().clearAddedFlag();
        }
    }

    @Override
    public Polygon transformed(Transformation transform) {
        Polygon polygon = copy(false);
        polygon.transform(transform);
        return polygon;
    }

}
