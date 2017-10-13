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
import net.nowina.cadmelia.math.Transformation;
import net.nowina.cadmelia.construction.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VectorArrayList extends ArrayList<Vector> implements VectorList {

    private static final Logger LOGGER = LoggerFactory.getLogger(VectorArrayList.class);

    private final boolean dontAddEquals;

    private final boolean dontAddNearlyEquals;

    private Factory builder;

    public VectorArrayList(Factory builder) {
        this.builder = builder;
        this.dontAddEquals = builder.isAddingEqualsVertex();
        this.dontAddNearlyEquals = builder.isAddingNearlyEqualsVertex();
    }

    public VectorArrayList(Factory builder, List<Vector> vectors) {
        super(vectors);
        this.builder = builder;
        this.dontAddEquals = builder.isAddingEqualsVertex();
        this.dontAddNearlyEquals = builder.isAddingNearlyEqualsVertex();
    }

    private VectorArrayList(Factory builder, VectorList vectors) {
        super((List<Vector>) vectors);
        this.builder = builder;
        this.dontAddEquals = builder.isAddingEqualsVertex();
        this.dontAddNearlyEquals = builder.isAddingNearlyEqualsVertex();
    }

    @Override
    public void reverse() {
        Collections.reverse(this);
    }

    @Override
    public void addVector(Vector vector) {
        if (dontAddEquals && super.contains(vector)) {
            LOGGER.warn("This vector is already contained in the List");
        } else {
            if (dontAddNearlyEquals) {
                VectorListBrowser browser = browse();
                for (int i = 0; i < browser.size(); i++) {
                    browser.index(i);
                    double deltaX = Math.abs(vector.x() - browser.x());
                    double deltaY = Math.abs(vector.y() - browser.y());
                    double deltaZ = Math.abs(vector.z() - browser.z());
                    if (deltaX < Vector.EPSILON && deltaY < Vector.EPSILON && deltaZ < Vector.EPSILON) {
                        LOGGER.warn("This vector is near an existing one");
                        return;
                    }
                }
            }
            super.add(vector);
        }
    }

    @Override
    public void addVector(VectorListBrowser browser) {
        addVector(browser.get());
    }

    @Override
    public Vector getVector(int i) {
        return get(i);
    }

    @Override
    public void applyTransformation(Transformation transformation) {
        for (int i = 0; i < size(); i++) {
            set(i, transformation.transform(get(i)));
        }
    }

    @Override
    public VectorListBrowser browse() {
        return new Browser();
    }

    @Override
    public VectorList copy() {
        return new VectorArrayList(builder, (VectorList) this);
    }

    class Browser implements VectorListBrowser {

        private int index = 0;

        @Override
        public VectorListBrowser index(int index) {
            this.index = index;
            return this;
        }

        @Override
        public Vector get() {
            return VectorArrayList.this.get(index);
        }

        @Override
        public int size() {
            return VectorArrayList.super.size();
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
        public double x() {
            return get().x();
        }

        @Override
        public double y() {
            return get().y();
        }

        @Override
        public double z() {
            return get().z();
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public int getNextIndex() {
            return (index + 1) % size();
        }

        @Override
        public Vector getNext() {
            return VectorArrayList.this.get(getNextIndex());
        }

    }

}
