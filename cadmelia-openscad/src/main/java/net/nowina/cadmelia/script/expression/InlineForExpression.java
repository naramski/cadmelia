package net.nowina.cadmelia.script.expression;

import net.nowina.cadmelia.script.Expression;
import net.nowina.cadmelia.script.IterableDef;
import net.nowina.cadmelia.script.ScriptContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InlineForExpression extends IterableDef {

    private final String variableName;

    private final IterableDef iterableDef;

    private final Expression expression;

    public InlineForExpression(String variableName, IterableDef iterableDef, Expression expression) {
        this.variableName = variableName;
        this.iterableDef = iterableDef;
        this.expression = expression;
    }

    @Override
    protected List<Object> doEvaluation(ScriptContext context) {

        List<Object> list = new ArrayList<>();
        for(Expression var : iterableDef.iterable(context)) {
            Object value = var._evaluate(context);
            ScriptContext childContext = new ScriptContext(context);
            childContext.defineVariableValue(variableName, value);
            list.add(expression.evaluate(childContext));
        }
        return list;
    }

    @Override
    public Iterable<Expression> iterable(ScriptContext ctx) {
        return new IterableFor(doEvaluation(ctx));
    }

    class IterableFor implements Iterable<Expression> {

        List<Object> list;

        IterableFor(List<Object> list) {
            this.list = list;
        }

        @Override
        public Iterator<Expression> iterator() {
            return new Iterator<Expression>() {

                int index = 0;

                @Override
                public boolean hasNext() {
                    return index < list.size();
                }

                @Override
                public Expression next() {
                    return new ExpressionElement(list.get(index++));
                }
            };
        }

    }

}
