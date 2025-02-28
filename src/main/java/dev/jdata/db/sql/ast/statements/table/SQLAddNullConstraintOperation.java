package dev.jdata.db.sql.ast.statements.table;

import org.jutils.parse.context.Context;

public final class SQLAddNullConstraintOperation extends SQLAlterTableAddConstraintMultiColumnOperation {

    private final long nullKeyword;

    public SQLAddNullConstraintOperation(Context context, long addKeyword, long constraintKeyword, long nullKeyword, SQLColumnNames columnNames, long name) {
        super(context, addKeyword, constraintKeyword, columnNames, name);

        this.nullKeyword = checkIsKeyword(nullKeyword);
    }

    public long getNullKeyword() {
        return nullKeyword;
    }

    @Override
    public <T, R> R visit(SQLAlterTableOperationVisitor<T, R> visitor, T parameter) {

        return visitor.onAddNullConstraint(this, parameter);
    }
}
