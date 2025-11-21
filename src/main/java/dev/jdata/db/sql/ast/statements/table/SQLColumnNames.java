package dev.jdata.db.sql.ast.statements.table;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;
import dev.jdata.db.utils.adt.elements.ILongForEach;
import dev.jdata.db.utils.adt.elements.ILongForEachWithResult;
import dev.jdata.db.utils.adt.elements.ILongIterableOnlyElementsView;
import dev.jdata.db.utils.adt.lists.IHeapLongIndexList;

public final class SQLColumnNames extends BaseSQLElement implements ILongIterableOnlyElementsView {

    private final IHeapLongIndexList names;

    public SQLColumnNames(Context context, IHeapLongIndexList names) {
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
    public <P, E extends Exception> void forEach(P parameter, ILongForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        names.forEach(parameter, forEach);
    }

    @Override
    public <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, ILongForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        return names.forEachWithResult(defaultResult, parameter1, parameter2, forEach);
    }

    public long get(int index) {

        return names.get(index);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
