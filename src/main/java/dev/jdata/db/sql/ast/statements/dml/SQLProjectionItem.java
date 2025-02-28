package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLProjectionItem extends BaseSQLElement {

    private final ASTSingle<SQLProjectionElement> projectionElement;
    private final long alias;

    public SQLProjectionItem(Context context, SQLProjectionElement projectionElement) {
        this(context, projectionElement, NO_STRING, false);
    }

    public SQLProjectionItem(Context context, SQLProjectionElement projectionElement, long alias) {
        this(context, projectionElement, checkIsString(alias), false);
    }

    private SQLProjectionItem(Context context, SQLProjectionElement projectionElement, long alias, boolean disambiguate) {
        super(context);

        this.projectionElement = makeSingle(projectionElement);
        this.alias = checkIsStringOrNoString(alias);
    }

    public SQLProjectionElement getProjectionElement() {
        return projectionElement.get();
    }

    public long getAlias() {
        return alias;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(projectionElement, recurseMode, iterator);
    }
}
