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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class Expression {

    private static final Logger LOGGER = LoggerFactory.getLogger(Expression.class);

    public abstract Object evaluate(ScriptContext context);

    public String evaluateAsString(ScriptContext context) {
        return (String) evaluate(context);
    }

    public double evaluateAsDouble(ScriptContext context) {
        return (Double) evaluate(context);
    }

    public boolean evaluateAsBoolean(ScriptContext context) {
        return (Boolean) evaluate(context);
    }

    public int evaluateAsInteger(ScriptContext context) {
        return ((Double) evaluate(context)).intValue();
    }

    public Vector evaluateAsVector(ScriptContext context) {
        return (Vector) evaluate(context);
    }

    public static Expression element(double value) {
        return new ExpressionElement<>(value);
    }

    public static Expression elementText(String text) {
        return new ExpressionElement<>(text);
    }

    public static Expression element(String variableName) {
        return new ExpressionVariable(variableName);
    }

    public static Expression element(Expression x, Expression y, Expression z) {
        return new ExpressionVector(x, y, z);
    }

    public static Expression element(Command command) {
        return new ExpressionElement(command);
    }

    public static Expression elementFunction(Command command) {
        return new ExpressionFunction(command);
    }

    public static Expression element(Boolean value) {
        return new ExpressionElement(value);
    }

    public static Expression element(List<Expression> list) {
        return new ExpressionList(list);
    }

    public Expression neg() {
        return new Neg(this);
    }

    public Expression times(Expression arg) {
        return new Times(this, arg);
    }

    public Expression divide(Expression arg) {
        return new Divide(this, arg);
    }

    public Expression plus(Expression arg) {
        return new Plus(this, arg);
    }

    public Expression minus(Expression arg) {
        return new Minus(this, arg);
    }

    public Expression booleanEquals(Expression arg) {
        return new BooleanEquals(this, arg);
    }

    public Expression booleanNotEquals(Expression arg) {
        return new BooleanNotEquals(this, arg);
    }

    public Expression greaterThan(Expression arg) {
        return new CompareDouble(this, arg, ">");
    }

    public Expression lowerThan(Expression arg) {
        return new CompareDouble(this, arg, "<");
    }

    public Expression greaterOrEqualThan(Expression arg) {
        return new CompareDouble(this, arg, ">=");
    }

    public Expression lowerOrEqualThan(Expression arg) {
        return new CompareDouble(this, arg, "<=");
    }

    private static Object evaluateFunction(Command command, ScriptContext context) {
        switch (command.getName()) {
            case "max":
                return evaluateMax(command, context);
            case "sin":
                return evaluateSin(command, context);
            case "cos":
                return evaluateCos(command, context);
            case "round":
                return evaluateRound(command, context);
            case "tan":
                return evaluateTan(command, context);
            case "atan":
                return evaluateAtan(command, context);
            case "rand":
                return Math.random();
            case "rands":
                return new Vector(Math.random(), Math.random(), Math.random());
            case "version":
                return 0.4d;
            default:
                return evaluateFunctionDef(command, context);
        }
    }

    private static Object evaluateFunctionDef(Command command, ScriptContext context) {
        Function fun = context.getFunction(command.getName());
        if(fun == null) {
            throw new UnsupportedOperationException("Function not recognized " + command.getName());
        } else {
            ScriptContext childContext = new ScriptContext(context);

            boolean unamed = command.getUnamedArgCount() == fun.getArgs().size();

            LOGGER.info("Execution of function " + command.getName());
            for (int i = 0; i < fun.getArgs().size(); i++) {
                Parameter param = fun.getArgs().get(i);
                String name = param.getName();

                Object value = param.getDefaultValue();
                LOGGER.info("Received param " + name + " with default value " + value);

                /* If the the argument was provided at the time of the call
                   the default value is overrided
                 */
                Expression argumentValue = unamed ? command.getArg(i) : command.getArg(name);
                if (argumentValue != null) {
                    value = argumentValue.evaluate(context);
                    LOGGER.info("Param " + name + " is overrided by param " + value);
                }

                childContext.defineVariableValue(name, value);
            }

            Expression exp = fun.getExpression();
            return exp.evaluate(childContext);
        }
    }

    private static Object evaluateMax(Command c, ScriptContext context) {
        double max = evaluateDouble(c.getArg(0), context);
        for (int i = 1; i < c.getArgCount(); i++) {
            double value = evaluateDouble(c.getArg(i), context);
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private static Object evaluateSin(Command c, ScriptContext context) {
        double value = evaluateDouble(c.getArg(0), context);
        return Math.sin(Math.toRadians(value));
    }

    private static Object evaluateCos(Command c, ScriptContext context) {
        double value = evaluateDouble(c.getArg(0), context);
        return Math.cos(Math.toRadians(value));
    }

    private static Object evaluateTan(Command c, ScriptContext context) {
        double value = evaluateDouble(c.getArg(0), context);
        return Math.tan(Math.toRadians(value));
    }

    private static Object evaluateAtan(Command c, ScriptContext context) {
        double value = evaluateDouble(c.getArg(0), context);
        return Math.toDegrees(Math.atan(value));
    }

    private static Object evaluateRound(Command c, ScriptContext context) {
        double value = evaluateDouble(c.getArg(0), context);
        return new Double(Math.round(value));
    }

    private static double evaluateDouble(Expression expression, ScriptContext context) {
        Object value = expression.evaluate(context);
        if (value == null) {
            throw new NullPointerException("Null value for " + expression);
        }
        if (value instanceof Command) {
            value = evaluateFunction((Command) value, context);
        }
        if (!(value instanceof Double)) {
            throw new IllegalArgumentException("Cannot evaluate " + value.getClass());
        }
        return (Double) value;
    }

    static class ExpressionElement<T> extends Expression {

        final T element;

        public ExpressionElement(T element) {
            this.element = element;
        }

        @Override
        public Object evaluate(ScriptContext scriptContext) {
            return element;
        }

        @Override
        public String toString() {
            return element.toString();
        }
    }

    static class Neg extends Expression {

        final Expression exp;

        public Neg(Expression e) {
            this.exp = e;
        }

        @Override
        public Object evaluate(ScriptContext context) {
            return -evaluateDouble(exp, context);
        }

        @Override
        public String toString() {
            return "-" + exp.toString();
        }
    }

    static abstract class Binary extends Expression {

        final Expression exp;

        final Expression exp2;

        public Binary(Expression e, Expression exp2) {
            this.exp = e;
            this.exp2 = exp2;
        }

        @Override
        public Object evaluate(ScriptContext context) {
            double d1 = evaluateDouble(exp, context);
            double d2 = evaluateDouble(exp2, context);
            return new Double(operation(d1, d2));
        }

        protected abstract double operation(double d1, double d2);

    }

    static class Times extends Binary {

        public Times(Expression e, Expression exp2) {
            super(e, exp2);
        }

        @Override
        protected double operation(double d1, double d2) {
            return d1 * d2;
        }

        @Override
        public String toString() {
            return "(" + exp.toString() + " * " + exp2.toString() + ")";
        }
    }

    static class Divide extends Binary {

        public Divide(Expression e, Expression exp2) {
            super(e, exp2);
        }

        @Override
        protected double operation(double d1, double d2) {
            return d1 / d2;
        }

        @Override
        public String toString() {
            return "(" + exp.toString() + " / " + exp2.toString() + ")";
        }
    }

    static class Plus extends Binary {

        public Plus(Expression e, Expression exp2) {
            super(e, exp2);
        }

        @Override
        protected double operation(double d1, double d2) {
            return d1 + d2;
        }

        @Override
        public String toString() {
            return "(" + exp.toString() + " + " + exp2.toString() + ")";
        }
    }

    static class Minus extends Binary {

        public Minus(Expression e, Expression exp2) {
            super(e, exp2);
        }

        @Override
        protected double operation(double d1, double d2) {
            return d1 - d2;
        }

        @Override
        public String toString() {
            return "(" + exp.toString() + " - " + exp2.toString() + ")";
        }
    }

    static class BooleanEquals extends Expression {

        final Expression exp;

        final Expression exp2;

        public BooleanEquals(Expression exp, Expression exp2) {
            this.exp = exp;
            this.exp2 = exp2;
        }

        @Override
        public Object evaluate(ScriptContext context) {
            Object value1 = exp.evaluate(context);
            Object value2 = exp2.evaluate(context);
            if (value1 == null) {
                return value2 == null;
            } else {
                // value1 != null
                return value1.equals(value2);
            }
        }

        @Override
        public String toString() {
            return "(" + exp.toString() + " == " + exp2.toString() + ")";
        }

    }

    static class BooleanNotEquals extends BooleanEquals {

        public BooleanNotEquals(Expression exp, Expression exp2) {
            super(exp, exp2);
        }

        @Override
        public Object evaluate(ScriptContext context) {
            Object value1 = exp.evaluate(context);
            Object value2 = exp2.evaluate(context);
            if (value1 == null) {
                return value2 != null;
            } else {
                // value1 != null
                return !value1.equals(value2);
            }
        }

        @Override
        public String toString() {
            return "(" + exp.toString() + " != " + exp2.toString() + ")";
        }

    }

    static class CompareDouble extends Expression {

        final Expression exp;

        final Expression exp2;

        private String operation;

        public CompareDouble(Expression exp, Expression exp2, String operation) {
            this.exp = exp;
            this.exp2 = exp2;
            this.operation = operation;
        }

        @Override
        public Object evaluate(ScriptContext context) {
            double d1 = evaluateDouble(exp, context);
            double d2 = evaluateDouble(exp2, context);
            switch (operation) {
                case ">":
                    return d1 > d2;
                case "<":
                    return d1 < d2;
                case ">=":
                    return d1 >= d2;
                case "<=":
                    return d1 <= d2;
                default:
                    throw new IllegalArgumentException("operation not recognize " + operation);
            }
        }

        @Override
        public String toString() {
            return "(" + exp.toString() + " == " + exp2.toString() + ")";
        }

    }

    static class ExpressionVector extends Expression {

        final Expression x;

        final Expression y;

        final Expression z;

        public ExpressionVector(Expression x, Expression y, Expression z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public Vector evaluate(ScriptContext context) {
            Double valX = (Double) x.evaluate(context);
            Double valY = (Double) y.evaluate(context);
            if (z != null) {
                Double valZ = (Double) z.evaluate(context);
                return new Vector(valX, valY, valZ);
            } else {
                return new Vector(valX, valY);
            }
        }

        @Override
        public String toString() {
            return "[" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    ']';
        }

    }

    static class ExpressionList extends ExpressionElement<List<ExpressionVector>> {

        public ExpressionList(List element) {
            super(element);
        }

        @Override
        public Object evaluate(ScriptContext scriptContext) {
            List<Vector> list = new ArrayList<>();
            for (ExpressionVector exp : element) {
                list.add(exp.evaluate(scriptContext));
            }
            return list;
        }

        @Override
        public String toString() {
            return "[ " + element + " ]";
        }
    }

    static class ExpressionVariable extends ExpressionElement<String> {

        public ExpressionVariable(String element) {
            super(element);
        }

        @Override
        public Object evaluate(ScriptContext scriptContext) {
            Object value = scriptContext.getVariableValue(element);
            if (value == null) {
                throw new NullPointerException("Element " + element + " undefined");
            }
            while (value instanceof Command) {
                value = evaluateFunction((Command) value, scriptContext);
            }
            return value;
        }

        @Override
        public String toString() {
            return element;
        }
    }

    static class ExpressionFunction extends ExpressionElement<Command> {

        public ExpressionFunction(Command element) {
            super(element);
        }

        @Override
        public Object evaluate(ScriptContext scriptContext) {
            Object value = element;
            while (value instanceof Command) {
                value = evaluateFunction((Command) value, scriptContext);
            }
            return value;
        }

        @Override
        public String toString() {
            return element.toString();
        }
    }

}
