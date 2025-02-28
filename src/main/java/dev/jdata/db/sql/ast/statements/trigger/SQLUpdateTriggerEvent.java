package dev.jdata.db.sql.ast.statements.trigger;

import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.dml.SQLTableName;
import dev.jdata.db.sql.ast.statements.table.SQLColumnNames;

public class SQLUpdateTriggerEvent extends SQLTriggerEvent {

    private final long ofKeyword;
    private final ASTSingle<SQLColumnNames> columnNames;

    public SQLUpdateTriggerEvent(Context context, long updateKeyword, long ofKeyword, SQLColumnNames columnNames, long onKeyword, SQLTableName tableName) {
        super(context, updateKeyword, onKeyword, tableName);

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
