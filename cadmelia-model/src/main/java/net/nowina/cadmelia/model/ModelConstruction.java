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
package net.nowina.cadmelia.model;

import net.nowina.cadmelia.construction.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.List;

public abstract class ModelConstruction implements Construction, Shape, Solid {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelConstruction.class);

    private Transform transform;

    @Override
    public Construction union(Construction other) {
        LOGGER.info("Union of " + this + " with " + other);
        if (other != null) {
            if (this instanceof Union) {
                Union union = (Union) this;
                union.add(other);
                return union;
            } else {
                Union union = new Union();
                union.add(this);
                union.add(other);
                return union;
            }
        } else {
            return this;
        }
    }

    @Override
    public Construction difference(Construction other) {
        if (this instanceof Difference) {
            Difference difference = (Difference) this;
            difference.addElement(other);
            return difference;
        } else {
            Difference difference = new Difference(this);
            difference.addElement(other);
            return difference;
        }
    }

    @Override
    public Construction intersection(Construction other) {
        LOGGER.info("Intersection of " + this + " with " + other);
        if (this instanceof Intersection) {
            Intersection intersection = (Intersection) this;
            intersection.add(other);
            return intersection;
        } else {
            Intersection intersection = new Intersection();
            intersection.add(this);
            intersection.add(other);
            return intersection;
        }

    }

    @Override
    public Construction rotate(Vector angle) {
        apply(new Transform().rotate(angle.x(), angle.y(), angle.z()));
        return this;
    }

    @Override
    public Construction translate(Vector vector) {
        apply(new Transform().translate(vector.x(), vector.y(), vector.z()));
        return this;
    }

    @Override
    public Construction scale(Vector scale) {
        apply(new Transform().scale(scale.x(), scale.y(), scale.z()));
        return this;
    }

    private void apply(Transform transform) {
        if (this.transform == null) {
            this.transform = transform;
        } else {
            this.transform.appy(transform);
        }
    }

    public void print(PrintWriter writer) {
        print(writer, "");
    }

    protected void print(PrintWriter writer, String paddingPrefix) {
        writer.print(paddingPrefix);
        if (getTransform() != null) {
            writer.write("Transform{" + getTransform() + "} ");
        }
        writer.println(toString());
    }

    @Override
    public abstract boolean isShape();

    @Override
    public abstract boolean isSolid();

    @Override
    public void visit(MeshVisitor visitor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<PolygonWithHoles> getPolygons() {
        throw new UnsupportedOperationException();
    }

    public Transform getTransform() {
        return transform;
    }

}
