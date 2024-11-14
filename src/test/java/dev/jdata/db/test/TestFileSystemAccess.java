package dev.jdata.db.test;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import dev.jdata.db.utils.file.access.AbsoluteDirectoryPath;
import dev.jdata.db.utils.file.access.BaseNIOFileSystemAccess;
import dev.jdata.db.utils.file.access.RelativeFileSystemAccess;
import dev.jdata.db.utils.paths.PathIOUtil;

public final class TestFileSystemAccess extends BaseNIOFileSystemAccess implements Closeable {

    public static RelativeFileSystemAccess create() throws IOException {

        final TestFileSystemAccess testFileSystemAccess = createTestFileSystemAccess();

        return create(testFileSystemAccess);
    }

    public static RelativeFileSystemAccess create(TestFileSystemAccess testFileSystemAccess) throws IOException {

        Objects.requireNonNull(testFileSystemAccess);

        return RelativeFileSystemAccess.create(testFileSystemAccess.rootDirectoryPath, testFileSystemAccess);
    }

    public static TestFileSystemAccess createTestFileSystemAccess() throws IOException {

        final Path tempDirectory = Files.createTempDirectory("dbtest");

        return new TestFileSystemAccess(tempDirectory);
    }

    private final Path tempDirectory;
    private final AbsoluteDirectoryPath rootDirectoryPath;

    private TestFileSystemAccess(Path tempDirectory) throws IOException {
        super(tempDirectory.getFileSystem());

        this.tempDirectory = Objects.requireNonNull(tempDirectory);

        this.rootDirectoryPath = AbsoluteDirectoryPath.of(tempDirectory);
    }

    @Override
    public void close() throws IOException {

        PathIOUtil.deleteRecursively(tempDirectory);
    }

    public AbsoluteDirectoryPath getRootDirectoryPath() {
        return rootDirectoryPath;
    }
}
