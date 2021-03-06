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
package net.nowina.cadmelia.math;

import net.nowina.cadmelia.construction.Vector;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import java.text.NumberFormat;

public class Transformation {

    private Matrix4d matrix;

    private Transformation() {
        matrix = new Matrix4d();
        matrix.setIdentity();
    }

    public static Transformation rotation(double a, Vector u) {

        double cos = Math.cos(a);
        double sin = Math.sin(a);

        Transformation tx = new Transformation();
        tx.matrix.set(new double[] {
            cos + u.x() * u.x() * (1 - cos),
            u.x() * u.y() * (1 - cos) - u.z() * sin,
            u.x() * u.z() * (1 - cos) + u.y() * sin,
            0,
            u.y() * u.x() * (1 - cos) + u.z() * sin,
            cos + u.y() * u.y() * (1 - cos),
            u.y() * u.z() * (1 - cos) - u.x() * sin,
            0,
            u.z() * u.x() * (1 - cos) - u.y() * sin,
            u.z() * u.y() * (1 - cos) + u.x() * sin,
            cos + u.z() * u.z() * (1 - cos),
            0, 0, 0, 0, 1
        });
        return tx;

    }

    public static Transformation unity() {
        return new Transformation();
    }

    public Transformation rotX(double degrees) {
        double radians = -degrees * Math.PI / 180;
        Matrix4d m = new Matrix4d();
        m.rotX(radians);
        this.matrix.mul(m);
        return this;
    }

    public Transformation rotY(double degrees) {
        double radians = -degrees * Math.PI / 180;
        Matrix4d m = new Matrix4d();
        m.rotY(radians);
        this.matrix.mul(m);
        return this;
    }

    public Transformation rotZ(double degrees) {
        double radians = -degrees * Math.PI / 180;
        Matrix4d m = new Matrix4d();
        m.rotZ(radians);
        this.matrix.mul(m);
        return this;
    }

    public Transformation rot(double x, double y, double z) {
        return rotZ(z).rotY(y).rotX(x);
    }

    public Transformation rot(Vector vec) {
        return rot(vec.x(),vec.y(),vec.z());
    }

    public Transformation translate(Vector vec) {
        return translate(vec.x(), vec.y(), vec.z());
    }

    public Transformation translate(double x, double y, double z) {
        Matrix4d m = new Matrix4d();
        m.setIdentity();
        m.setTranslation(new Vector3d(x, y, z));
        this.matrix.mul(m);
        return this;
    }

    public Transformation translateX(double value) {
        return translate(value, 0, 0);
    }

    public Transformation translateY(double value) {
        return translate(0, value, 0);
    }

    public Transformation translateZ(double value) {
        return translate(0, 0, value);
    }

    public Transformation mirror(Vector normal, double d) {

        double a = normal.x();
        double b = normal.y();
        double c = normal.z();

        double elements[] = {
                1 - 2 * a * a, -2 * a * b, -2 * a * c, -2 * a * d,
                -2 * a * b, 1 - 2 * b * b, -2 * b * c, -2 * b * d,
                -2 * a * c, -2 * b * c, 1 - 2 * c * c, -2 * c * d,
                0, 0, 0, 1
        };
        this.matrix.mul(new Matrix4d(elements));
        return this;
    }

    public Transformation scale(Vector vec) {
        return scale(vec.x(), vec.y(), vec.z());
    }

    public Transformation scale(double x, double y, double z) {

        Matrix4d m = new Matrix4d(new double[]{x, 0, 0, 0, 0, y, 0, 0, 0, 0, z, 0, 0, 0, 0, 1});
        this.matrix.mul(m);
        return this;
    }

    public Transformation scale(double s) {
        return scale(s, s, s);
    }

    public Transformation scaleX(double s) {
        return scale(s, 1, 1);
    }

    public Transformation scaleY(double s) {
        return scale(1, s, 1);
    }

    public Transformation scaleZ(double s) {
        return scale(1, 1, s);
    }

    public Vector transform(Vector vec) {

        double x = vec.x();
        double y = vec.y();
        double z = vec.z();

        double tx = transformX(x, y, z);
        double ty = transformY(x, y, z);
        double tz = transformZ(x, y, z);

        return new Vector(tx, ty, tz);
    }

    public double transformX(double x, double y, double z) {
        return matrix.m00 * x + matrix.m01 * y + matrix.m02 * z + matrix.m03;
    }

    public double transformY(double x, double y, double z) {
        return matrix.m10 * x + matrix.m11 * y + matrix.m12 * z + matrix.m13;
    }

    public double transformZ(double x, double y, double z) {
        return matrix.m20 * x + matrix.m21 * y + matrix.m22 * z + matrix.m23;
    }

    public double matrixDeterminant() {
        return matrix.determinant();
    }

    public Transformation apply(Transformation t) {
        this.matrix.mul(t.matrix);
        return this;
    }

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        return "[ " + nf.format(matrix.m00) + ", " + nf.format(matrix.m01) + ", " + nf.format(matrix.m02) + ", " + nf.format(matrix.m03) + " ] [" +
                nf.format(matrix.m10) + ", " + nf.format(matrix.m11) + ", " + nf.format(matrix.m12) + ", " + nf.format(matrix.m13) + "] [" +
                nf.format(matrix.m20) + ", " + nf.format(matrix.m21) + ", " + nf.format(matrix.m22) + ", " + nf.format(matrix.m23) + "] [" +
                nf.format(matrix.m30) + ", " + nf.format(matrix.m31) + ", " + nf.format(matrix.m32) + ", " + nf.format(matrix.m33) + "]";

    }

}
