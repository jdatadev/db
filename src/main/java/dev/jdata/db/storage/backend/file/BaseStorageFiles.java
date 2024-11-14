package dev.jdata.db.storage.backend.file;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public abstract class BaseStorageFiles<T extends Closeable, U extends BaseStorageFile<T>> implements Closeable {

    private final Path directoryPath;

    private final List<U> files;

    protected static int parseSequenceNo(String fileName, String fileNamePrefix) {

        Checks.isFileName(fileName);
        Checks.isFileNamePrefix(fileNamePrefix);

        if (!fileName.startsWith(fileNamePrefix)) {

            throw new IllegalArgumentException();
        }

        return Integer.parseUnsignedInt(fileName, fileNamePrefix.length(), fileName.length(), 10);
    }

    protected abstract String getFileNamePrefix();

    protected BaseStorageFiles(Path directoryPath, List<U> files) {

        this.directoryPath = Objects.requireNonNull(directoryPath);
        this.files = new ArrayList<>(files);
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

    protected final Path constructPath(long sequenceNo) {

        Checks.isSequenceNo(sequenceNo);

        return directoryPath.resolve(constructTempPath(sequenceNo).toString());
    }

    protected final Path constructTempPath(long sequenceNo) {

        Checks.isSequenceNo(sequenceNo);

        return directoryPath.resolve(constructTableFileName(sequenceNo).append(".tmp").toString());
    }

    private StringBuilder constructTableFileName(long sequenceNo) {

        final String fileNamePrefix = getFileNamePrefix();

        final StringBuilder sb = new StringBuilder(fileNamePrefix.length() + 20);

        sb.append(fileNamePrefix).append('.').append(sequenceNo);

        return sb;
    }
}
