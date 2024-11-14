package dev.jdata.db.utils.file.access;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public interface FileSystemAccess<F extends FilePath, D extends DirectoryPath<F, D>> {

    enum OpenMode {

        READ_ONLY,
        READ_WRITE_CREATE,
        READ_WRITE_CREATE_IF_NOT_EXISTS,
        READ_WRITE_EXISTING,
        APPEND_EXISTING
    }

    boolean exists(D directoryPath);
    boolean exists(F filePath);

    boolean isDirectory(D directoryPath);

    void createDirectories(D directoryPath) throws IOException;

    List<F> listFilePaths(D directoryPath) throws IOException;

    void moveAtomically(F srcFilePath, F dstFilePath) throws IOException;

    void deleteIfExists(F filePath) throws IOException;

    DataInputStream openDataInputStream(F filePath) throws IOException;
    DataOutputStream openDataOutputStream(F filePath) throws IOException;

    FileOutputStream openFileOutputStream(F filePath) throws IOException;

    SequentialFileAccess openSequentialFileAccess(F filePath, OpenMode openMode) throws IOException;

    RandomFileAccess openRandomFileAccess(F path, OpenMode openMode) throws IOException;
}
