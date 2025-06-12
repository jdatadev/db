package dev.jdata.db.sql.ast.conditions;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.expression.ExpressionList;
import org.jutils.ast.objects.list.IIndexListGetters;
import org.jutils.ast.operator.Operator;
import org.jutils.parse.context.Context;

@Deprecated // currently in use?
class SQLExpressionList extends ExpressionList {

    SQLExpressionList(Context context, IIndexListGetters<Operator> operators, IIndexListGetters<Expression> expressions) {
        super(context, operators, expressions);
    }
}
