package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLColumnValueUpdateValue extends BaseSQLElement {

    private final long columnName;
    private final ASTSingle<Expression> value;

    public SQLColumnValueUpdateValue(Context context, long columnName, Expression value) {
        super(context);

        this.columnName = checkIsString(columnName);
        this.value = makeSingle(value);
    }

    public long getColumnName() {
        return columnName;
    }

    public Expression getValue() {
        return value.get();
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(value, recurseMode, iterator);
    }
}
