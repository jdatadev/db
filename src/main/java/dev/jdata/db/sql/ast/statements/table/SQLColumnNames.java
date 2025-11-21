package dev.jdata.db.sql.ast.statements.table;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.ISQLFreeable;
import dev.jdata.db.utils.adt.elements.ILongForEach;
import dev.jdata.db.utils.adt.elements.ILongForEachWithResult;
import dev.jdata.db.utils.adt.elements.ILongIterableOnlyElementsView;
import dev.jdata.db.utils.adt.lists.ICachedLongIndexList;

public final class SQLColumnNames extends BaseSQLElement implements ILongIterableOnlyElementsView, ISQLFreeable  {

    private final ICachedLongIndexList names;

    public SQLColumnNames(Context context, ICachedLongIndexList names) {
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

    @Override
    public long getNumIterableElements() {

        return getNumElements();
    }

    @Override
    public <P, E extends Exception> void forEach(P parameter, ILongForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        names.forEach(parameter, forEach);
    }

    @Override
    public <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, ILongForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        return names.forEachWithResult(defaultResult, parameter1, parameter2, forEach);
    }

    @Override
    public void free(ISQLAllocator allocator) {

        Objects.requireNonNull(allocator);

        allocator.freeLongIndexList(names);
    }

    public long get(int index) {

        return names.get(index);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
