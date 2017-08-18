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

import net.nowina.cadmelia.construction.Construction;

import java.io.PrintWriter;

public class Difference extends ModelConstruction {

    private ModelConstruction source;

    private Union union;

    public Difference(ModelConstruction source) {
        this.source = source;
    }

    public void addElement(Construction construction) {
        if (union == null) {
            union = new Union();
        }
        union.add(construction);
    }

    @Override
    public boolean isSolid() {
        if (source == null) {
            throw new IllegalStateException();
        }
        return source.isSolid();
    }

    @Override
    public boolean isShape() {
        if (source == null) {
            throw new IllegalStateException();
        }
        return source.isShape();
    }

    @Override
    protected void print(PrintWriter writer, String paddingPrefix) {
        writer.print(paddingPrefix);
        if (getTransform() != null) {
            writer.write("Transform{" + getTransform() + "} ");
        }
        writer.println("Difference {");
        String padding = paddingPrefix + "  ";
        source.print(writer, padding);
        if (union != null) {
            for (ModelConstruction c : union.getElements()) {
                c.print(writer, padding);
            }
        }
        writer.print(paddingPrefix);
        writer.println("}");
    }

}
