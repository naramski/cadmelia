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
package net.nowina.cadmelia.script;

import java.util.Iterator;

public class Range implements Iterable<Expression> {

    private final double start;

    private final double end;

    private final double increment;

    public Range(double start, double end, double increment) {
        this.start = start;
        this.end = end;
        this.increment = increment;
    }

    @Override
    public Iterator<Expression> iterator() {
        return new RangeIterator();
    }

    class RangeIterator implements Iterator<Expression> {

        private double value;

        RangeIterator() {
            this.value = start;
        }

        @Override
        public boolean hasNext() {
            return value <= end;
        }

        @Override
        public Expression next() {
            double current = value;
            value += increment;
            return ExpressionBuilder.element(current);
        }

    }

}
