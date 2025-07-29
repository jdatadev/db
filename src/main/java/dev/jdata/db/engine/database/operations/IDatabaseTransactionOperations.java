package dev.jdata.db.engine.database.operations;

public interface IDatabaseTransactionOperations {

    int startTransaction(int sessionId);

    int createSavePoint(int sessionId, long savePointName);
    int rollbackToSavePoint(int sessionId, long savePointName);

    void commitTransaction(int sessionId);
    void rollbackTransaction(int sessionId);
}
