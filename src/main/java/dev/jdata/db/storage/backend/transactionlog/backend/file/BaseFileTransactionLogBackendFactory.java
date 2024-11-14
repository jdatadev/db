package dev.jdata.db.storage.backend.transactionlog.backend.file;

import dev.jdata.db.storage.backend.transactionlog.BaseTransactionLogBackend;
import dev.jdata.db.storage.backend.transactionlog.backend.BaseTransactionLogBackendFactory;

public abstract class BaseFileTransactionLogBackendFactory<C extends FileTransactionLogBackendConfiguration, T extends BaseTransactionLogBackend>
        extends BaseTransactionLogBackendFactory<C, T> {

}
