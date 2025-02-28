package dev.jdata.db.sql.ast.statements.trigger;

import java.util.List;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.BaseASTElement;
import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.BaseSQLStatement;

public class SQLTriggeredAction extends BaseASTElement {

    private final long whenKeyword;
    private final ASTSingle<Expression> condition;
    private final ASTList<BaseSQLStatement> statements;

    public SQLTriggeredAction(Context context, long whenKeyword, Expression condition, List<SQLTriggeredStatement> statements) {
        super(context);

        checkIsKeywordAndElementOrNot(whenKeyword, condition);

        this.whenKeyword = whenKeyword;
        this.condition = safeMakeSingle(condition);

        @SuppressWarnings("unchecked")
        final List<BaseSQLStatement> statementsList = (List<BaseSQLStatement>)(List<?>)statements;

        this.statements = makeList(statementsList);
    }

    public final long getWhenKeyword() {
        return whenKeyword;
    }

    public final Expression getCondition() {
        return condition.get();
    }

    public final ASTList<BaseSQLStatement> getStatements() {
        return statements;
    }

    @Override
    protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(condition, recurseMode, iterator);
        doIterate(statements, recurseMode, iterator);
    }
}
