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

import javafx.geometry.BoundingBox;
import net.nowina.bspcsg.collection.PolygonList;
import net.nowina.cadmelia.math.Transformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The union of two disjoint solids doesn't require BSP
 */
public class CompositeCSG extends CSG {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompositeCSG.class);

    private static int INSTANCE_COUNT = 0;

    private int instanceNumber;

    public List<CSG> separatedCSGs;

    public CompositeCSG(Factory builder, CSG first) {

        super(builder, builder.newPolygonList(), null);

        if (first == null) {
            throw new NullPointerException();
        }

        if (first == null) {
            separatedCSGs = new ArrayList<>();
        } else {

            if (first instanceof CompositeCSG) {
                LOGGER.info("First object is composite");
                CompositeCSG composite = (CompositeCSG) first;
                separatedCSGs = new ArrayList<>(composite.separatedCSGs);
            } else {
                LOGGER.info("New composite");
                separatedCSGs = new ArrayList<>();
                separatedCSGs.add(first);
            }

        }

        instanceNumber = INSTANCE_COUNT++;
    }

    @Override
    public PolygonList getPolygons() {
        return collapse().getPolygons();
    }

    /**
     * Return trus is the CSG has a bounding box that interesct with one of the bounding box of the composed CSG
     *
     * @param csg
     * @return
     */
    private boolean mayIntersect(CSG csg) {
        BoundingBox bounds = csg.getBounds();
        for (CSG internal : separatedCSGs) {
            if (internal.getBounds().intersects(bounds)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CSG union(CSG csg) {

        LOGGER.info("Union of " + this + " with " + csg);
        if (mayIntersect(csg)) {
            LOGGER.info("There is maybe an intersection");
            /* There may be an intersection of this CSG with the other CSG. We then "collapse" the other CSG in one
            and then perform the union as usual.
             */
            CSG currentCSG = collapse();

            CSG otherCSG = unwrapCSG(csg);
            return currentCSG.union(otherCSG);

        } else {
            LOGGER.info("No intersection with the other " + separatedCSGs.size() + ". Saving time with " + this);
            if (csg instanceof CompositeCSG) {
                CompositeCSG composite = (CompositeCSG) csg;
                separatedCSGs.addAll(composite.separatedCSGs);
            } else {
                separatedCSGs.add(csg);
            }
            return this;
        }

    }

    private CSG unwrapCSG(CSG csg) {
        CSG otherCSG = null;
        if (csg instanceof CompositeCSG) {
            PolygonList polygons = csg.getPolygons();
            /* We do not use the Builder as this CSG must not be a CompositeCSG */
            otherCSG = new CSG(getFactory(), polygons, null);
        } else {
            otherCSG = csg;
        }
        return otherCSG;
    }

    /**
     * Return the CSG composed of the polygons of all the separated internal CSG of this composite
     *
     * @return
     */
    private CSG collapse() {

        PolygonList polygons = getFactory().newPolygonList();
        for (CSG separatedCSG : separatedCSGs) {
            polygons.addAll(separatedCSG.getPolygons());
        }
        LOGGER.info("Collapsing CSG, had cached " + separatedCSGs.size() + " operations in " + this + " resulting in CSG of #" + polygons.size() + " polygons");
        /* We do not use the Builder as this CSG must not be a CompositeCSG */
        return new CSG(getFactory(), polygons, null);
    }

    @Override
    public CSG difference(CSG csg) {
        return collapse().difference(unwrapCSG(csg));
    }

    @Override
    public CSG intersect(CSG csg) {
        return collapse().intersect(unwrapCSG(csg));
    }

    @Override
    public CSG transformed(Transformation transform) {

        if (separatedCSGs.size() == 1) {
            return separatedCSGs.get(0).transformed(transform);
        } else {
            return new CompositeCSG(getFactory(), collapse().transformed(transform));
        }

    }

    @Override
    public BoundingBox getBounds() {
        BoundingBox total = null;
        for (CSG csg : separatedCSGs) {
            if (total == null) {
                total = csg.getBounds();
            } else {
                BoundingBox second = csg.getBounds();
                double minX = Math.min(total.getMinX(), second.getMinX());
                double minY = Math.min(total.getMinY(), second.getMinY());
                double minZ = Math.min(total.getMinZ(), second.getMinZ());
                double maxX = Math.max(total.getMaxX(), second.getMaxX());
                double maxY = Math.max(total.getMaxY(), second.getMaxY());
                double maxZ = Math.max(total.getMaxZ(), second.getMaxZ());
                total = new BoundingBox(minX, minY, minZ, maxX - minX, maxY - minY, maxZ - minZ);
            }
        }
        return total;
    }

    @Override
    public String toString() {
        return "CSG[i=" + instanceNumber + "]";
    }

}