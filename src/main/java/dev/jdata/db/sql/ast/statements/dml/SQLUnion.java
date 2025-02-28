package dev.jdata.db.sql.ast.statements.dml;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLUnion extends BaseSQLElement {

    private final long keyword;
    private final SQLUnionType type;

    public SQLUnion(Context context, long keyword, SQLUnionType type) {
        super(context);

        this.keyword = checkIsKeyword(keyword);
        this.type = Objects.requireNonNull(type);
    }

    public long getKeyword() {
        return keyword;
    }

    public SQLUnionType getType() {
        return type;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
