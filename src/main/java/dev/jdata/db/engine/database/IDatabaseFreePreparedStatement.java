package dev.jdata.db.engine.database;

public interface IDatabaseFreePreparedStatement {

    void freePreparedStatement(int databaseId, int sessionId, int preparedStatementId);
}
