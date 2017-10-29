package net.nowina.cadmelia.script;

public class Iteration {

    private String variable;

    private Expression iterableDef;

    public Iteration(String variable, Expression iterableDef) {
        this.variable = variable;
        this.iterableDef = iterableDef;
    }

    public Expression getIterableDef() {
        return iterableDef;
    }

    public String getVariable() {
        return variable;
    }

}
