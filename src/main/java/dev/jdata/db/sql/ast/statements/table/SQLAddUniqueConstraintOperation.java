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
    public <P, R, E extends Exception> R visit(SQLAlterTableOperationVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onAddUniqueConstraint(this, parameter);
    }
}
