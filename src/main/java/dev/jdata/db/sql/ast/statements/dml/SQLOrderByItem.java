package dev.jdata.db.sql.ast.statements.dml;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLOrderByItem extends BaseSQLElement {

    private final ASTSingle<SQLOrderByOrGroupByItem> item;
    private final ASTSingle<SQLSortOrder> sortOrder;

    public SQLOrderByItem(Context context, SQLOrderByOrGroupByItem item) {
        this(context, item, null, false);
    }

    public SQLOrderByItem(Context context, SQLOrderByOrGroupByItem item, SQLSortOrder sortOrder) {
        this(context, item, Objects.requireNonNull(sortOrder), false);
    }

    private SQLOrderByItem(Context context, SQLOrderByOrGroupByItem item, SQLSortOrder sortOrder, boolean disambiguate) {
        super(context);

        this.item = makeSingle(item);
        this.sortOrder = safeMakeSingle(sortOrder);
    }

    public SQLOrderByOrGroupByItem getItem() {
        return item.get();
    }

    public SQLSortOrder getSortOrder() {

        return safeGet(sortOrder);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(item, recurseMode, iterator);

        if (sortOrder != null) {

            doIterate(sortOrder, recurseMode, iterator);
        }
    }
}
