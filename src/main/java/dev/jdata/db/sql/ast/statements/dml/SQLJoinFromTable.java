package dev.jdata.db.sql.ast.statements.dml;

import java.util.List;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

public final class SQLJoinFromTable extends SQLFromTable {

    private final ASTSingle<SQLObjectNameAndAlias> fromTable;
    private final long onKeyword;
    private final ASTSingle<Expression> onCondition;
    private final ASTList<SQLTableJoin> join;

    public SQLJoinFromTable(Context context, SQLObjectNameAndAlias fromTable, long onKeyword, Expression onCondition, List<SQLTableJoin> join) {
        super(context);

        this.fromTable = makeSingle(fromTable);
        this.onKeyword = checkIsKeyword(onKeyword);
        this.onCondition = makeSingle(onCondition);
        this.join = makeList(join);
    }

    public SQLObjectNameAndAlias getFromTable() {
        return fromTable.get();
    }

    public long getOnKeyword() {
        return onKeyword;
    }

    public Expression getOnCondition() {
        return onCondition.get();
    }

    public ASTList<SQLTableJoin> getJoin() {
        return join;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(fromTable, recurseMode, iterator);
        doIterate(join, recurseMode, iterator);
    }
}
