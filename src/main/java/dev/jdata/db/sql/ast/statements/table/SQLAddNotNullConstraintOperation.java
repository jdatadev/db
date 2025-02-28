package dev.jdata.db.sql.ast.statements.table;

import org.jutils.parse.context.Context;

public final class SQLAddNotNullConstraintOperation extends SQLAlterTableAddConstraintMultiColumnOperation {

    private final long notKeyword;
    private final long nullKeyword;

    public SQLAddNotNullConstraintOperation(Context context, long addKeyword, long constraintKeyword, long notKeyword, long nullKeyword, SQLColumnNames columnNames, long name) {
        super(context, addKeyword, constraintKeyword, columnNames, name);

        this.notKeyword = checkIsKeyword(notKeyword);
        this.nullKeyword = checkIsKeyword(nullKeyword);
    }

    public long getNotKeyword() {
        return notKeyword;
    }

    public long getNullKeyword() {
        return nullKeyword;
    }

    @Override
    public <T, R> R visit(SQLAlterTableOperationVisitor<T, R> visitor, T parameter) {

        return visitor.onAddNotNullConstraint(this, parameter);
    }
}
