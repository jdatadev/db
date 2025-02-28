package dev.jdata.db.sql.ast.statements.table;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLAddColumnDefinition extends BaseSQLElement {

    private final ASTSingle<SQLTableColumnDefinition> columnDefinition;
    private final long beforeKeyword;
    private final long beforeColumnName;

    public SQLAddColumnDefinition(Context context, SQLTableColumnDefinition columnDefinition, long beforeKeyword, long beforeColumnName) {
        super(context);

        this.columnDefinition = makeSingle(columnDefinition);
        this.beforeKeyword = checkIsKeyword(beforeKeyword);
        this.beforeColumnName = checkIsString(beforeColumnName);
    }

    public SQLTableColumnDefinition getColumnDefinition() {
        return columnDefinition.get();
    }

    public long getBeforeKeyword() {
        return beforeKeyword;
    }

    public long getBeforeColumnName() {
        return beforeColumnName;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(columnDefinition, recurseMode, iterator);
    }
}
