package dev.jdata.db.sql.ast.statements.table;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLPrimaryKey extends BaseSQLElement {

    private final long primaryKeyword;
    private final long keyKeyword;
    private final ASTSingle<SQLColumnNames> columnNames;

    public SQLPrimaryKey(Context context, long primaryKeyword, long keyKeyword, SQLColumnNames columnNames) {
        super(context);

        this.primaryKeyword = checkIsKeyword(primaryKeyword);
        this.keyKeyword = checkIsKeyword(keyKeyword);
        this.columnNames = makeSingle(columnNames);
    }

    public long getPrimaryKeyword() {
        return primaryKeyword;
    }

    public long getKeyKeyword() {
        return keyKeyword;
    }

    public SQLColumnNames getColumnNames() {
        return columnNames.get();
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(columnNames, recurseMode, iterator);
    }
}
