package dev.jdata.db.utils.file.access;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;
import java.util.Objects;

import dev.jdata.db.utils.adt.IResettable;

final class PathImpl implements Path, IResettable {

    private AbsolutePath absolutePath;
    private FileSystem fileSystem;

    void initialize(AbsolutePath absolutePath, FileSystem fileSystem) {

        Objects.requireNonNull(absolutePath);
        Objects.requireNonNull(fileSystem);

        if (this.absolutePath != null) {

            throw new IllegalStateException();
        }

        this.absolutePath = absolutePath;
        this.fileSystem = fileSystem;
    }

    @Override
    public void reset() {

        checkInitialized();

        this.absolutePath = null;
        this.fileSystem = null;
    }

    private void checkInitialized() {

        if (absolutePath == null) {

            throw new IllegalStateException();
        }
    }

    @Override
    public boolean endsWith(String arg0) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Path> iterator() {

        throw new UnsupportedOperationException();
    }

    @Override
    public WatchKey register(WatchService arg0, Kind<?>... arg1) throws IOException {

        throw new UnsupportedOperationException();
    }

    @Override
    public Path resolve(String arg0) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Path resolveSibling(Path arg0) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Path resolveSibling(String arg0) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean startsWith(String arg0) {

        throw new UnsupportedOperationException();
    }

    @Override
    public File toFile() {

        throw new UnsupportedOperationException();
    }

    @Override
    public FileSystem getFileSystem() {

        checkInitialized();

        return fileSystem;
    }

    @Override
    public boolean isAbsolute() {

        checkInitialized();

        return true;
    }

    @Override
    public Path getRoot() {

        throw new UnsupportedOperationException();
    }

    @Override
    public Path getFileName() {

        throw new UnsupportedOperationException();
    }

    @Override
    public Path getParent() {

        throw new UnsupportedOperationException();
    }

    @Override
    public int getNameCount() {

        throw new UnsupportedOperationException();
    }

    @Override
    public Path getName(int index) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean startsWith(Path other) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean endsWith(Path other) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Path normalize() {

        throw new UnsupportedOperationException();
    }

    @Override
    public Path resolve(Path other) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Path relativize(Path other) {

        throw new UnsupportedOperationException();
    }

    @Override
    public URI toUri() {

        throw new UnsupportedOperationException();
    }

    @Override
    public Path toAbsolutePath() {

        throw new UnsupportedOperationException();
    }

    @Override
    public Path toRealPath(LinkOption... options) throws IOException {

        throw new UnsupportedOperationException();
    }

    @Override
    public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) throws IOException {

        throw new UnsupportedOperationException();
    }

    @Override
    public int compareTo(Path other) {

        throw new UnsupportedOperationException();
    }
}
