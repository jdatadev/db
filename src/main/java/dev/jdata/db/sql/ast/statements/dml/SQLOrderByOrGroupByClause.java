package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.ast.objects.list.IListGetters;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;
import dev.jdata.db.sql.ast.clauses.SQLClause;

public abstract class SQLOrderByOrGroupByClause<T extends BaseSQLElement> extends SQLClause {

    private final ASTList<T> items;

    SQLOrderByOrGroupByClause(Context context, IListGetters<T> items) {
        super(context);

        this.items = makeList(items);
    }

    public final ASTList<T> getItems() {
        return items;
    }

    @Override
    protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(items, recurseMode, iterator);
    }
}
