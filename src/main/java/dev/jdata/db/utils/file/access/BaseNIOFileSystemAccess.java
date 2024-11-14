package dev.jdata.db.utils.file.access;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dev.jdata.db.utils.adt.lists.Lists;
import dev.jdata.db.utils.paths.PathIOUtil;

public abstract class BaseNIOFileSystemAccess implements AbsoluteFileSystemAccess{

    private final FileSystem fileSystem;

    protected BaseNIOFileSystemAccess(FileSystem fileSystem) {

        this.fileSystem = Objects.requireNonNull(fileSystem);
    }

    @Override
    public final boolean exists(AbsoluteDirectoryPath directoryPath) {

        final Path nioPath = checkFileSystemNIO(directoryPath);

        return Files.exists(nioPath);
    }

    @Override
    public final boolean exists(AbsoluteFilePath filePath) {

        final Path nioPath = checkFileSystemNIO(filePath);

        return Files.exists(nioPath);
    }

    @Override
    public final boolean isDirectory(AbsoluteDirectoryPath directoryPath) {

        final Path nioPath = checkFileSystemNIO(directoryPath);

        return Files.isDirectory(nioPath);
    }

    @Override
    public final void createDirectories(AbsoluteDirectoryPath directoryPath) throws IOException {

        final Path nioPath = checkFileSystemNIO(directoryPath);

        Files.createDirectories(nioPath);
    }

    @Override
    public final List<AbsoluteFilePath> listFilePaths(AbsoluteDirectoryPath directoryPath) throws IOException {

        final Path nioPath = checkFileSystemNIO(directoryPath);

        final List<Path> paths = PathIOUtil.listPaths(nioPath, Files::isRegularFile);

        return Collections.unmodifiableList(Lists.map(paths, p -> AbsoluteFilePath.of(nioPath.relativize(p))));
    }

    @Override
    public final void moveAtomically(AbsoluteFilePath srcFilePath, AbsoluteFilePath dstFilePath) throws IOException {

        final Path chrootedSrcPath = checkFileSystemNIO(srcFilePath);
        final Path chrootedDstPath = checkFileSystemNIO(dstFilePath);

        Files.move(chrootedSrcPath, chrootedDstPath, StandardCopyOption.ATOMIC_MOVE);
    }

    @Override
    public final void deleteIfExists(AbsoluteFilePath filePath) throws IOException {

        final Path nioPath = checkFileSystemNIO(filePath);

        Files.deleteIfExists(nioPath);
    }

    @Override
    public final DataInputStream openDataInputStream(AbsoluteFilePath filePath) throws IOException {

        final Path nioPath = checkFileSystemNIO(filePath);

        return new DataInputStream(Files.newInputStream(nioPath));
    }

    @Override
    public final DataOutputStream openDataOutputStream(AbsoluteFilePath filePath) throws IOException {

        final Path nioPath = checkFileSystemNIO(filePath);

        return new DataOutputStream(Files.newOutputStream(nioPath));
    }

    @Override
    public final SequentialFileAccess openSequentialFileAccess(AbsoluteFilePath filePath, OpenMode openMode) throws IOException {

        checkFileSystem(filePath);

        return new SequentialFileAccessImpl<>(this, filePath);
    }

    @Override
    public final RandomFileAccess openRandomFileAccess(AbsoluteFilePath filePath, OpenMode openMode) throws IOException {

        final AbsoluteFilePath chrootedFilePath = checkFileSystem(filePath);

        return new RandomFileAccessImpl(this, chrootedFilePath, openMode);
    }

    @Override
    public FileOutputStream openFileOutputStream(AbsoluteFilePath filePath) throws IOException {

        final AbsoluteFilePath chrootedFilePath = checkFileSystem(filePath);

        return new FileOutputStream(chrootedFilePath.toFile());
    }

    private AbsoluteDirectoryPath checkFileSystem(AbsoluteDirectoryPath directoryPath) {

        checkFileSystemNIO(directoryPath.getPath());

        return directoryPath;
    }

    private AbsoluteFilePath checkFileSystem(AbsoluteFilePath filePath) {

        checkFileSystemNIO(filePath.getPath());

        return filePath;
    }

    private Path checkFileSystemNIO(AbsoluteDirectoryPath directoryPath) {

        return checkFileSystemNIO(directoryPath.getPath());
    }

    private Path checkFileSystemNIO(AbsoluteFilePath filePath) {

        return checkFileSystemNIO(filePath.getPath());
    }

    private Path checkFileSystemNIO(Path path) {

        if (!path.isAbsolute()) {

            throw new IllegalArgumentException();
        }

        if (!path.getFileSystem().equals(fileSystem)) {

            throw new IllegalArgumentException();
        }

        return path;
    }
}
