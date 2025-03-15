package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.clauses.SQLWhereClause;
import dev.jdata.db.sql.ast.statements.SQLStatementVisitor;
import dev.jdata.db.sql.ast.statements.trigger.SQLTriggeredStatement;

public final class SQLUpdateStatement extends SQLDMLUpdatingStatement implements SQLTriggeredStatement {

    private final long updateKeyword;
    private final long tableName;
    private final long setKeyword;
    private final ASTSingle<SQLUpdateValues> values;
    private final ASTSingle<SQLWhereClause> whereClause;

    public SQLUpdateStatement(Context context, long updateKeyword, long tableName, long setKeyword, SQLUpdateValues values, SQLWhereClause whereClause) {
        super(context);

        this.updateKeyword = checkIsKeyword(updateKeyword);
        this.tableName = checkIsString(tableName);
        this.setKeyword = checkIsKeyword(setKeyword);
        this.values = makeSingle(values);
        this.whereClause = makeSingle(whereClause);
    }

    public long getUpdateKeyword() {
        return updateKeyword;
    }

    public long getTableName() {
        return tableName;
    }

    public long getSetKeyword() {
        return setKeyword;
    }

    public SQLUpdateValues getValues() {
        return values.get();
    }

    public SQLWhereClause getWhereClause() {
        return whereClause.get();
    }

    @Override
    public <P, R, E extends Exception> R visit(SQLStatementVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onUpdate(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(values, recurseMode, iterator);
        doIterate(whereClause, recurseMode, iterator);
    }
}
