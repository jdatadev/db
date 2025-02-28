package dev.jdata.db.sql.ast.statements.dml;

import java.util.List;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.clauses.SQLClause;

public final class SQLFromClause extends SQLClause {

    private final ASTList<SQLFromTable> fromTables;

    public SQLFromClause(Context context, List<SQLFromTable> fromTables) {
        super(context);

        this.fromTables = makeNonEmptyList(fromTables);
    }

    public ASTList<SQLFromTable> getFromTables() {
        return fromTables;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(fromTables, recurseMode, iterator);
    }
}
