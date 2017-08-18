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

public interface Construction {

    boolean isShape();

    boolean isSolid();

    Construction union(Construction other);

    Construction difference(Construction other);

    Construction intersection(Construction other);

    Construction rotate(Vector angle);

    default Construction rotate(double x, double y, double z) {
        return rotate(new Vector(x, y, z));
    }

    Construction translate(Vector vector);

    default Construction translate(double x, double y, double z) {
        return translate(new Vector(x, y, z));
    }

    Construction scale(Vector scale);

    default Construction scale(double x, double y, double z) {
        return scale(new Vector(x, y, z));
    }

}
