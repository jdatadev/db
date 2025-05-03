package dev.jdata.db.sql.ast.statements.table;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.utils.adt.arrays.LongArray;

public final class SQLDropConstraintOperation extends SQLAlterTableConstraintOperation {

    private final LongArray names;

    public SQLDropConstraintOperation(Context context, long dropKeyword, long constraintKeyword, LongArray names) {
        super(context, dropKeyword, constraintKeyword);

        this.names = Objects.requireNonNull(names);
    }

    @Override
    public <T, R> R visit(SQLAlterTableOperationVisitor<T, R> visitor, T parameter) {

        return visitor.onDropConstraint(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
