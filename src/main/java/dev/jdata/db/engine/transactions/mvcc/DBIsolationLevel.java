package dev.jdata.db.engine.transactions.mvcc;

public enum DBIsolationLevel {

    COMMITTED_READ,
    REPEATABLE_READ,
    SERIALIZABLE
}
