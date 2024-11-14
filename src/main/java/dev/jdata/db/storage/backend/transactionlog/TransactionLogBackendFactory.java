package dev.jdata.db.storage.backend.transactionlog;

import java.io.IOException;

import dev.jdata.db.storage.backend.tabledata.StorageTableSchemas;
import dev.jdata.db.storage.backend.transactionlog.backend.TransactionLogBackendConfiguration;

public interface TransactionLogBackendFactory<C extends TransactionLogBackendConfiguration, T extends TransactionLogBackend> {

    T initialize(StorageTableSchemas storageTableSchemas, C configuration) throws IOException;
}
