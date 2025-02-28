package dev.jdata.db.sql.ast.statements.table;

import org.jutils.parse.context.Context;

abstract class SQLAlterTableConstraintNameOperation extends SQLAlterTableConstraintOperation {

    private final long name;

    SQLAlterTableConstraintNameOperation(Context context, long operationKeyword, long constraintKeyword, long name) {
        super(context, operationKeyword, constraintKeyword);

        this.name = checkIsString(name);
    }

    public final long getName() {
        return name;
    }
}
