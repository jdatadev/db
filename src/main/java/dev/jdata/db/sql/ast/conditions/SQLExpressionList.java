package dev.jdata.db.sql.ast.conditions;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.expression.ExpressionList;
import org.jutils.ast.objects.list.IIndexListView;
import org.jutils.ast.operator.Operator;
import org.jutils.parse.context.Context;

@Deprecated // currently in use?
class SQLExpressionList extends ExpressionList {

    SQLExpressionList(Context context, IIndexListView<Operator> operators, IIndexListView<Expression> expressions) {
        super(context, operators, expressions);
    }
}
