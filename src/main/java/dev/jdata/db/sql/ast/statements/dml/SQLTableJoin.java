package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLTableJoin extends BaseSQLElement {

    private final ASTSingle<SQLJoin> join;
    private final ASTSingle<SQLJoinFromTable> table;

    public SQLTableJoin(Context context, SQLJoin join, SQLJoinFromTable table) {
        super(context);

        this.join = makeSingle(join);
        this.table = makeSingle(table);
    }

    public SQLJoin getJoin() {
        return join.get();
    }

    public SQLJoinFromTable getTable() {
        return table.get();
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(join, recurseMode, iterator);
        doIterate(table, recurseMode, iterator);
    }
}
