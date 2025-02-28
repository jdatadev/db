package dev.jdata.db.sql.ast.statements.dml;

import java.util.List;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.clauses.SQLClause;

public final class SQLProjectionClause extends SQLClause {

    private final ASTList<SQLProjectionItem> items;

    public SQLProjectionClause(Context context, List<SQLProjectionItem> items) {
        super(context);

        this.items = makeList(items);
    }

    public ASTList<SQLProjectionItem> getItems() {
        return items;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(items, recurseMode, iterator);
    }
}
