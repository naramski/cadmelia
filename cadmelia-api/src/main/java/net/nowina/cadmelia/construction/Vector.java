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
package net.nowina.cadmelia.construction;

public class Vector {

    public static double EPSILON = 1e-6;

    public static final Vector X = new Vector(1, 0, 0);
    public static final Vector Y = new Vector(0, 1, 0);
    public static final Vector Z = new Vector(0, 0, 1);
    public static final Vector ZERO = new Vector(0, 0, 0);

    double x, y, z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(double x, double y) {
        this(x, y, 0.0d);
    }

    public Vector negated() {
        return new Vector(-x, -y, -z);
    }

    public Vector plus(Vector o) {
        return new Vector(x + o.x, y + o.y, z + o.z);
    }

    public Vector minus(Vector o) {
        return new Vector(x - o.x, y - o.y, z - o.z);
    }

    public Vector times(double f) {
        return new Vector(x * f, y * f, z * f);
    }

    public Vector times(Vector o) {
        return new Vector(x * o.x, y * o.y, z * o.z);
    }

    public double dot(Vector o) {
        return dot(o.x, o.y, o.z);
    }

    public double dot(double x, double y, double z) {
        return this.x * x + this.y * y + this.z * z;
    }

    public Vector interpolate(Vector o, double f) {
        return new Vector(x + (o.x - x) * f, y + (o.y - y) * f, z + (o.z - z) * f);
    }

    public double magnitude() {
        return Math.sqrt(magnitudeSq());
    }

    public double magnitudeSq() {
        return x * x + y * y + z * z;
    }

    public Vector normalized() {
        return this.divided(magnitude());
    }

    public Vector divided(double f) {
        return new Vector(x / f, y / f, z / f);
    }

    public double angle(Vector v) {
        double val = this.dot(v) / (this.magnitude() * v.magnitude());
        return Math.acos(Math.max(Math.min(val, 1), -1)) * 180.0 / Math.PI; // compensate rounding errors
    }

    public Vector crossed(Vector a) {
        return new Vector(
                this.y() * a.z() - this.z() * a.y(),
                this.z() * a.x() - this.x() * a.z(),
                this.x() * a.y() - this.y() * a.x()
        );
    }

    public Vector project(Vector v) {
        double pScale = v.dot(this) / this.magnitudeSq();
        return times(pScale);
    }

    public boolean equalsEnough(Vector o) {
        if (Math.abs(o.x - x) > EPSILON) return false;
        if (Math.abs(o.y - y) > EPSILON) return false;
        if (Math.abs(o.z - z) > EPSILON) return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        if (Double.compare(vector.x, x) != 0) return false;
        if (Double.compare(vector.y, y) != 0) return false;
        return Double.compare(vector.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public double get(int i) {
        switch (i) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
            default:
                throw new IllegalArgumentException("i must be 0,1 or 2");
        }
    }

    @Override
    public String toString() {
        return "[" + x +
                "," + y +
                "," + z +
                ']';
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

}
