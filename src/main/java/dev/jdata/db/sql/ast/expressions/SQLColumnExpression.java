package dev.jdata.db.sql.ast.expressions;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.dml.SQLObjectOrAlias;

public final class SQLColumnExpression extends BaseSQLExpression {

    private final ASTSingle<SQLObjectOrAlias> objectOrAlias;
    private final long columnName;

    SQLColumnExpression(Context context, SQLObjectOrAlias objectOrAlias, long columnName) {
        super(context);

        this.objectOrAlias = makeSingle(objectOrAlias);
        this.columnName = checkIsString(columnName);
    }

    public SQLObjectOrAlias getObjectOrAlias() {
        return objectOrAlias.get();
    }

    public long getColumnName() {
        return columnName;
    }

    @Override
    public <P, R> R visit(SQLExpressionVisitor<P, R> visitor, P parameter) {

        return visitor.onColumn(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(objectOrAlias, recurseMode, iterator);
    }
}
