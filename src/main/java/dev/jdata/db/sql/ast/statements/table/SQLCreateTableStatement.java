package dev.jdata.db.sql.ast.statements.table;

import java.util.List;
import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.SQLStatementVisitor;

public final class SQLCreateTableStatement extends SQLTableStatement {

    private final ASTList<SQLTableColumnDefinition> columns;
    private final ASTSingle<SQLPrimaryKey> primaryKey;

    public SQLCreateTableStatement(Context context, long operationKeyword, long tableKeyword, long tableName, List<SQLTableColumnDefinition> columns) {
        this(context, operationKeyword, tableKeyword, tableName, columns, null, false);
    }

    public SQLCreateTableStatement(Context context, long operationKeyword, long tableKeyword, long tableName, List<SQLTableColumnDefinition> columns, SQLPrimaryKey primaryKey) {
        this(context, operationKeyword, tableKeyword, tableName, columns, Objects.requireNonNull(primaryKey), false);
    }

    private SQLCreateTableStatement(Context context, long operationKeyword, long tableKeyword, long tableName, List<SQLTableColumnDefinition> columns, SQLPrimaryKey primaryKey,
            boolean disambiguate) {
        super(context, operationKeyword, tableKeyword, tableName);

        this.columns = makeList(columns);
        this.primaryKey = safeMakeSingle(primaryKey);
    }

    @Override
    public <T, R> R visit(SQLStatementVisitor<T, R> visitor, T parameter) {

        return visitor.onCreateTable(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(columns, recurseMode, iterator);

        if (primaryKey != null) {

            doIterate(primaryKey, recurseMode, iterator);
        }
    }
}
