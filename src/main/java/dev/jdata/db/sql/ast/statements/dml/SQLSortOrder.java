package dev.jdata.db.sql.ast.statements.dml;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLSortOrder extends BaseSQLElement {

    private final long keyword;
    private final SQLSortOrderType type;

    public SQLSortOrder(Context context, long keyword, SQLSortOrderType type) {
        super(context);

        this.keyword = checkIsKeyword(keyword);
        this.type = Objects.requireNonNull(type);
    }

    public long getKeyword() {
        return keyword;
    }

    public SQLSortOrderType getType() {
        return type;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
