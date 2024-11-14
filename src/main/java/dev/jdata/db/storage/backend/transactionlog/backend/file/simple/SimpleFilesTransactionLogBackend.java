package dev.jdata.db.storage.backend.transactionlog.backend.file.simple;

import dev.jdata.db.storage.backend.tabledata.StorageTableSchemas;
import dev.jdata.db.storage.backend.transactionlog.backend.file.BaseFilesTransactionLogBackend;
import dev.jdata.db.storage.backend.transactionlog.backend.file.FileTransactionLogFiles;

final class SimpleFilesTransactionLogBackend extends BaseFilesTransactionLogBackend {

    SimpleFilesTransactionLogBackend(StorageTableSchemas storageTableSchemas, FileTransactionLogFiles transactionLogFiles) {
        super(storageTableSchemas, transactionLogFiles);
    }
}
