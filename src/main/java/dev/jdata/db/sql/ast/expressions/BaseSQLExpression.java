package dev.jdata.db.sql.ast.expressions;

import org.jutils.ast.objects.expression.CustomExpression;
import org.jutils.ast.objects.typereference.TypeReference;
import org.jutils.language.common.names.Allocator;
import org.jutils.parse.context.Context;

abstract class BaseSQLExpression extends CustomExpression {

    public abstract <P, R> R visit(SQLExpressionVisitor<P, R> visitor, P parameter);

    BaseSQLExpression(Context context) {
        super(context);
    }

    @Override
    public final TypeReference getType(Allocator allocator) {

        throw new UnsupportedOperationException();
    }
}
