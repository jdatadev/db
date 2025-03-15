package dev.jdata.db.sql.ast.statements.dml;

import java.util.List;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.SQLStatementVisitor;
import dev.jdata.db.sql.ast.statements.table.SQLColumnNames;
import dev.jdata.db.sql.ast.statements.trigger.SQLTriggeredStatement;

public final class SQLInsertStatement extends SQLDMLUpdatingStatement implements SQLTriggeredStatement {

    private final long insertKeyword;
    private final long intoKeyword;
    private final long tableName;
    private final ASTSingle<SQLColumnNames> columnNames;
    private final long valuesKeyword;
    private final ASTList<Expression> values;

    public SQLInsertStatement(Context context, long insertKeyword, long intoKeyword, long tableName, SQLColumnNames columnNames, long valuesKeyword, List<Expression> values) {
        super(context);

        this.insertKeyword = checkIsKeyword(insertKeyword);
        this.intoKeyword = checkIsKeyword(intoKeyword);
        this.tableName = checkIsString(tableName);
        this.columnNames = makeSingle(columnNames);
        this.valuesKeyword = checkIsKeyword(valuesKeyword);
        this.values = makeList(values);
    }

    public long getInsertKeyword() {
        return insertKeyword;
    }

    public long getIntoKeyword() {
        return intoKeyword;
    }

    public long getTableName() {
        return tableName;
    }

    public SQLColumnNames getColumnNames() {
        return columnNames.get();
    }

    public long getValuesKeyword() {
        return valuesKeyword;
    }

    public ASTList<Expression> getValues() {
        return values;
    }

    @Override
    public <P, R, E extends Exception> R visit(SQLStatementVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onInsert(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(values, recurseMode, iterator);
    }
}
