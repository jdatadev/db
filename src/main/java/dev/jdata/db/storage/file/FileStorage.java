package dev.jdata.db.storage.file;

import java.util.Objects;

import dev.jdata.db.storage.backend.file.BaseStorageFile;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.file.access.RelativeDirectoryPath;
import dev.jdata.db.utils.file.access.RelativeFilePath;
import dev.jdata.db.utils.scalars.Integers;

public abstract class FileStorage extends ObjectCacheNode {

    private static final boolean DEBUG = Boolean.FALSE;

    private static final Class<?> debugClass = BaseStorageFile.class;

    public static int parseSequenceNo(CharSequence fileName, String fileNamePrefix) {

        Checks.isFileName(fileName);
        Checks.isFileNamePrefix(fileNamePrefix);
        Checks.startsWith(fileName, fileNamePrefix);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("fileName", fileName).add("fileNamePrefix", fileNamePrefix));
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

        final int result = Integers.parseUnsignedInt(fileName, fileNamePrefixLength + 1, fileNameLength, 10);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    public static RelativeFilePath constructPath(IRelativeFileSystemAccess fileSystemAccess, RelativeDirectoryPath directoryPath, String fileNamePrefix, long sequenceNo) {

        Objects.requireNonNull(fileSystemAccess);
        Objects.requireNonNull(directoryPath);
        Checks.isFileNamePrefix(fileNamePrefix);
        Checks.isSequenceNo(sequenceNo);

        return fileSystemAccess.resolveFile(directoryPath, constructTableFileName(fileNamePrefix, sequenceNo).toString());
    }

    public static RelativeFilePath constructTempPath(IRelativeFileSystemAccess fileSystemAccess, RelativeDirectoryPath directoryPath, String fileNamePrefix, long sequenceNo) {

        Objects.requireNonNull(fileSystemAccess);
        Objects.requireNonNull(directoryPath);
        Checks.isFileNamePrefix(fileNamePrefix);
        Checks.isSequenceNo(sequenceNo);

        return fileSystemAccess.resolveFile(directoryPath, constructTempFileName(fileNamePrefix, sequenceNo));
    }

    private static String constructTempFileName(String fileNamePrefix, long sequenceNo) {

        return constructFileNameStringBuilder(fileNamePrefix, sequenceNo).append(".tmp").toString();
    }

    protected static String constructTableFileName(String fileNamePrefix, long sequenceNo) {

        Checks.isFileNamePrefix(fileNamePrefix);
        Checks.isSequenceNo(sequenceNo);

        return constructFileNameStringBuilder(fileNamePrefix, sequenceNo).toString();
    }

    protected static StringBuilder constructFileNameStringBuilder(String fileNamePrefix, long sequenceNo) {

        Checks.isFileNamePrefix(fileNamePrefix);
        Checks.isSequenceNo(sequenceNo);

        final StringBuilder sb = new StringBuilder(fileNamePrefix.length() + 20);

        sb.append(fileNamePrefix).append('.').append(sequenceNo);

        return sb;
    }

    protected FileStorage(AllocationType allocationType) {
        super(allocationType);
    }
}
