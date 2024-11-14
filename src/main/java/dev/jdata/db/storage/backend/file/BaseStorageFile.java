package dev.jdata.db.storage.backend.file;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.file.access.FileAccess;
import dev.jdata.db.utils.file.access.RelativeDirectoryPath;
import dev.jdata.db.utils.file.access.RelativeFilePath;

public abstract class BaseStorageFile<T extends FileAccess> implements Closeable {

    private static final boolean DEBUG = Boolean.FALSE;

    private static final Class<?> debugClass = BaseStorageFile.class;

    protected static int parseSequenceNo(String fileName, String fileNamePrefix) {

        Checks.isFileName(fileName);
        Checks.isFileNamePrefix(fileNamePrefix);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("fileName", fileName).add("fileNamePrefix", fileNamePrefix));
        }

        if (!fileName.startsWith(fileNamePrefix)) {

            throw new IllegalArgumentException();
        }

        final int fileNameLength = fileName.length();
        final int fileNamePrefixLength = fileNamePrefix.length();

        if (fileNameLength <= fileNamePrefixLength) {

            throw new IllegalArgumentException();
        }

        if (fileName.charAt(fileNamePrefixLength) != '.') {

            throw new IllegalArgumentException();
        }

        final int startOfSequenceNoIndex = fileNamePrefixLength + 1;
        final int numSequenceNoCharacters = fileNameLength - startOfSequenceNoIndex;

        if (numSequenceNoCharacters < 1) {

            throw new IllegalArgumentException();
        }

        final char startOfSequenceNoCharacter = fileName.charAt(startOfSequenceNoIndex);

        if (startOfSequenceNoCharacter == '0') {

            if (numSequenceNoCharacters > 1) {

                throw new IllegalArgumentException();
            }
        }
        else if (startOfSequenceNoCharacter < '1' || startOfSequenceNoCharacter > '9') {

            throw new IllegalArgumentException();
        }

        Checks.containsOnly(fileName, startOfSequenceNoIndex + 1, numSequenceNoCharacters - 1, c -> c >= '0' && c <= '9');

        final int result = Integer.parseUnsignedInt(fileName, fileNamePrefixLength + 1, fileNameLength, 10);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private final T file;

    protected BaseStorageFile(T file) {

        this.file = Objects.requireNonNull(file);
    }

    @Override
    public final void close() throws IOException {

        file.close();
    }

    protected final T getFile() {
        return file;
    }

    static RelativeFilePath constructPath(RelativeDirectoryPath directoryPath, String fileNamePrefix, long sequenceNo) {

        Objects.requireNonNull(directoryPath);
        Checks.isFileNamePrefix(fileNamePrefix);
        Checks.isSequenceNo(sequenceNo);

        return directoryPath.resolveFile(constructTableFileName(fileNamePrefix, sequenceNo).toString());
    }

    static RelativeFilePath constructTempPath(RelativeDirectoryPath directoryPath, String fileNamePrefix, long sequenceNo) {

        Objects.requireNonNull(directoryPath);
        Checks.isFileNamePrefix(fileNamePrefix);
        Checks.isSequenceNo(sequenceNo);

        return directoryPath.resolveFile(constructTempTableFileName(fileNamePrefix, sequenceNo));
    }

    private static String constructTempTableFileName(String fileNamePrefix, long sequenceNo) {

        return constructTableFileNameStringBuilder(fileNamePrefix, sequenceNo).append(".tmp").toString();
    }

    protected static String constructTableFileName(String fileNamePrefix, long sequenceNo) {

        Checks.isFileNamePrefix(fileNamePrefix);
        Checks.isSequenceNo(sequenceNo);

        return constructTableFileNameStringBuilder(fileNamePrefix, sequenceNo).toString();
    }

    static StringBuilder constructTableFileNameStringBuilder(String fileNamePrefix, long sequenceNo) {

        Checks.isFileNamePrefix(fileNamePrefix);
        Checks.isSequenceNo(sequenceNo);

        final StringBuilder sb = new StringBuilder(fileNamePrefix.length() + 20);

        sb.append(fileNamePrefix).append('.').append(sequenceNo);

        return sb;
    }
}
