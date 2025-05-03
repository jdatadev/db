package dev.jdata.db.engine.statements;

import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.IIntArray;
import dev.jdata.db.utils.adt.maps.MutableIntToObjectWithRemoveNonBucketMap;
import dev.jdata.db.utils.checks.Checks;

@Deprecated // statement cache?
public final class SQLStatements {

    public static abstract class SQLStatement {

    }

    static abstract class DMLStatement extends SQLStatement {

        private final int tableId;

        DMLStatement(int tableId) {

            this.tableId = Checks.isTableId(tableId);
        }

        public final int getTableId() {
            return tableId;
        }
    }

    static abstract class InsertOrUpdateStatement extends DMLStatement {

        private final IIntArray columns;

        InsertOrUpdateStatement(int tableId, IIntArray columns) {
            super(tableId);

            this.columns = Objects.requireNonNull(columns);
        }

        public final IIntArray getColumns() {
            return columns;
        }
    }

    public static final class InsertStatement extends InsertOrUpdateStatement {

        InsertStatement(int tableId, IIntArray columns) {
            super(tableId, columns);
        }
    }

    public static final class UpdateStatement extends InsertOrUpdateStatement {

        UpdateStatement(int tableId, IIntArray columns) {
            super(tableId, columns);
        }
    }

    public static final class DeleteStatement extends DMLStatement {

        DeleteStatement(int tableId) {
            super(tableId);
        }
    }

    private int sqlStatementIdAllocator;

    private final MutableIntToObjectWithRemoveNonBucketMap<SQLStatement> sqlStatementsById;

    public SQLStatements() {

        this.sqlStatementIdAllocator = 0;

        this.sqlStatementsById = new MutableIntToObjectWithRemoveNonBucketMap<>(0, SQLStatement[]::new);
    }

    public int prepareSQLStatement() {

        final int sqlStatemenId;

        synchronized (this) {

            sqlStatemenId = sqlStatementIdAllocator ++;
        }

        return sqlStatemenId;
    }

    public synchronized SQLStatement getSQLStatement(int sqlStatementId) {

        return sqlStatementsById.get(sqlStatementId);
    }
}
