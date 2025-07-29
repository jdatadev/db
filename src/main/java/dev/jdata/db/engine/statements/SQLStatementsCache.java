package dev.jdata.db.engine.statements;

import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.IIntArrayCommon;
import dev.jdata.db.utils.adt.maps.MutableIntToObjectWithRemoveNonBucketMap;
import dev.jdata.db.utils.checks.Checks;

public final class SQLStatementsCache {

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

        private final IIntArrayCommon columns;

        InsertOrUpdateStatement(int tableId, IIntArrayCommon columns) {
            super(tableId);

            this.columns = Objects.requireNonNull(columns);
        }

        public final IIntArrayCommon getColumns() {
            return columns;
        }
    }

    public static final class InsertStatement extends InsertOrUpdateStatement {

        InsertStatement(int tableId, IIntArrayCommon columns) {
            super(tableId, columns);
        }
    }

    public static final class UpdateStatement extends InsertOrUpdateStatement {

        UpdateStatement(int tableId, IIntArrayCommon columns) {
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

    public SQLStatementsCache() {

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
