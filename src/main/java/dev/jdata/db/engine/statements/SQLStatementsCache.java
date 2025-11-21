package dev.jdata.db.engine.statements;

import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.IIntArrayView;
import dev.jdata.db.utils.adt.maps.IHeapMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
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

        private final IIntArrayView columns;

        InsertOrUpdateStatement(int tableId, IIntArrayView columns) {
            super(tableId);

            this.columns = Objects.requireNonNull(columns);
        }

        public final IIntArrayView getColumns() {
            return columns;
        }
    }

    public static final class InsertStatement extends InsertOrUpdateStatement {

        InsertStatement(int tableId, IIntArrayView columns) {
            super(tableId, columns);
        }
    }

    public static final class UpdateStatement extends InsertOrUpdateStatement {

        UpdateStatement(int tableId, IIntArrayView columns) {
            super(tableId, columns);
        }
    }

    public static final class DeleteStatement extends DMLStatement {

        DeleteStatement(int tableId) {
            super(tableId);
        }
    }

    private int sqlStatementIdAllocator;

    private final IMutableIntToObjectWithRemoveStaticMap<SQLStatement> sqlStatementsById;

    public SQLStatementsCache() {

        this.sqlStatementIdAllocator = 0;

        this.sqlStatementsById = IHeapMutableIntToObjectWithRemoveStaticMap.create(0, SQLStatement[]::new);
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
