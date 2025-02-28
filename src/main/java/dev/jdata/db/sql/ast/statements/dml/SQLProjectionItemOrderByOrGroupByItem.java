package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

public final class SQLProjectionItemOrderByOrGroupByItem extends SQLOrderByOrGroupByItem {

    private final ASTSingle<SQLProjectionItem> projectionItem;

    public SQLProjectionItemOrderByOrGroupByItem(Context context, SQLProjectionItem projectionItem) {
        super(context);

        this.projectionItem = makeSingle(projectionItem);
    }

    public SQLProjectionItem getProjectionItem() {
        return projectionItem.get();
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(projectionItem, recurseMode, iterator);
    }
}
