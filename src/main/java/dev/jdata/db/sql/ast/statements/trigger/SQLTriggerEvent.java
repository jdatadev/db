package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;
import dev.jdata.db.sql.ast.statements.dml.SQLTableName;

abstract class SQLTriggerEvent extends BaseSQLElement {

    private final long dmlStatementKeyword;
    private final long onKeyword;
    private final ASTSingle<SQLTableName> tableName;

    SQLTriggerEvent(Context context, long dmlStatementKeyword, long onKeyword, SQLTableName tableName) {
        super(context);

        this.dmlStatementKeyword = checkIsKeyword(dmlStatementKeyword);
        this.onKeyword = checkIsKeyword(onKeyword);
        this.tableName = makeSingle(tableName);
    }

    public final long getDmlStatementKeyword() {
        return dmlStatementKeyword;
    }

    public long getOnKeyword() {
        return onKeyword;
    }

    public SQLTableName getTableName() {
        return tableName.get();
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(tableName, recurseMode, iterator);
    }
}
