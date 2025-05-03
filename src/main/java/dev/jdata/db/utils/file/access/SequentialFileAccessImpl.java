package dev.jdata.db.utils.file.access;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Objects;

final class SequentialFileAccessImpl<F extends IFilePath, D extends IDirectoryPath<F, D>, A extends IFileSystemAccess<F, D>>
        extends BaseFileAccess
        implements SequentialFileAccess {

    private final FileOutputStream fileOutputStream;

    SequentialFileAccessImpl(A fileSystemAccess, F filePath) throws IOException {

        Objects.requireNonNull(filePath);

        this.fileOutputStream = fileSystemAccess.openFileOutputStream(filePath);
    }

    @Override
    public void close() throws IOException {

        fileOutputStream.close();
    }

    @Override
    public DataOutputStream getDataOutputStream() {

        return new DataOutputStream(fileOutputStream);
    }

    @Override
    FileChannel getFileChannel() {

        return fileOutputStream.getChannel();
    }
}
