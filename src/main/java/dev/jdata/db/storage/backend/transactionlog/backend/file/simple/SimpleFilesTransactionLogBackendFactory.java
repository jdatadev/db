package dev.jdata.db.storage.backend.transactionlog.backend.file.simple;

import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.storage.backend.tabledata.StorageTableSchemas;
import dev.jdata.db.storage.backend.transactionlog.backend.file.BaseFileTransactionLogBackendFactory;
import dev.jdata.db.storage.backend.transactionlog.backend.file.FileTransactionLogBackendConfiguration;
import dev.jdata.db.storage.backend.transactionlog.backend.file.FileTransactionLogFile;
import dev.jdata.db.storage.backend.transactionlog.backend.file.FileTransactionLogFiles;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.lists.ICachedIndexList;
import dev.jdata.db.utils.adt.lists.ICachedIndexListAllocator;
import dev.jdata.db.utils.adt.lists.ICachedIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.file.access.AbsoluteDirectoryPath;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.file.access.RelativeDirectoryPath;
import dev.jdata.db.utils.file.access.RelativeFilePath;

public final class SimpleFilesTransactionLogBackendFactory
        extends BaseFileTransactionLogBackendFactory<FileTransactionLogBackendConfiguration, SimpleFilesTransactionLogBackend> {

    private final ICachedIndexListAllocator<FileTransactionLogFile> logFileIndexListAllocator;
    private final ICachedIndexListAllocator<RelativeFilePath> relativeFilePathIndexListAllocator;

    public SimpleFilesTransactionLogBackendFactory(ICachedIndexListAllocator<FileTransactionLogFile> logFileIndexListAllocator,
            ICachedIndexListAllocator<RelativeFilePath> relativeFilePathIndexListAllocator) {

        this.logFileIndexListAllocator = Objects.requireNonNull(logFileIndexListAllocator);
        this.relativeFilePathIndexListAllocator = Objects.requireNonNull(relativeFilePathIndexListAllocator);
    }

    @Override
    public SimpleFilesTransactionLogBackend initialize(StorageTableSchemas storageTableSchemas, FileTransactionLogBackendConfiguration configuration) throws IOException {

        Objects.requireNonNull(storageTableSchemas);
        Objects.requireNonNull(configuration);

        final SimpleFilesTransactionLogBackend result;

        final AbsoluteDirectoryPath rootPath = configuration.getRootPath();
        final IRelativeFileSystemAccess fileSystemAccess = IRelativeFileSystemAccess.create(rootPath, configuration.getFileSystemAccess());

        final RelativeDirectoryPath relativeRootPath = RelativeDirectoryPath.ROOT;

        ICachedIndexList<RelativeFilePath> transactionFilePaths = null;

        final ICachedIndexListBuilder<RelativeFilePath> transactionFilePathsBuilder = relativeFilePathIndexListAllocator.createBuilder();

        try {
            fileSystemAccess.listFilePaths(relativeRootPath, transactionFilePathsBuilder, (p, b) -> b.addTail(p));

            transactionFilePaths = transactionFilePathsBuilder.buildOrEmpty();

            ICachedIndexList<FileTransactionLogFile> fileTransactionLogFileList = null;

            try {
                fileTransactionLogFileList = readTransactionLogFiles(fileSystemAccess, transactionFilePaths, logFileIndexListAllocator);

                final FileTransactionLogFiles fileTransactionLogFiles = new FileTransactionLogFiles(fileSystemAccess, relativeRootPath, fileTransactionLogFileList, null);

                result = new SimpleFilesTransactionLogBackend(storageTableSchemas, fileTransactionLogFiles);
            }
            finally {

                if (fileTransactionLogFileList != null) {

                    logFileIndexListAllocator.freeImmutable(fileTransactionLogFileList);
                }
            }
        }
        finally {

            if (transactionFilePaths != null) {

                relativeFilePathIndexListAllocator.freeImmutable(transactionFilePaths);
            }

            relativeFilePathIndexListAllocator.freeBuilder(transactionFilePathsBuilder);
        }

        return result;
    }

    private static ICachedIndexList<FileTransactionLogFile> readTransactionLogFiles(IRelativeFileSystemAccess fileSystemAccess,
            IIndexList<RelativeFilePath> transactionLogFilePaths, ICachedIndexListAllocator<FileTransactionLogFile> indexListAllocator) throws IOException {

        final long numElements = transactionLogFilePaths.getNumElements();
        final int initialCapacity = IOnlyElementsView.intNumElements(transactionLogFilePaths);

        final ICachedIndexList<FileTransactionLogFile> result;

        final ICachedIndexListBuilder<FileTransactionLogFile> fileTransactionLogFileList = indexListAllocator.createBuilder(initialCapacity);

        try {
            for (long i = 0L; i < numElements; ++ i) {

                final RelativeFilePath transactionLogFilePath = transactionLogFilePaths.get(i);

                final FileTransactionLogFile fileTransactionLogFile = FileTransactionLogFile.read(fileSystemAccess, transactionLogFilePath);

                fileTransactionLogFileList.addTail(fileTransactionLogFile);
            }

            result = fileTransactionLogFileList.buildOrEmpty();
        }
        finally {

            indexListAllocator.freeBuilder(fileTransactionLogFileList);
        }

        return result;
    }
}
