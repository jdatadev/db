package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.clauses.SQLWhereClause;
import dev.jdata.db.sql.ast.statements.SQLStatementVisitor;
import dev.jdata.db.sql.ast.statements.trigger.SQLTriggeredStatement;

public final class SQLDeleteStatement extends SQLDMLStatement implements SQLTriggeredStatement {

    private final long deleteKeyword;
    private final long fromKeyword;
    private final ASTSingle<SQLObjectName> objectName;
    private final long asKeyword;
    private final long alias;
    private final ASTSingle<SQLWhereClause> whereClause;

    public SQLDeleteStatement(Context context, long deleteKeyword, long fromKeyword, SQLObjectName objectName, long asKeyword, long alias, SQLWhereClause whereClause) {
        super(context);

        checkIsKeyword(deleteKeyword);
        checkIsKeyword(fromKeyword);
        checkIsKeywordOrNoKeyword(asKeyword);
        checkIsKeywordOrNoKeyword(alias);

        checkIsKeywordAndStringOrNot(asKeyword, alias);

        this.deleteKeyword = deleteKeyword;
        this.fromKeyword = fromKeyword;
        this.objectName = makeSingle(objectName);
        this.asKeyword = asKeyword;
        this.alias = alias;
        this.whereClause = safeMakeSingle(whereClause);
    }

    public long getDeleteKeyword() {
        return deleteKeyword;
    }

    public long getFromKeyword() {
        return fromKeyword;
    }

    public SQLObjectName getObjectName() {
        return objectName.get();
    }

    public long getAsKeyword() {
        return asKeyword;
    }

    public long getAlias() {
        return alias;
    }

    public SQLWhereClause getWhereClause() {

        return whereClause != null ? whereClause.get() : null;
    }

    @Override
    public <T, R> R visit(SQLStatementVisitor<T, R> visitor, T parameter) {

        return visitor.onDelete(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        if (whereClause != null) {

            doIterate(whereClause, recurseMode, iterator);
        }
    }
}
