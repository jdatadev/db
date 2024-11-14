package dev.jdata.db.utils.file.access;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Objects;

import dev.jdata.db.utils.file.access.FileSystemAccess.OpenMode;

final class RandomFileAccessImpl extends BaseDataInputOutputFileAccess<RandomAccessFile> implements RandomFileAccess {

    RandomFileAccessImpl(AbsoluteFileSystemAccess fileSystemAccess, AbsoluteFilePath filePath, OpenMode openMode) throws FileNotFoundException {
        super(createRandomAccessFile(fileSystemAccess, filePath, openMode));
    }

    private static RandomAccessFile createRandomAccessFile(AbsoluteFileSystemAccess fileSystemAccess, AbsoluteFilePath filePath, OpenMode openMode)
            throws FileNotFoundException {

        Objects.requireNonNull(fileSystemAccess);
        Objects.requireNonNull(filePath);
        Objects.requireNonNull(openMode);

        final String mode;
        final Boolean fileExists;

        switch (openMode) {

        case READ_ONLY:

            mode = "r";
            fileExists = true;
            break;

        case READ_WRITE_CREATE:

            mode = "rw";
            fileExists = false;
            break;

        case READ_WRITE_CREATE_IF_NOT_EXISTS:

            mode = "rw";
            fileExists = null;
            break;

        case READ_WRITE_EXISTING:

            mode = "rw";
            fileExists = true;
            break;

        default:
            throw new UnsupportedOperationException();
        }

        if (fileExists != null) {

            if (fileSystemAccess.exists(filePath) != fileExists) {

                throw new IllegalArgumentException();
            }
        }

        return new RandomAccessFile(filePath.toFile(), mode);
    }

    @Override
    public long getFilePointer() throws IOException {

        return getFile().getFilePointer();
    }

    @Override
    public void seek(long offset) throws IOException {

        getFile().seek(offset);
    }

    @Override
    public long length() throws IOException {

        return getFile().length();
    }

    @Override
    public int read(byte[] buffer) throws IOException {

        return getFile().read(buffer);
    }

    @Override
    FileChannel getFileChannel() {

        return getFile().getChannel();
    }
}
