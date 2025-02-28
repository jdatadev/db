package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLObjectOrAlias extends BaseSQLElement {

    private final ASTSingle<SQLNamespaceObject> namespaceObject;
    private final long objectNameAndAlias;

    public SQLObjectOrAlias(Context context, SQLNamespaceObject namespaceObject) {
        super(context);

        this.namespaceObject = makeSingle(namespaceObject);
        this.objectNameAndAlias = NO_STRING;
    }

    public SQLObjectOrAlias(Context context, long alias) {
        super(context);

        this.namespaceObject = null;
        this.objectNameAndAlias = checkIsString(alias);
    }

    public SQLNamespaceObject getNamespaceObject() {

        return namespaceObject.get();
    }

    public long getObjectNameAndAlias() {
        return objectNameAndAlias;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(namespaceObject, recurseMode, iterator);
    }
}
