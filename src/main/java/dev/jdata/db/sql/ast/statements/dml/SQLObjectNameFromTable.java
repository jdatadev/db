package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

public final class SQLObjectNameFromTable extends SQLFromTable {

    private final ASTSingle<SQLObjectNameAndAlias> objectNameAndAlias;

    public SQLObjectNameFromTable(Context context, SQLObjectNameAndAlias objectNameAndAlias) {
        super(context);

        this.objectNameAndAlias = makeSingle(objectNameAndAlias);
    }

    public SQLObjectNameAndAlias getObjectNameAndAlias() {
        return objectNameAndAlias.get();
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(objectNameAndAlias, recurseMode, iterator);
    }
}
