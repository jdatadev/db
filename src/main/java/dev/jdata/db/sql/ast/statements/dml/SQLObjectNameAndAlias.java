package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLObjectNameAndAlias extends BaseSQLElement {

    private final ASTSingle<SQLObjectName> objectName;
    private final long alias;

    public SQLObjectNameAndAlias(Context context, SQLObjectName objectName) {
        this(context, objectName, NO_STRING, false);
    }

    public SQLObjectNameAndAlias(Context context, SQLObjectName objectName, long alias) {
        this(context, objectName, checkIsString(alias), false);
    }

    private SQLObjectNameAndAlias(Context context, SQLObjectName objectName, long alias, boolean disambiguate) {
        super(context);

        this.objectName = makeSingle(objectName);
        this.alias = checkIsString(alias);
    }

    public SQLObjectName getObjectName() {
        return objectName.get();
    }

    public long getAlias() {
        return alias;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(objectName, recurseMode, iterator);
    }
}
