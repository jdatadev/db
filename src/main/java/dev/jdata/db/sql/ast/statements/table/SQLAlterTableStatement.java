package dev.jdata.db.sql.ast.statements.table;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.SQLStatementVisitor;

public final class SQLAlterTableStatement extends SQLTableStatement {

    private final ASTSingle<SQLAlterTableOperation> operation;

    public SQLAlterTableStatement(Context context, long operationKeyword, long tableKeyword, long tableName, SQLAlterTableOperation operation) {
        super(context, operationKeyword, tableKeyword, tableName);

        this.operation = makeSingle(operation);
    }

    @Override
    public <T, R> R visit(SQLStatementVisitor<T, R> visitor, T parameter) {

        return visitor.onAlterTable(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(operation, recurseMode, iterator);
    }
}
