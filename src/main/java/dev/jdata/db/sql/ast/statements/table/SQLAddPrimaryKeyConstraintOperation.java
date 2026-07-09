package dev.jdata.db.sql.ast.statements.table;

import org.jutils.parse.context.Context;

public final class SQLAddPrimaryKeyConstraintOperation extends SQLAlterTableAddConstraintMultiColumnOperation {

    private final long primaryKeyword;
    private final long keyKeyword;

    public SQLAddPrimaryKeyConstraintOperation(Context context, long addKeyword, long constraintKeyword, long primaryKeyword, long keyKeyword, SQLColumnNames columnNames,
            long name) {
        super(context, addKeyword, constraintKeyword, columnNames, name);

        this.primaryKeyword = checkIsKeyword(primaryKeyword);
        this.keyKeyword = checkIsKeyword(keyKeyword);
    }

    public long getPrimaryKeyword() {
        return primaryKeyword;
    }

    public long getKeyKeyword() {
        return keyKeyword;
    }

    @Override
    public <P, R, E extends Exception> R visit(SQLAlterTableOperationVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onAddPrimaryKeyConstraint(this, parameter);
    }
}
