package dev.jdata.db.sql.ast.statements.table;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;
import dev.jdata.db.utils.adt.arrays.LongArray;
import dev.jdata.db.utils.adt.elements.IElements;

public final class SQLColumnNames extends BaseSQLElement implements IElements {

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

        return names.getLimit();
    }

    public long get(int index) {

        return names.get(index);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
