package dev.jdata.db.storage.backend.file;

import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.storage.file.FileStorage;
import dev.jdata.db.utils.adt.lists.ICachedMutableIndexList;
import dev.jdata.db.utils.adt.lists.ICachedMutableIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.file.access.FileAccess;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.file.access.RelativeDirectoryPath;
import dev.jdata.db.utils.file.access.RelativeFilePath;

public abstract class BaseStorageFiles<T extends FileAccess, U extends BaseStorageFile<T>> {

    private final IRelativeFileSystemAccess fileSystemAccess;
    private final RelativeDirectoryPath directoryPath;

    private final ICachedMutableIndexList<U> files;

    protected static int parseSequenceNo(String fileName, String fileNamePrefix) {

        Checks.isFileName(fileName);
        Checks.isFileNamePrefix(fileNamePrefix);

        return FileStorage.parseSequenceNo(fileName, fileNamePrefix);
    }

    protected abstract String getFileNamePrefix();

    protected BaseStorageFiles(IRelativeFileSystemAccess fileSystemAccess, RelativeDirectoryPath directoryPath, IIndexList<U> files,
            ICachedMutableIndexListAllocator<U> mutableIndexListAllocator) {

        Objects.requireNonNull(fileSystemAccess);
        Objects.requireNonNull(directoryPath);
        Objects.requireNonNull(files);

        this.fileSystemAccess = Objects.requireNonNull(fileSystemAccess);
        this.directoryPath = Objects.requireNonNull(directoryPath);
        this.files = mutableIndexListAllocator.copyToMutable(files);
    }

    protected final IRelativeFileSystemAccess getFileSystemAccess() {
        return fileSystemAccess;
    }

    protected final U getFile(int index) {

        Checks.isIntIndex(index);

        return files.get(index);
    }

    protected final void setFile(int index, U file) {

        Checks.isIntIndex(index);
        Objects.requireNonNull(file);

        files.set(index, file);
    }

    protected final U getLastFile() {

        return files.getTail();
    }

    protected final void addFile(U file) {

        Objects.requireNonNull(file);

        files.addTail(file);
    }

    protected final IIndexListView<U> getFiles() {

        return files;
    }

    public final void close(ICachedMutableIndexListAllocator<U> mutableIndexListAllocator) throws IOException {

        Objects.requireNonNull(mutableIndexListAllocator);

        IOException toThrow = null;

        try {
            final long numFiles = files.getNumElements();

            for (long i = 0L; i < numFiles; ++ i) {

                final U file = files.get(i);

                try {
                    file.close();
                }
                catch (IOException ex) {

                    if (toThrow != null) {

                        toThrow = ex;
                    }
                }
            }
        }
        finally {

            mutableIndexListAllocator.freeMutable(files);
        }

        if (toThrow != null) {

            throw toThrow;
        }
    }

    protected final RelativeFilePath constructPath(long sequenceNo) {

        Checks.isSequenceNo(sequenceNo);

        return FileStorage.constructPath(fileSystemAccess, directoryPath, getFileNamePrefix(), sequenceNo);
    }

    protected final RelativeFilePath constructTempPath(long sequenceNo) {

        Checks.isSequenceNo(sequenceNo);

        return FileStorage.constructTempPath(fileSystemAccess, directoryPath, getFileNamePrefix(), sequenceNo);
    }
}
