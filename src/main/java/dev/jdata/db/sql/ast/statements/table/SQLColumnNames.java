package dev.jdata.db.sql.ast.statements.table;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;
import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.ast.SQLFreeable;
import dev.jdata.db.utils.adt.arrays.LongArray;
import dev.jdata.db.utils.adt.elements.IElements;

public final class SQLColumnNames extends BaseSQLElement implements IElements, SQLFreeable {

    private final LongArray names;

    public SQLColumnNames(Context context, LongArray names) {
        super(context);

        this.names = Objects.requireNonNull(names);
    }

    @Override
    public boolean isEmpty() {

        return names.isEmpty();
    }

    @Override
    public long getNumElements() {

        return names.getNumElements();
    }

    public long get(int index) {

        return names.get(index);
    }

    @Override
    public void free(SQLAllocator allocator) {

        allocator.freeLongArray(names);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
