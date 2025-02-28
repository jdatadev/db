package dev.jdata.db.sql.ast.statements.table;

import org.jutils.parse.context.Context;

public final class SQLAddUniqueConstraintOperation extends SQLAlterTableAddConstraintMultiColumnOperation {

    private final long uniqueKeyword;

    public SQLAddUniqueConstraintOperation(Context context, long addKeyword, long constraintKeyword, long uniqueKeyword, SQLColumnNames columnNames, long name) {
        super(context, addKeyword, constraintKeyword, columnNames, name);

        this.uniqueKeyword = checkIsKeyword(uniqueKeyword);
    }

    public long getUniqueKeyword() {
        return uniqueKeyword;
    }

    @Override
    public <T, R> R visit(SQLAlterTableOperationVisitor<T, R> visitor, T parameter) {

        return visitor.onAddUniqueConstraint(this, parameter);
    }
}
