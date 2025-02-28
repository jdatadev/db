package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public abstract class SQLTriggerCorrelation extends BaseSQLElement {

    private final long keyword;
    private final long asKeyword;
    private final long correlation;

    public SQLTriggerCorrelation(Context context, long keyword, long asKeyword, long correlation) {
        super(context);

        this.keyword = checkIsKeyword(keyword);
        this.asKeyword = checkIsKeyword(asKeyword);
        this.correlation = checkIsString(correlation);
    }

    final long getKeyword() {
        return keyword;
    }

    public final long getAsKeyword() {
        return asKeyword;
    }

    public final long getCorrelation() {
        return correlation;
    }

    @Override
    protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
