package dev.jdata.db.sql.ast.conditions;

import java.util.List;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.expression.ExpressionList;
import org.jutils.ast.operator.Operator;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.ast.SQLFreeable;

@Deprecated // currently in use?
class SQLExpressionList extends ExpressionList implements SQLFreeable {

    SQLExpressionList(Context context, List<Operator> operators, List<Expression> expressions) {
        super(context, operators, expressions);
    }

    @Override
    public final void free(SQLAllocator allocator) {

        allocator.freeList(getOperators());
    }
}
