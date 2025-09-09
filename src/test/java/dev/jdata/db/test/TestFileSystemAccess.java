package dev.jdata.db.test;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import dev.jdata.db.utils.file.access.AbsoluteDirectoryPath;
import dev.jdata.db.utils.file.access.IAbsoluteFileSystemAccess;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.paths.PathIOUtil;

public final class TestFileSystemAccess implements Closeable {

    public static TestFileSystemAccess create() throws IOException {

        final Path tempDirectory = Files.createTempDirectory("dbtest");

        return new TestFileSystemAccess(tempDirectory);
    }

    private final Path tempDirectory;
    private final AbsoluteDirectoryPath rootDirectoryPath;

    private TestFileSystemAccess(Path tempDirectory) throws IOException {

        this.tempDirectory = Objects.requireNonNull(tempDirectory);

        this.rootDirectoryPath = createAbsolute().directoryPathOf(tempDirectory);
    }

    @Override
    public void close() throws IOException {

        PathIOUtil.deleteRecursively(tempDirectory);
    }

    public Path getRootPath() {
        return tempDirectory;
    }

    public AbsoluteDirectoryPath getRootDirectoryPath() {
        return rootDirectoryPath;
    }

    public IAbsoluteFileSystemAccess createAbsolute() throws IOException {

        return IAbsoluteFileSystemAccess.ofHeapAllocated(tempDirectory.getFileSystem());
    }

    public IRelativeFileSystemAccess createRelative() throws IOException {

        return IRelativeFileSystemAccess.create(rootDirectoryPath, createAbsolute());
    }
}
