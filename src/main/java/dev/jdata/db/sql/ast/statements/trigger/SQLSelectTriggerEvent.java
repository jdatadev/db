package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.dml.SQLTableName;
import dev.jdata.db.sql.ast.statements.table.SQLColumnNames;

public class SQLSelectTriggerEvent extends SQLTriggerEvent {

    private final long ofKeyword;
    private final ASTSingle<SQLColumnNames> columnNames;

    public SQLSelectTriggerEvent(Context context, long selectKeyword, long ofKeyword, SQLColumnNames columnNames, long onKeyword, SQLTableName tableName) {
        super(context, selectKeyword, onKeyword, tableName);

        checkIsKeywordAndElementOrNot(ofKeyword, columnNames);

        this.ofKeyword = ofKeyword;
        this.columnNames = safeMakeSingle(columnNames);
    }

    public final long getOfKeyword() {
        return ofKeyword;
    }

    public final SQLColumnNames getColumnNames() {
        return columnNames.get();
    }
}
