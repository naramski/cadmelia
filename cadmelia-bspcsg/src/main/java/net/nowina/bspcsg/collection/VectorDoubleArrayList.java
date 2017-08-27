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

import net.nowina.cadmelia.Transformation;
import net.nowina.cadmelia.construction.Vector;

public class VectorDoubleArrayList implements VectorList {

    private int itemsContained = 0;

    private double[] array;

    public VectorDoubleArrayList() {
        array = new double[30];
    }

    private VectorDoubleArrayList(VectorList list) {
        array = new double[list.size() * 3];
        if (list instanceof VectorDoubleArrayList) {
            VectorDoubleArrayList other = (VectorDoubleArrayList) list;
            System.arraycopy(other.array, 0, array, 0, other.size() * 3);
            itemsContained = other.size();
        } else {
            throw new IllegalArgumentException(list.getClass().toString());
        }
    }

    public VectorDoubleArrayList(Vector... vectors) {
        array = new double[vectors.length * 3];
        for (int i = 0; i < vectors.length; i++) {
            addVector(vectors[i]);
        }
    }

    private int xIndex(int vertexNumber) {
        return vertexNumber * 3;
    }

    private int yIndex(int vertexNumber) {
        return vertexNumber * 3 + 1;
    }

    private int zIndex(int vertexNumber) {
        return vertexNumber * 3 + 2;
    }

    @Override
    public int size() {
        return itemsContained;
    }

    @Override
    public Vector getVector(int i) {
        if (i >= itemsContained) {
            throw new ArrayIndexOutOfBoundsException(i + " > " + itemsContained);
        }
        return new Vector(array[xIndex(i)], array[yIndex(i)], array[zIndex(i)]);
    }

    @Override
    public void addVector(VectorListBrowser browser) {
        double x = browser.x();
        double y = browser.y();
        double z = browser.z();
        addVector(x, y, z);
    }

    @Override
    public void reverse() {
        for (int i = 0; i < size() / 2; i++) {
            double x = array[xIndex(i)];
            double y = array[yIndex(i)];
            double z = array[zIndex(i)];
            array[xIndex(i)] = array[xIndex(size() - 1 - i)];
            array[yIndex(i)] = array[yIndex(size() - 1 - i)];
            array[zIndex(i)] = array[zIndex(size() - 1 - i)];
            array[xIndex(size() - 1 - i)] = x;
            array[yIndex(size() - 1 - i)] = y;
            array[zIndex(size() - 1 - i)] = z;
        }
    }

    @Override
    public void addVector(Vector v) {
        double x = v.x();
        double y = v.y();
        double z = v.z();
        addVector(x, y, z);
    }

    private void addVector(double x, double y, double z) {
        if (xIndex(itemsContained) == array.length) {
            double[] newArray = new double[itemsContained * 3 * 2];
            System.arraycopy(array, 0, newArray, 0, array.length);
            this.array = newArray;
        }
        array[xIndex(itemsContained)] = x;
        array[yIndex(itemsContained)] = y;
        array[zIndex(itemsContained)] = z;
        itemsContained++;
    }

    @Override
    public void applyTransformation(Transformation transformation) {
        for (int i = 0; i < size(); i++) {
            double x = array[xIndex(i)];
            double y = array[yIndex(i)];
            double z = array[zIndex(i)];
            array[xIndex(i)] = transformation.transformX(x, y, z);
            array[yIndex(i)] = transformation.transformY(x, y, z);
            array[zIndex(i)] = transformation.transformZ(x, y, z);
        }
    }

    @Override
    public VectorListBrowser browse() {
        return new Browser();
    }

    @Override
    public VectorList copy() {
        return new VectorDoubleArrayList(this);
    }

    class Browser implements VectorListBrowser {

        int index = 0;

        @Override
        public VectorListBrowser index(int index) {
            this.index = index;
            return this;
        }

        @Override
        public int size() {
            return VectorDoubleArrayList.this.size();
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
        public Vector get() {
            return VectorDoubleArrayList.this.getVector(index);
        }

        @Override
        public double x() {
            return VectorDoubleArrayList.this.array[xIndex(index)];
        }

        @Override
        public double y() {
            return VectorDoubleArrayList.this.array[yIndex(index)];
        }

        @Override
        public double z() {
            return VectorDoubleArrayList.this.array[zIndex(index)];
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
            double[] array = VectorDoubleArrayList.this.array;
            int nextIndex = getNextIndex();
            return new Vector(array[xIndex(nextIndex)], array[yIndex(nextIndex)], array[zIndex(nextIndex)]);
        }

    }

}
