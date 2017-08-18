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

import net.nowina.cadmelia.construction.Vector;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import java.text.NumberFormat;

public class Transform {

    private final Matrix4d m;

    public Transform() {
        m = new Matrix4d();
        m.setIdentity();
    }

    private Transform(Matrix4d m) {
        this.m = m;
    }

    public void appy(Transform transform) {
        m.mul(transform.m);
    }

    public Vector transform(Vector v) {
        double x = m.m00 * v.x() + m.m01 * v.y() + m.m02 * v.z() + m.m03;
        double y = m.m10 * v.x() + m.m11 * v.y() + m.m12 * v.z() + m.m13;
        double z = m.m20 * v.x() + m.m21 * v.y() + m.m22 * v.z() + m.m23;
        return new Vector(x, y, z);
    }

    /**
     * Copy this transformation
     *
     * @return
     */
    public Transform copy() {
        return new Transform(new Matrix4d(m));
    }

    private double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180.0;
    }

    public Transform rotate(double x, double y, double z) {

        Matrix4d mx = new Matrix4d();
        mx.rotX(degreesToRadians(x));
        this.m.mul(mx);

        Matrix4d my = new Matrix4d();
        my.rotY(degreesToRadians(y));
        this.m.mul(my);

        Matrix4d mz = new Matrix4d();
        mz.rotZ(degreesToRadians(z));
        this.m.mul(mz);

        return this;

    }

    public Transform translate(double x, double y, double z) {
        Matrix4d m = new Matrix4d();
        m.setIdentity();
        m.setTranslation(new Vector3d(x, y, z));

        this.m.mul(m);
        return this;
    }

    public Transform scale(double x, double y, double z) {

        if (x == 0 || y == 0 || z == 0) {
            throw new IllegalArgumentException("scale factor cannot be 0");
        }

        double elements[] = {
                x, 0, 0, 0, 0, y, 0, 0, 0, 0, z, 0, 0, 0, 0, 1
        };
        Matrix4d m = new Matrix4d(elements);
        this.m.mul(m);
        return this;
    }

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        return "[ " + nf.format(this.m.m00) + ", " + nf.format(this.m.m01) + ", " + nf.format(this.m.m02) + ", " + nf.format(this.m.m03) + " ] [" +
                nf.format(this.m.m10) + ", " + nf.format(this.m.m11) + ", " + nf.format(this.m.m12) + ", " + nf.format(this.m.m13) + "] [" +
                nf.format(this.m.m20) + ", " + nf.format(this.m.m21) + ", " + nf.format(this.m.m22) + ", " + nf.format(this.m.m23) + "] [" +
                nf.format(this.m.m30) + ", " + nf.format(this.m.m31) + ", " + nf.format(this.m.m32) + ", " + nf.format(this.m.m33) + "]";

    }

}
