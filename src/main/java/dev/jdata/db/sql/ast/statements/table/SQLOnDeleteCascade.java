package dev.jdata.db.sql.ast.statements.table;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLOnDeleteCascade extends BaseSQLElement {

    private final long onKeyword;
    private final long deleteKeyword;
    private final long cascadeKeyword;

    public SQLOnDeleteCascade(Context context, long onKeyword, long deleteKeyword, long cascadeKeyword) {
        super(context);

        this.onKeyword = checkIsKeyword(onKeyword);
        this.deleteKeyword = checkIsKeyword(deleteKeyword);
        this.cascadeKeyword = checkIsKeyword(cascadeKeyword);
    }

    public long getOnKeyword() {
        return onKeyword;
    }

    public long getDeleteKeyword() {
        return deleteKeyword;
    }

    public long getCascadeKeyword() {
        return cascadeKeyword;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
