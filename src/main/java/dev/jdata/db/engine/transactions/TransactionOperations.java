package dev.jdata.db.engine.transactions;

public interface TransactionOperations<T> extends TransactionDMLOperations<T> {

    void commit(T sharedState);
    void rollback(T sharedState);
}
