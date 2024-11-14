package dev.jdata.db.storage.backend.transactionlog;

import java.io.IOException;

import dev.jdata.db.storage.backend.DataStorer;

public interface TransactionLogBackend extends DataStorer {

    void startTransaction(long transactionId) throws IOException;

    void commitTransaction(long transactionId) throws IOException;
}
