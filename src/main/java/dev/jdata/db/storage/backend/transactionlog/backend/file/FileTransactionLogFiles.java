package dev.jdata.db.storage.backend.transactionlog.backend.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import dev.jdata.db.storage.backend.file.BaseStorageFiles;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.maps.LongToObjectMap;
import dev.jdata.db.utils.checks.Checks;

public final class FileTransactionLogFiles extends BaseStorageFiles<FileOutputStream, FileTransactionLogFile> {

    private static final String FILE_NAME_PREFIX = "transaction";

    static long parseTransactionId(String fileName) {

        return parseSequenceNo(fileName, FILE_NAME_PREFIX);
    }

    private final LongToObjectMap<FileTransactionLogFile> fileByLongToObjectMap;

    public FileTransactionLogFiles(Path directoryPath, List<FileTransactionLogFile> files) {
        super(directoryPath, files);

        this.fileByLongToObjectMap = new LongToObjectMap<>(10, 1, HashedConstants.DEFAULT_LOAD_FACTOR, FileTransactionLogFile[]::new);
    }

    @Override
    protected String getFileNamePrefix() {

        return FILE_NAME_PREFIX;
    }

    FileTransactionLogFile getFile(long transactionId) throws IOException {

        Checks.isTransactionId(transactionId);

        return fileByLongToObjectMap.get(transactionId);
    }

    FileTransactionLogFile addFile(long transactionId) throws IOException {

        Checks.isTransactionId(transactionId);

        final Path path = constructPath(transactionId);

        final FileTransactionLogFile result = new FileTransactionLogFile(transactionId, path);

        addFile(result);

        fileByLongToObjectMap.put(transactionId, result);

        return result;
    }

    long[] getTransactionIdsSorted() {

        final long[] transactionIds = fileByLongToObjectMap.keys();

        Arrays.sort(transactionIds);

        return transactionIds;
    }
}
