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

import net.nowina.cadmelia.construction.Vector;
import net.nowina.cadmelia.script.expression.*;

import java.util.List;

public abstract class Expression {

    public final Literal evaluate(ScriptContext context) {
        Object value = _evaluate(context);
        return new Literal(value);
    }

    public final Object _evaluate(ScriptContext context) {
        Object value = doEvaluation(context);
        while (value instanceof Expression) {
            value = ((Expression) value).doEvaluation(context);
        }
        if(value == null) {
            throw new NullPointerException("Cannot evaluate a null value");
        }
        return value;
    }

    protected abstract Object doEvaluation(ScriptContext context);

    public boolean evaluateAsBoolean(ScriptContext context) {
        return (Boolean) _evaluate(context);
    }

    public String evaluateAsString(ScriptContext context) {
        return (String) _evaluate(context);
    }

    public Double evaluateAsDouble(ScriptContext context) {
        Object value = _evaluate(context);
        if(value instanceof Integer) {
            return new Double((Integer)value);
        } else {
            return (Double) value;
        }
    }

    public int evaluateAsInteger(ScriptContext context) {
        return ((Double) _evaluate(context)).intValue();
    }

    public List evaluateAsList(ScriptContext context) {
        return ((List) _evaluate(context));
    }

    public Vector evaluateAsVector(ScriptContext context) {
        List<Double> list = evaluateAsList(context);
        if(list.size() == 2) {
            double x = list.get(0);
            double y = list.get(1);
            return new Vector(x, y);
        }
        if(list.size() == 3) {
            double x = list.get(0);
            double y = list.get(1);
            double z = list.get(2);
            return new Vector(x, y, z);
        }
        throw new IllegalStateException("Cannot make a vector out of " + list.size() + " elements");
    }

    public Expression neg() {
        return new NegExpression(this);
    }

    public Expression times(Expression arg) {
        return new BinaryExpression(this, arg, (x,y) -> x*y);
    }

    public Expression divide(Expression arg) {
        return new BinaryExpression(this, arg, (x,y) -> x/y);
    }

    public Expression plus(Expression arg) {
        return new BinaryExpression(this, arg, (x,y) -> x+y);
    }

    public Expression minus(Expression arg) {
        return new BinaryExpression(this, arg, (x,y) -> x-y);
    }

    public Expression modulo(Expression arg) {
        return new BinaryExpression(this, arg, (x,y) -> x%y);
    }

    public Expression booleanEquals(Expression arg) {
        return new ComparisonExpression(this, arg, (x,y) -> x!=null ? x.equals(y) : y==null);
    }

    public Expression booleanNotEquals(Expression arg) {
        return new ComparisonExpression(this, arg, (x,y) -> x!=null ? !x.equals(y) : y!=null);
    }

    public Expression greaterThan(Expression arg) {
        return new ComparisonExpression(this, arg, (x,y) -> (double)x>(double)y);
    }

    public Expression lowerThan(Expression arg) {
        return new ComparisonExpression(this, arg, (x,y) -> (double)x<(double)y);
    }

    public Expression greaterOrEqualThan(Expression arg) {
        return new ComparisonExpression(this, arg, (x,y) -> (double)x>=(double)y);
    }

    public Expression lowerOrEqualThan(Expression arg) {
        return new ComparisonExpression(this, arg, (x,y) -> (double)x<=(double)y);
    }

}
