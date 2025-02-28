package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLObjectColumnName extends BaseSQLElement {

    private final ASTSingle<SQLObjectName> objectName;
    private final long columnName;

    public SQLObjectColumnName(Context context, long columnName) {
        super(context);

        this.objectName = null;
        this.columnName = checkIsString(columnName);
    }

    public SQLObjectColumnName(Context context, SQLObjectName objectName, long columnName) {
        super(context);

        this.objectName = makeSingle(objectName);
        this.columnName = checkIsString(columnName);
    }

    public SQLObjectName getObjectName() {

        return safeGet(objectName);
    }

    public long getColumnName() {
        return columnName;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(objectName, recurseMode, iterator);
    }
}
