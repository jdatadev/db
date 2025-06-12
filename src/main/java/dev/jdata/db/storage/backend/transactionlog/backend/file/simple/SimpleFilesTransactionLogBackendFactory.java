package dev.jdata.db.storage.backend.transactionlog.backend.file.simple;

import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.storage.backend.tabledata.StorageTableSchemas;
import dev.jdata.db.storage.backend.transactionlog.backend.file.BaseFileTransactionLogBackendFactory;
import dev.jdata.db.storage.backend.transactionlog.backend.file.FileTransactionLogBackendConfiguration;
import dev.jdata.db.storage.backend.transactionlog.backend.file.FileTransactionLogFile;
import dev.jdata.db.storage.backend.transactionlog.backend.file.FileTransactionLogFiles;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.file.access.AbsoluteDirectoryPath;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.file.access.RelativeDirectoryPath;
import dev.jdata.db.utils.file.access.RelativeFilePath;
import dev.jdata.db.utils.scalars.Integers;

public final class SimpleFilesTransactionLogBackendFactory
        extends BaseFileTransactionLogBackendFactory<FileTransactionLogBackendConfiguration, SimpleFilesTransactionLogBackend> {

    @Override
    public SimpleFilesTransactionLogBackend initialize(StorageTableSchemas storageTableSchemas, FileTransactionLogBackendConfiguration configuration) throws IOException {

        Objects.requireNonNull(storageTableSchemas);
        Objects.requireNonNull(configuration);

        final AbsoluteDirectoryPath rootPath = configuration.getRootPath();
        final IRelativeFileSystemAccess fileSystemAccess = IRelativeFileSystemAccess.create(rootPath, configuration.getFileSystemAccess());

        final RelativeDirectoryPath relativeRootPath = RelativeDirectoryPath.ROOT;

        final IIndexList<RelativeFilePath> transactionFilePaths;

        final IndexListAllocator<RelativeFilePath> indexListAllocator = null;

        final IndexList.Builder<RelativeFilePath> transactionFilePathsBuilder = IndexList.createBuilder(indexListAllocator);

        try {
            fileSystemAccess.listFilePaths(relativeRootPath, transactionFilePathsBuilder, (p, b) -> b.addTail(p));

            transactionFilePaths = transactionFilePathsBuilder.build();
        }
        finally {

            indexListAllocator.freeIndexListBuilder(transactionFilePathsBuilder);
        }

        final IIndexList<FileTransactionLogFile> fileTransactionLogFileList = readTransactionLogFiles(fileSystemAccess, transactionFilePaths);

        final FileTransactionLogFiles fileTransactionLogFiles = new FileTransactionLogFiles(fileSystemAccess, relativeRootPath, fileTransactionLogFileList);

        return new SimpleFilesTransactionLogBackend(storageTableSchemas, fileTransactionLogFiles);
    }

    private static IIndexList<FileTransactionLogFile> readTransactionLogFiles(IRelativeFileSystemAccess fileSystemAccess, IIndexList<RelativeFilePath> transactionLogFilePaths)
            throws IOException {

        final long numElements = transactionLogFilePaths.getNumElements();
        final int initialCapacity = Integers.checkUnsignedLongToUnsignedInt(numElements);

        final IIndexList<FileTransactionLogFile> result;

        final IndexListAllocator<FileTransactionLogFile> indexListAllocator = null;

        final IndexList.Builder<FileTransactionLogFile> fileTransactionLogFileList = IndexList.createBuilder(initialCapacity, indexListAllocator);

        try {
            for (long i = 0L; i < numElements; ++ i) {

                final RelativeFilePath transactionLogFilePath = transactionLogFilePaths.get(i);

                final FileTransactionLogFile fileTransactionLogFile = FileTransactionLogFile.read(fileSystemAccess, transactionLogFilePath);

                fileTransactionLogFileList.addTail(fileTransactionLogFile);
            }

            result = fileTransactionLogFileList.build();
        }
        finally {

            indexListAllocator.freeIndexListBuilder(fileTransactionLogFileList);
        }

        return result;
    }
}
