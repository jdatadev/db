package dev.jdata.db.storage.backend.file;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.file.access.FileAccess;
import dev.jdata.db.utils.file.access.RelativeDirectoryPath;
import dev.jdata.db.utils.file.access.RelativeFilePath;
import dev.jdata.db.utils.file.access.RelativeFileSystemAccess;

public abstract class BaseStorageFiles<T extends FileAccess, U extends BaseStorageFile<T>> implements Closeable {

    private final RelativeFileSystemAccess fileSystemAccess;
    private final RelativeDirectoryPath directoryPath;

    private final List<U> files;

    protected static int parseSequenceNo(String fileName, String fileNamePrefix) {

        Checks.isFileName(fileName);
        Checks.isFileNamePrefix(fileNamePrefix);

        return BaseStorageFile.parseSequenceNo(fileName, fileNamePrefix);
    }

    protected abstract String getFileNamePrefix();

    protected BaseStorageFiles(RelativeFileSystemAccess fileSystemAccess, RelativeDirectoryPath directoryPath, List<U> files) {

        this.fileSystemAccess = Objects.requireNonNull(fileSystemAccess);
        this.directoryPath = Objects.requireNonNull(directoryPath);
        this.files = new ArrayList<>(files);
    }

    protected final RelativeFileSystemAccess getFileSystemAccess() {
        return fileSystemAccess;
    }

    protected final U getFile(int index) {

        Checks.isIndex(index);

        return files.get(index);
    }

    protected final void setFile(int index, U file) {

        Checks.isIndex(index);
        Objects.requireNonNull(file);

        files.set(index, file);
    }

    protected final U getLastFile() {

        return files.get(files.size() - 1);
    }

    protected final void addFile(U file) {

        Objects.requireNonNull(file);

        files.add(file);
    }

    protected final List<U> getFiles() {

        return Collections.unmodifiableList(files);
    }

    @Override
    public final void close() throws IOException {

        IOException toThrow = null;

        for (U file : files) {

            try {
                file.close();
            }
            catch (IOException ex) {

                if (toThrow != null) {

                    toThrow = ex;
                }
            }
        }

        if (toThrow != null) {

            throw toThrow;
        }
    }

    protected final RelativeFilePath constructPath(long sequenceNo) {

        Checks.isSequenceNo(sequenceNo);

        return BaseStorageFile.constructPath(directoryPath, getFileNamePrefix(), sequenceNo);
    }

    protected final RelativeFilePath constructTempPath(long sequenceNo) {

        Checks.isSequenceNo(sequenceNo);

        return BaseStorageFile.constructTempPath(directoryPath, getFileNamePrefix(), sequenceNo);
    }
}
