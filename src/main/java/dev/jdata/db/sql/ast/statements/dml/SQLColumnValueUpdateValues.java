package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.ast.objects.list.IListGetters;
import org.jutils.parse.context.Context;

public final class SQLColumnValueUpdateValues extends SQLUpdateValues {

    private final ASTList<SQLColumnValueUpdateValue> values;

    public SQLColumnValueUpdateValues(Context context, IListGetters<SQLColumnValueUpdateValue> values) {
        super(context);

        this.values = makeNonEmptyList(values);
    }

    public ASTList<SQLColumnValueUpdateValue> getValues() {
        return values;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(values, recurseMode, iterator);
    }
}
