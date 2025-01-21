package dev.jdata.db.engine.database;

import dev.jdata.db.dml.DMLInsertRows;
import dev.jdata.db.dml.DMLUpdateRows;

public interface DMLOperations {

    void insertRows(int sessionId, int sqlStatementId, DMLInsertRows rows);

    void updateRows(int sessionId, int sqlStatementId, DMLUpdateRows rows);
}
