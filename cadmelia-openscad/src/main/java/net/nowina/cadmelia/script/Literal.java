package net.nowina.cadmelia.script;

import net.nowina.cadmelia.construction.Vector;

import java.util.List;

public class Literal {

    private Object value;

    public Literal(Object value) {
        if(value == null) {
            throw new NullPointerException("Value must be defined");
        }
        if(value instanceof Literal) {
            this.value = ((Literal)value).getValue();
        } else {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public Double asDouble() {
        if(isInteger()) {
            return new Double((Integer) value);
        }
        return (Double) value;
    }

    public boolean isDouble() {
        return value instanceof Double;
    }

    public List asList() {
        return (List) value;
    }

    public boolean isList() {
        return value instanceof List;
    }

    public Integer asInteger() {
        if(isDouble()) {
            return ((Double) value).intValue();
        }
        return (Integer) value;
    }

    public boolean isInteger() {
        return value instanceof Integer;
    }

    public Boolean asBoolean() {
        return (Boolean) value;
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    public String asString() {
        return (String) value;
    }

    public boolean isString() {
        return value instanceof String;
    }

    public Vector asVector() {
        if(asList().size() == 3) {
            return new Vector((Double) asList().get(0), (Double) asList().get(1), (Double) asList().get(2));
        }

        if(asList().size() == 2) {
            return new Vector((Double)asList().get(0), (Double)asList().get(1));
        }

        throw new IllegalArgumentException("Cannot convert list of " + asList().size() + " elements in a Vector");
    }

    public boolean isVector() {
        return isList() && (asList().size() ==2 || asList().size() ==3);
    }

    public Object getValue() {
        return value;
    }

}
