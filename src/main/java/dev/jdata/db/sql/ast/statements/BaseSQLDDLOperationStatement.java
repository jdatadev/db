package dev.jdata.db.sql.ast.statements;

import org.jutils.parse.context.Context;

public abstract class BaseSQLDDLOperationStatement extends BaseSQLStatement {

    private final long operationKeyword;
    private final long objectKeyword;
    private final long name;

    protected BaseSQLDDLOperationStatement(Context context, long operationKeyword, long objectKeyword, long name) {
        super(context);

        this.operationKeyword = checkIsKeyword(operationKeyword);
        this.objectKeyword = checkIsKeyword(objectKeyword);
        this.name = checkIsString(name);
    }

    public final long getOperationKeyword() {
        return operationKeyword;
    }

    public final long getObjectKeyword() {
        return objectKeyword;
    }

    public final long getName() {
        return name;
    }
}
