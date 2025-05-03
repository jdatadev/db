package dev.jdata.db.storage.backend.transactionlog.backend.file;

import java.io.IOException;
import java.util.Arrays;

import dev.jdata.db.storage.backend.file.BaseStorageFiles;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.maps.MutableLongToObjectWithRemoveNonBucketMap;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.file.access.RelativeDirectoryPath;
import dev.jdata.db.utils.file.access.RelativeFilePath;
import dev.jdata.db.utils.file.access.SequentialFileAccess;

public final class FileTransactionLogFiles extends BaseStorageFiles<SequentialFileAccess, FileTransactionLogFile> {

    private static final String FILE_NAME_PREFIX = "transaction";

    static long parseTransactionId(String fileName) {

        return parseSequenceNo(fileName, FILE_NAME_PREFIX);
    }

    private final MutableLongToObjectWithRemoveNonBucketMap<FileTransactionLogFile> fileByLongToObjectMap;

    public FileTransactionLogFiles(IRelativeFileSystemAccess fileSystemAccess, RelativeDirectoryPath directoryPath, IIndexList<FileTransactionLogFile> files) {
        super(fileSystemAccess, directoryPath, files);

        this.fileByLongToObjectMap = new MutableLongToObjectWithRemoveNonBucketMap<>(10, 1, HashedConstants.DEFAULT_LOAD_FACTOR, FileTransactionLogFile[]::new);
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

        final RelativeFilePath filePath = constructPath(transactionId);

        final FileTransactionLogFile result = new FileTransactionLogFile(transactionId, getFileSystemAccess(), filePath);

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
