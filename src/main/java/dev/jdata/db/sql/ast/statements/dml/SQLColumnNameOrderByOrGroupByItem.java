package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

public final class SQLColumnNameOrderByOrGroupByItem extends SQLOrderByOrGroupByItem {

    private final ASTSingle<SQLObjectColumnName> column;

    public SQLColumnNameOrderByOrGroupByItem(Context context, SQLObjectColumnName column) {
        super(context);

        this.column = makeSingle(column);
    }

    public SQLObjectColumnName getColumn() {
        return column.get();
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(column, recurseMode, iterator);
    }
}
