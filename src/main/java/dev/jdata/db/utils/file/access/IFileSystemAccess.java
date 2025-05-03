package dev.jdata.db.utils.file.access;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.function.BiConsumer;

public interface IFileSystemAccess<F extends IFilePath, D extends IDirectoryPath<F, D>> extends IPathFactory<F, D> {

    public enum OpenMode {

        READ_ONLY,
        WRITE_ONLY_CREATE_FAIL_IF_EXISTS,
        READ_WRITE_CREATE_FAIL_IF_EXISTS,
        READ_WRITE_CREATE_IF_NOT_EXISTS,
        READ_WRITE_EXISTING,
        APPEND_EXISTING
    }

    boolean exists(D directoryPath);
    boolean exists(F filePath);

    boolean isDirectory(D directoryPath);

    void createDirectory(D directoryPath) throws IOException;

    void createDirectories(D directoryPath) throws IOException;

    <P, R> void listFilePaths(D directoryPath, P parameter, BiConsumer<F, P> consumer) throws IOException;

    void moveAtomically(F srcFilePath, F dstFilePath) throws IOException;

    void deleteIfExists(F filePath) throws IOException;

    DataInputStream openDataInputStream(F filePath) throws IOException;
    DataOutputStream openDataOutputStream(F filePath) throws IOException;

    FileOutputStream openFileOutputStream(F filePath) throws IOException;

    FileChannel openFileChannel(F filePath, OpenMode openMode) throws IOException;

    SequentialFileAccess openSequentialFileAccess(F filePath, OpenMode openMode) throws IOException;

    RandomFileAccess openRandomFileAccess(F filePath, OpenMode openMode) throws IOException;
}
