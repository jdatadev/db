package dev.jdata.db.sql.ast.statements.dml;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.language.common.names.Names;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLNamespaceObject extends BaseSQLElement {

    private final Names namespace;
    private final long name;

    public SQLNamespaceObject(Context context, Names namespace, long name) {
        super(context);

        this.namespace = Objects.requireNonNull(namespace);
        this.name = checkIsString(name);
    }

    public Names getNamespace() {
        return namespace;
    }

    public long getName() {
        return name;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
