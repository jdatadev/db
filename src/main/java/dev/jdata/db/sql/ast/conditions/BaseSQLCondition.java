package dev.jdata.db.sql.ast.conditions;

import org.jutils.ast.objects.expression.CustomExpression;
import org.jutils.ast.objects.typereference.TypeReference;
import org.jutils.language.common.names.IArrayOfLongsAllocator;
import org.jutils.parse.context.Context;

abstract class BaseSQLCondition extends CustomExpression implements SQLCondition {

    BaseSQLCondition(Context context) {
        super(context);
    }

    @Override
    public final TypeReference getType(IArrayOfLongsAllocator allocator) {

        throw new UnsupportedOperationException();
    }
}
