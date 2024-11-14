package dev.jdata.db.storage.backend.transactionlog.backend.file.simple;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.jdata.db.storage.backend.StorageTableSchemas;
import dev.jdata.db.storage.backend.transactionlog.backend.file.BaseFileTransactionLogBackendFactory;
import dev.jdata.db.storage.backend.transactionlog.backend.file.FileTransactionLogBackendConfiguration;
import dev.jdata.db.storage.backend.transactionlog.backend.file.FileTransactionLogFile;
import dev.jdata.db.storage.backend.transactionlog.backend.file.FileTransactionLogFiles;
import dev.jdata.db.utils.paths.PathIOUtil;

public final class SimpleFilesTransactionLogBackendFactory
        extends BaseFileTransactionLogBackendFactory<FileTransactionLogBackendConfiguration, SimpleFilesTransactionLogBackend> {

    @Override
    public SimpleFilesTransactionLogBackend initialize(StorageTableSchemas storageTableSchemas, FileTransactionLogBackendConfiguration configuration) throws IOException {

        Objects.requireNonNull(storageTableSchemas);
        Objects.requireNonNull(configuration);

        final Path rootPath = configuration.getRootPath();

        final List<Path> transactionFilePaths = PathIOUtil.listFilePaths(rootPath);

        final List<FileTransactionLogFile> fileTransactionLogFileList = readTransactionLogFiles(transactionFilePaths);

        final FileTransactionLogFiles fileTransactionLogFiles = new FileTransactionLogFiles(rootPath, fileTransactionLogFileList);

        return new SimpleFilesTransactionLogBackend(storageTableSchemas, fileTransactionLogFiles);
    }

    private static List<FileTransactionLogFile> readTransactionLogFiles(List<Path> transactionLogFilePaths) throws IOException {

        final List<FileTransactionLogFile> fileTransactionLogFileList = new ArrayList<>(transactionLogFilePaths.size());

        for (Path transactionLogFilePath : transactionLogFilePaths) {

            final FileTransactionLogFile fileTransactionLogFile = FileTransactionLogFile.read(transactionLogFilePath);

            fileTransactionLogFileList.add(fileTransactionLogFile);
        }

        return fileTransactionLogFileList;
    }
}
