package dev.jdata.db.sql.ast.expressions;

import org.jutils.ast.objects.expression.CustomExpression;
import org.jutils.ast.objects.typereference.TypeReference;
import org.jutils.language.common.names.IArrayOfLongsAllocator;
import org.jutils.parse.context.Context;

public abstract class BaseSQLExpression extends CustomExpression {

    public abstract <P, R, E extends Exception> R visitSQLExpression(SQLExpressionVisitor<P, R, E> visitor, P parameter) throws E;

    BaseSQLExpression(Context context) {
        super(context);
    }

    @Override
    public final TypeReference getType(IArrayOfLongsAllocator allocator) {

        throw new UnsupportedOperationException();
    }
}
