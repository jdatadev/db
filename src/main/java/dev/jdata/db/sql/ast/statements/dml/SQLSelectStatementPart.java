package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;
import dev.jdata.db.sql.ast.clauses.SQLWhereClause;

public final class SQLSelectStatementPart extends BaseSQLElement {

    private final long selectKeyword;
    private final ASTSingle<SQLProjectionClause> projectionClause;
    private final ASTSingle<SQLFromClause> fromClause;
    private final ASTSingle<SQLWhereClause> whereClause;
    private final ASTSingle<SQLGroupByClause> groupByClause;
    private final ASTSingle<SQLHavingClause> havingClause;
    private final ASTSingle<SQLOrderByClause> orderByClause;

    public SQLSelectStatementPart(Context context, long selectKeyword, SQLProjectionClause projectionClause, SQLFromClause fromClause, SQLWhereClause whereClause,
            SQLGroupByClause groupByClause, SQLHavingClause havingClause, SQLOrderByClause orderByClause) {
        super(context);

        this.selectKeyword = checkIsKeyword(selectKeyword);
        this.projectionClause = makeSingle(projectionClause);
        this.fromClause = makeSingle(fromClause);
        this.whereClause = safeMakeSingle(whereClause);
        this.groupByClause = safeMakeSingle(groupByClause);
        this.havingClause = safeMakeSingle(havingClause);
        this.orderByClause = safeMakeSingle(orderByClause);
    }

    public long getSelectKeyword() {
        return selectKeyword;
    }

    public SQLProjectionClause getProjectionClause() {
        return projectionClause.get();
    }

    public SQLFromClause getFromClause() {
        return fromClause.get();
    }

    public SQLWhereClause getWhereClause() {

        return safeGet(whereClause);
    }

    public SQLGroupByClause getGroupByClause() {

        return safeGet(groupByClause);
    }

    public SQLHavingClause getHavingClause() {

        return safeGet(havingClause);
    }

    public SQLOrderByClause getOrderByClause() {

        return safeGet(orderByClause);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(projectionClause, recurseMode, iterator);
        doIterate(fromClause, recurseMode, iterator);

        if (whereClause != null) {

            doIterate(whereClause, recurseMode, iterator);
        }

        if (groupByClause != null) {

            doIterate(groupByClause, recurseMode, iterator);
        }

        if (havingClause != null) {

            doIterate(havingClause, recurseMode, iterator);
        }

        if (orderByClause != null) {

            doIterate(orderByClause, recurseMode, iterator);
        }
    }
}
