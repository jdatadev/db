package dev.jdata.db.sql.ast.statements.table;
import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.ast.objects.list.IIndexListGetters;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.SQLStatementVisitor;

public final class SQLCreateTableStatement extends SQLTableStatement {

    private final ASTList<SQLTableColumnDefinition> columns;
    private final ASTSingle<SQLPrimaryKey> primaryKey;

    public SQLCreateTableStatement(Context context, long operationKeyword, long tableKeyword, long tableName, IIndexListGetters<SQLTableColumnDefinition> columns) {
        this(context, operationKeyword, tableKeyword, tableName, columns, null, false);
    }

    public SQLCreateTableStatement(Context context, long operationKeyword, long tableKeyword, long tableName, IIndexListGetters<SQLTableColumnDefinition> columns,
            SQLPrimaryKey primaryKey) {
        this(context, operationKeyword, tableKeyword, tableName, columns, Objects.requireNonNull(primaryKey), false);
    }

    private SQLCreateTableStatement(Context context, long operationKeyword, long tableKeyword, long tableName, IIndexListGetters<SQLTableColumnDefinition> columns,
            SQLPrimaryKey primaryKey, boolean disambiguate) {
        super(context, operationKeyword, tableKeyword, tableName);

        this.columns = makeList(columns);
        this.primaryKey = safeMakeSingle(primaryKey);
    }

    public ASTList<SQLTableColumnDefinition> getColumns() {
        return columns;
    }

    public SQLPrimaryKey getPrimaryKey() {
        return primaryKey.get();
    }

    @Override
    public <P, R, E extends Exception> R visit(SQLStatementVisitor<P, R, E> visitor, P parameter) throws E {

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
