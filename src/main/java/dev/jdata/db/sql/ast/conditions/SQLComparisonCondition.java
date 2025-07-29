package dev.jdata.db.sql.ast.conditions;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.operator.Relational;
import org.jutils.parse.context.Context;

import dev.jdata.db.utils.allocators.AddableListAllocator.AddableList;

@Deprecated
public final class SQLComparisonCondition extends SQLExpressionList implements ISQLCondition {

    public SQLComparisonCondition(Context context, Expression lhs, Relational operator, Expression rhs) {
        super(context, AddableList.of(operator), AddableList.of(lhs, rhs));
    }

    @Override
    public <P, R> R visit(SQLConditionVisitor<P, R> visitor, P parameter) {

        return visitor.onComparison(this, parameter);
    }
}
