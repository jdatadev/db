package dev.jdata.db.sql.ast.statements.table;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLModifyColumn extends BaseSQLElement {

    private final ASTSingle<SQLTableColumnDefinition> columnDefinition;

    public SQLModifyColumn(Context context, SQLTableColumnDefinition columnDefinition) {
        super(context);

        this.columnDefinition = makeSingle(columnDefinition);
    }

    public SQLTableColumnDefinition getColumnDefinition() {
        return columnDefinition.get();
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(columnDefinition, recurseMode, iterator);
    }
}
