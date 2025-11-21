package dev.jdata.db.sql.ast.statements.table;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.ISQLFreeable;
import dev.jdata.db.utils.adt.lists.ICachedLongIndexList;

public final class SQLDropConstraintOperation extends SQLAlterTableConstraintOperation implements ISQLFreeable {

    private ICachedLongIndexList names;

    public SQLDropConstraintOperation(Context context, long dropKeyword, long constraintKeyword, ICachedLongIndexList names) {
        super(context, dropKeyword, constraintKeyword);

        this.names = Objects.requireNonNull(names);
    }

    public ICachedLongIndexList getNames() {
        return names;
    }

    @Override
    public void free(ISQLAllocator allocator) {

        Objects.requireNonNull(allocator);

        allocator.freeLongIndexList(names);

        this.names = null;
    }

    @Override
    public <T, R, E extends Exception> R visit(SQLAlterTableOperationVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onDropConstraint(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
