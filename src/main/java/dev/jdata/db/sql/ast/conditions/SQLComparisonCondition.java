package dev.jdata.db.sql.ast.conditions;

import java.util.Arrays;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.operator.Relational;
import org.jutils.parse.context.Context;

@Deprecated
public final class SQLComparisonCondition extends SQLExpressionList implements SQLCondition {

    public SQLComparisonCondition(Context context, Expression lhs, Relational operator, Expression rhs) {
        super(context, Arrays.asList(operator),  Arrays.asList(lhs, rhs));
    }

    @Override
    public <P, R> R visit(SQLConditionVisitor<P, R> visitor, P parameter) {

        return visitor.onComparison(this, parameter);
    }
}
