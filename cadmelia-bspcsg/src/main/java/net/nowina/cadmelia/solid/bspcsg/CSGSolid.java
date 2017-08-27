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

import net.nowina.bspcsg.CSG;
import net.nowina.bspcsg.CompositeCSG;
import net.nowina.bspcsg.Polygon;
import net.nowina.bspcsg.collection.PolygonList;
import net.nowina.bspcsg.collection.PolygonListBrowser;
import net.nowina.cadmelia.Transformation;
import net.nowina.cadmelia.Triangle;
import net.nowina.cadmelia.construction.Construction;
import net.nowina.cadmelia.construction.MeshVisitor;
import net.nowina.cadmelia.construction.Solid;
import net.nowina.cadmelia.construction.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSGSolid implements Solid {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSGSolid.class);

    private final boolean usingComposite;

    private static int INSTANCE_COUNT = 0;

    private final CSG csg;

    private int instanceNumber;

    private CSGSolidBuilder builder;

    public CSGSolid(CSGSolidBuilder builder, CSG csg) {

        usingComposite = builder.isUsingComposite();

        if (builder == null) {
            throw new NullPointerException();
        }
        this.builder = builder;

        if (csg == null) {
            throw new RuntimeException("csg must be defined");
        }
        this.csg = wrapCSG(csg);
        instanceNumber = INSTANCE_COUNT++;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public boolean isShape() {
        return false;
    }

    public CSG wrapCSG(CSG csg) {
        if (usingComposite) {
            if (csg instanceof CompositeCSG) {
                return csg;
            } else {
                return new CompositeCSG(builder, csg);
            }
        } else {
            return csg;
        }
    }

    public CSG getCSG() {
        return csg;
    }

    @Override
    public Construction union(Construction other) {
        if (!other.isSolid()) {
            return this;
        }

        CSGSolid solid = (CSGSolid) other;
        CSG csg = solid.getCSG();
        CSG thisCSG = getCSG();
        CSG unionCSG = thisCSG.union(csg);
        LOGGER.info("Union of Solid " + this + " and " + other + " resulting in " + unionCSG);
        return new CSGSolid(builder, unionCSG);
    }

    @Override
    public Construction difference(Construction other) {
        if (!other.isSolid()) {
            return this;
        }

        CSGSolid solid = (CSGSolid) other;
        return builder.newCSGSolid(getCSG().difference(solid.getCSG()));
    }

    @Override
    public Construction intersection(Construction other) {
        if (!other.isSolid()) {
            return this;
        }

        CSGSolid solid = (CSGSolid) other;
        return new CSGSolid(builder, getCSG().intersect(solid.getCSG()));
    }

    @Override
    public Construction rotate(Vector angle) {

        Transformation t = Transformation.unity().rot(angle);
        return new CSGSolid(builder, getCSG().transformed(t));
    }

    @Override
    public Construction translate(Vector vector) {

        Transformation t = Transformation.unity().translate(vector);
        return new CSGSolid(builder, getCSG().transformed(t));
    }

    @Override
    public Construction scale(Vector scale) {

        Transformation t = Transformation.unity().scale(scale);
        return new CSGSolid(builder, getCSG().transformed(t));
    }

    @Override
    public void visit(MeshVisitor visitor) {

        CSG csg = getCSG();
        PolygonList polygons = csg.getPolygons();

        for (PolygonListBrowser browser = polygons.browse(); browser.hasNext(); browser.next()) {
            Polygon p = browser.get();

            for (Triangle facet : p.toTriangle()) {
                Vector normal = p.getNormal();
                visitor.startFacet(normal);
                Vector[] points = facet.getPoints();
                visitor.triangle(points[0], points[1], points[2]);
                visitor.endFacet();
            }
        }

    }

    @Override
    public String toString() {
        return "CSGSolid[i=" + instanceNumber + "," + csg + "]";
    }
}
