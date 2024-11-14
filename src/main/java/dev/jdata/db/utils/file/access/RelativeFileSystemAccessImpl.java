package dev.jdata.db.utils.file.access;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import dev.jdata.db.utils.adt.lists.Lists;

final class RelativeFileSystemAccessImpl implements RelativeFileSystemAccess {

    private final AbsoluteDirectoryPath rootPath;
    private final AbsoluteFileSystemAccess delegate;

    RelativeFileSystemAccessImpl(AbsoluteDirectoryPath rootPath, AbsoluteFileSystemAccess delegate) {

        this.rootPath = Objects.requireNonNull(rootPath);
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public boolean exists(RelativeDirectoryPath directoryPath) {

        return delegate.exists(toAbsoluteDirectoryPath(directoryPath));
    }

    @Override
    public boolean exists(RelativeFilePath filePath) {

        return delegate.exists(toAbsoluteFilePath(filePath));
    }

    @Override
    public boolean isDirectory(RelativeDirectoryPath directoryPath) {

        return delegate.isDirectory(toAbsoluteDirectoryPath(directoryPath));
    }

    @Override
    public void createDirectories(RelativeDirectoryPath directoryPath) throws IOException {

        delegate.createDirectories(toAbsoluteDirectoryPath(directoryPath));
    }

    @Override
    public List<RelativeFilePath> listFilePaths(RelativeDirectoryPath directoryPath) throws IOException {

        return Lists.unmodifiableOf(Lists.map(delegate.listFilePaths(toAbsoluteDirectoryPath(directoryPath)), p -> rootPath.relativize(p)));
    }

    @Override
    public void moveAtomically(RelativeFilePath srcFilePath, RelativeFilePath dstFilePath) throws IOException {

        delegate.moveAtomically(toAbsoluteFilePath(srcFilePath), toAbsoluteFilePath(dstFilePath));
    }

    @Override
    public void deleteIfExists(RelativeFilePath filePath) throws IOException {

        delegate.deleteIfExists(toAbsoluteFilePath(filePath));
    }

    @Override
    public DataInputStream openDataInputStream(RelativeFilePath filePath) throws IOException {

        return delegate.openDataInputStream(toAbsoluteFilePath(filePath));
    }

    @Override
    public DataOutputStream openDataOutputStream(RelativeFilePath filePath) throws IOException {

        return delegate.openDataOutputStream(toAbsoluteFilePath(filePath));
    }

    @Override
    public FileOutputStream openFileOutputStream(RelativeFilePath filePath) throws IOException {

        return delegate.openFileOutputStream(toAbsoluteFilePath(filePath));
    }

    @Override
    public SequentialFileAccess openSequentialFileAccess(RelativeFilePath filePath, OpenMode openMode) throws IOException {

        return delegate.openSequentialFileAccess(toAbsoluteFilePath(filePath), openMode);
    }

    @Override
    public RandomFileAccess openRandomFileAccess(RelativeFilePath path, OpenMode openMode) throws IOException {

        return delegate.openRandomFileAccess(toAbsoluteFilePath(path), openMode);
    }

    private AbsoluteDirectoryPath toAbsoluteDirectoryPath(RelativeDirectoryPath relativeDirectoryPath) {

        return rootPath.append(relativeDirectoryPath);
    }

    private AbsoluteFilePath toAbsoluteFilePath(RelativeFilePath relativeFilePath) {

        return rootPath.append(relativeFilePath);
    }
}
