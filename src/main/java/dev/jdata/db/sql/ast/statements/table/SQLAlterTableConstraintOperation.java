package dev.jdata.db.sql.ast.statements.table;

import org.jutils.parse.context.Context;

abstract class SQLAlterTableConstraintOperation extends SQLAlterTableOperation {

    private final long operationKeyword;
    private final long constraintKeyword;

    SQLAlterTableConstraintOperation(Context context, long operationKeyword, long constraintKeyword) {
        super(context);

        this.operationKeyword = checkIsKeyword(operationKeyword);
        this.constraintKeyword = checkIsKeyword(constraintKeyword);
    }

    public final long getOperationKeyword() {
        return operationKeyword;
    }

    public final long getConstraintKeyword() {
        return constraintKeyword;
    }
}
