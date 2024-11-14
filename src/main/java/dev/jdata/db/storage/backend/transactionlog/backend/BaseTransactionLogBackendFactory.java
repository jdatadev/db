package dev.jdata.db.storage.backend.transactionlog.backend;

import dev.jdata.db.storage.backend.transactionlog.TransactionLogBackend;
import dev.jdata.db.storage.backend.transactionlog.TransactionLogBackendFactory;

public abstract class BaseTransactionLogBackendFactory<C extends TransactionLogBackendConfiguration, T extends TransactionLogBackend>
        implements TransactionLogBackendFactory<C, T> {

}
