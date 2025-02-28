package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public class SQLObjectName extends BaseSQLElement {

    private final long name;

    public SQLObjectName(Context context, long name) {
        super(context);

        this.name = checkIsString(name);
    }

    public final long getName() {
        return name;
    }

    @Override
    protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
