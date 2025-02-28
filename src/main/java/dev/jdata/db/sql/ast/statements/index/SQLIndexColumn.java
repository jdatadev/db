package dev.jdata.db.sql.ast.statements.index;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLIndexColumn extends BaseSQLElement {

    private final long name;

    public SQLIndexColumn(Context context, long name) {
        super(context);

        this.name = checkIsString(name);
    }

    public long getName() {
        return name;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
