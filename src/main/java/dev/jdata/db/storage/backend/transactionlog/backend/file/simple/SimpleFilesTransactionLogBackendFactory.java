package dev.jdata.db.storage.backend.transactionlog.backend.file.simple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.jdata.db.storage.backend.tabledata.StorageTableSchemas;
import dev.jdata.db.storage.backend.transactionlog.backend.file.BaseFileTransactionLogBackendFactory;
import dev.jdata.db.storage.backend.transactionlog.backend.file.FileTransactionLogBackendConfiguration;
import dev.jdata.db.storage.backend.transactionlog.backend.file.FileTransactionLogFile;
import dev.jdata.db.storage.backend.transactionlog.backend.file.FileTransactionLogFiles;
import dev.jdata.db.utils.file.access.AbsoluteDirectoryPath;
import dev.jdata.db.utils.file.access.RelativeDirectoryPath;
import dev.jdata.db.utils.file.access.RelativeFilePath;
import dev.jdata.db.utils.file.access.RelativeFileSystemAccess;

public final class SimpleFilesTransactionLogBackendFactory
        extends BaseFileTransactionLogBackendFactory<FileTransactionLogBackendConfiguration, SimpleFilesTransactionLogBackend> {

    @Override
    public SimpleFilesTransactionLogBackend initialize(StorageTableSchemas storageTableSchemas, FileTransactionLogBackendConfiguration configuration) throws IOException {

        Objects.requireNonNull(storageTableSchemas);
        Objects.requireNonNull(configuration);

        final AbsoluteDirectoryPath rootPath = configuration.getRootPath();
        final RelativeFileSystemAccess fileSystemAccess = RelativeFileSystemAccess.create(rootPath, configuration.getFileSystemAccess());

        final RelativeDirectoryPath relativeRootPath = RelativeDirectoryPath.ROOT;

        final List<RelativeFilePath> transactionFilePaths = fileSystemAccess.listFilePaths(relativeRootPath);

        final List<FileTransactionLogFile> fileTransactionLogFileList = readTransactionLogFiles(fileSystemAccess, transactionFilePaths);

        final FileTransactionLogFiles fileTransactionLogFiles = new FileTransactionLogFiles(fileSystemAccess, relativeRootPath, fileTransactionLogFileList);

        return new SimpleFilesTransactionLogBackend(storageTableSchemas, fileTransactionLogFiles);
    }

    private static List<FileTransactionLogFile> readTransactionLogFiles(RelativeFileSystemAccess fileSystemAccess, List<RelativeFilePath> transactionLogFilePaths)
            throws IOException {

        final List<FileTransactionLogFile> fileTransactionLogFileList = new ArrayList<>(transactionLogFilePaths.size());

        for (RelativeFilePath transactionLogFilePath : transactionLogFilePaths) {

            final FileTransactionLogFile fileTransactionLogFile = FileTransactionLogFile.read(fileSystemAccess, transactionLogFilePath);

            fileTransactionLogFileList.add(fileTransactionLogFile);
        }

        return fileTransactionLogFileList;
    }
}
