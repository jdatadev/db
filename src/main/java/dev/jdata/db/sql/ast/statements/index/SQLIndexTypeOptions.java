package dev.jdata.db.sql.ast.statements.index;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public class SQLIndexTypeOptions extends BaseSQLElement {

    private final long uniqueKeyword;

    public SQLIndexTypeOptions(Context context, long uniqueKeyword) {
        super(context);

        this.uniqueKeyword = checkIsKeywordOrNoKeyword(uniqueKeyword);
    }

    public final long getUniqueKeyword() {
        return uniqueKeyword;
    }

    public final boolean isUnique() {

        return isKeyword(uniqueKeyword);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
