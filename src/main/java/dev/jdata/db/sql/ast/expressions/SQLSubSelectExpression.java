package dev.jdata.db.sql.ast.expressions;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

public final class SQLSubSelectExpression extends BaseSQLExpression {

    private final ASTSingle<SQLSubSelectStatement> selectStatement;

    public SQLSubSelectExpression(Context context, SQLSubSelectStatement subSelectStatement) {
        super(context);

        this.selectStatement = makeSingle(subSelectStatement);
    }

    public SQLSubSelectStatement getSelectStatement() {
        return selectStatement.get();
    }

    @Override
    public <P, R, E extends Exception> R visitSQLExpression(SQLExpressionVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onSubSelect(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(selectStatement, recurseMode, iterator);
    }
}
