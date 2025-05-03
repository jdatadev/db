package dev.jdata.db.utils.file.access;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.BiConsumer;

import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;

final class RelativeFileSystemAccess implements IRelativeFileSystemAccess {

    private static final class ListPathsParameters<P> extends ObjectCacheNode implements IResettable {

        private P parameter;
        private BiConsumer<RelativeFilePath, P> consumer;
        private AbsoluteDirectoryPath rootPath;
        private IAbsoluteFileSystemAccess absoluteFileSystemAccess;

        void initialize(P parameter, BiConsumer<RelativeFilePath, P> consumer, AbsoluteDirectoryPath rootPath, IAbsoluteFileSystemAccess absoluteFileSystemAccess) {

            Objects.requireNonNull(consumer);
            Objects.requireNonNull(rootPath);
            Objects.requireNonNull(absoluteFileSystemAccess);

            if (this.consumer != null) {

                throw new IllegalStateException();
            }

            this.parameter = parameter;
            this.consumer = consumer;
            this.rootPath = rootPath;
            this.absoluteFileSystemAccess = absoluteFileSystemAccess;
        }

        @Override
        public void reset() {

            this.parameter = null;
            this.consumer = null;
            this.rootPath = null;
            this.absoluteFileSystemAccess = null;
        }
    }

    private final AbsoluteDirectoryPath rootPath;
    private final IAbsoluteFileSystemAccess delegate;
    private final IPathAllocator<AbsoluteFilePath, AbsoluteDirectoryPath> absolutePathAllocator;
    private final IPathAllocator<RelativeFilePath, RelativeDirectoryPath> relativePathAllocator;
    private final IPathObjectsAllocator pathObjectsAllocator;

    private final NodeObjectCache<ListPathsParameters<?>> listPathsParametersCache;

    RelativeFileSystemAccess(AbsoluteDirectoryPath rootPath, IAbsoluteFileSystemAccess delegate) {
        this(rootPath, delegate, HeapAbsolutePathAllocator.INSTANCE, HeapRelativePathAllocator.INSTANCE, IPathObjectsAllocator.HEAP_ALLOCATION);
    }

    RelativeFileSystemAccess(AbsoluteDirectoryPath rootPath, BaseNIOFileSystemAccess delegate) {
        this(rootPath, delegate, delegate.getAbsolutePathAllocator(), delegate.getRelativePathAllocator(), delegate.getPathObjectsAllocator());
    }

    RelativeFileSystemAccess(AbsoluteDirectoryPath rootPath, IAbsoluteFileSystemAccess delegate, IPathAllocator<AbsoluteFilePath, AbsoluteDirectoryPath> absolutePathAllocator,
            IPathAllocator<RelativeFilePath, RelativeDirectoryPath> relativePathAllocator, IPathObjectsAllocator pathObjectsAllocator) {

        this.rootPath = Objects.requireNonNull(rootPath);
        this.delegate = Objects.requireNonNull(delegate);
        this.absolutePathAllocator = Objects.requireNonNull(absolutePathAllocator);
        this.relativePathAllocator = Objects.requireNonNull(relativePathAllocator);
        this.pathObjectsAllocator = Objects.requireNonNull(pathObjectsAllocator);

        this.listPathsParametersCache = new NodeObjectCache<>(ListPathsParameters::new);
    }

    @Override
    public RelativeDirectoryPath directoryPathOf(CharSequence pathName) {

        Checks.isPathName(pathName);

        final RelativeDirectoryPath result = relativePathAllocator.allocateDirectoryPath();

        result.initialize(pathName, pathObjectsAllocator);

        return result;
    }

    @Override
    public RelativeDirectoryPath directoryPathOf(CharSequence... pathNames) {

        Checks.isNotEmpty(pathNames);
        Checks.areElements(pathNames, Checks::checkIsPathName);

        final RelativeDirectoryPath result = relativePathAllocator.allocateDirectoryPath();

        result.initialize(pathNames, pathObjectsAllocator);

        return result;
    }

    @Override
    public RelativeDirectoryPath directoryPathOf(Path path) {

        Objects.requireNonNull(path);

        final RelativeDirectoryPath result = relativePathAllocator.allocateDirectoryPath();

        result.initialize(path, pathObjectsAllocator);

        return result;
    }

    @Override
    public RelativeFilePath filePathOf(CharSequence pathName) {

        Checks.isPathName(pathName);

        final RelativeFilePath result = relativePathAllocator.allocateFilePath();

        result.initialize(pathName, pathObjectsAllocator);

        return result;
    }

    @Override
    public RelativeFilePath filePathOf(CharSequence... pathNames) {

        Checks.isNotEmpty(pathNames);
        Checks.areElements(pathNames, Checks::checkIsPathName);

        final RelativeFilePath result = relativePathAllocator.allocateFilePath();

        result.initialize(pathNames, pathObjectsAllocator);

        return result;
    }

    @Override
    public RelativeDirectoryPath resolveDirectory(RelativeDirectoryPath directoryPath, CharSequence directoryName) {

        Objects.requireNonNull(directoryPath);
        Checks.isDirectoryName(directoryName);

        final RelativeDirectoryPath result = relativePathAllocator.allocateDirectoryPath();

        result.initialize(directoryPath, directoryName, pathObjectsAllocator);

        return result;
    }

    @Override
    public RelativeFilePath resolveFile(RelativeDirectoryPath directoryPath, CharSequence fileName) {

        Objects.requireNonNull(directoryPath);
        Checks.isFileName(fileName);

        final RelativeFilePath result = relativePathAllocator.allocateFilePath();

        result.initialize(directoryPath, fileName, pathObjectsAllocator);

        return result;
    }

    @Override
    public boolean exists(RelativeDirectoryPath directoryPath) {

        Objects.requireNonNull(directoryPath);

        final boolean result;

        final AbsoluteDirectoryPath absoluteDirectoryPath = toAbsoluteDirectoryPath(directoryPath);

        try {
            result = delegate.exists(absoluteDirectoryPath);
        }
        finally {

            freeDirectoryPath(absoluteDirectoryPath);
        }

        return result;
    }

    @Override
    public boolean exists(RelativeFilePath filePath) {

        Objects.requireNonNull(filePath);

        final boolean result;

        final AbsoluteFilePath absoluteFilePath = toAbsoluteFilePath(filePath);

        try {
            result = delegate.exists(absoluteFilePath);
        }
        finally {

            freeFilePath(absoluteFilePath);
        }

        return result;
    }

    @Override
    public boolean isDirectory(RelativeDirectoryPath directoryPath) {

        Objects.requireNonNull(directoryPath);

        final boolean result;

        final AbsoluteDirectoryPath absoluteDirectoryPath = toAbsoluteDirectoryPath(directoryPath);

        try {
            result = delegate.isDirectory(absoluteDirectoryPath);
        }
        finally {

            freeDirectoryPath(absoluteDirectoryPath);
        }

        return result;
    }

    @Override
    public void createDirectory(RelativeDirectoryPath directoryPath) throws IOException {

        Objects.requireNonNull(directoryPath);

        final AbsoluteDirectoryPath absoluteDirectoryPath = toAbsoluteDirectoryPath(directoryPath);

        try {
            delegate.createDirectory(absoluteDirectoryPath);
        }
        finally {

            freeDirectoryPath(absoluteDirectoryPath);
        }
    }

    @Override
    public void createDirectories(RelativeDirectoryPath directoryPath) throws IOException {

        Objects.requireNonNull(directoryPath);

        final AbsoluteDirectoryPath absoluteDirectoryPath = toAbsoluteDirectoryPath(directoryPath);

        try {
            delegate.createDirectories(absoluteDirectoryPath);
        }
        finally {

            freeDirectoryPath(absoluteDirectoryPath);
        }
    }

    @Override
    public <P, R> void listFilePaths(RelativeDirectoryPath directoryPath, P parameter, BiConsumer<RelativeFilePath, P> consumer) throws IOException {

        Objects.requireNonNull(directoryPath);
        Objects.requireNonNull(consumer);

        final ListPathsParameters<P> parameters;

        final NodeObjectCache<ListPathsParameters<?>> cache = listPathsParametersCache;

        synchronized (cache) {

            @SuppressWarnings("unchecked")
            final ListPathsParameters<P> p = (ListPathsParameters<P>)cache.allocate();

            parameters = p;
        }

        parameters.initialize(parameter, consumer, rootPath, delegate);

        final AbsoluteDirectoryPath absoluteDirectoryPath = toAbsoluteDirectoryPath(directoryPath);

        try {
            delegate.listFilePaths(absoluteDirectoryPath, parameters, (path, p) -> {

                final RelativeFilePath relativeFilePath = p.absoluteFileSystemAccess.relativize(p.rootPath, path);

                p.consumer.accept(relativeFilePath, p.parameter);
            });
        }
        finally {

            freeDirectoryPath(absoluteDirectoryPath);

            synchronized (cache) {

                cache.free(parameters);
            }
        }
    }

    @Override
    public void moveAtomically(RelativeFilePath srcFilePath, RelativeFilePath dstFilePath) throws IOException {

        Objects.requireNonNull(srcFilePath);
        Objects.requireNonNull(dstFilePath);

        final AbsoluteFilePath absoluteSrcFilePath = toAbsoluteFilePath(srcFilePath);
        final AbsoluteFilePath absoluteDstFilePath = toAbsoluteFilePath(dstFilePath);

        try {
            delegate.moveAtomically(absoluteSrcFilePath, absoluteDstFilePath);
        }
        finally {

            freeFilePath(absoluteSrcFilePath);
            freeFilePath(absoluteDstFilePath);
        }
    }

    @Override
    public void deleteIfExists(RelativeFilePath filePath) throws IOException {

        Objects.requireNonNull(filePath);

        final AbsoluteFilePath absoluteFilePath = toAbsoluteFilePath(filePath);

        try {
            delegate.deleteIfExists(absoluteFilePath);
        }
        finally {

            freeFilePath(absoluteFilePath);
        }
    }

    @Override
    public DataInputStream openDataInputStream(RelativeFilePath filePath) throws IOException {

        Objects.requireNonNull(filePath);

        final DataInputStream result;

        final AbsoluteFilePath absoluteFilePath = toAbsoluteFilePath(filePath);

        try {
            result = delegate.openDataInputStream(absoluteFilePath);
        }
        finally {

            freeFilePath(absoluteFilePath);
        }

        return result;
    }

    @Override
    public DataOutputStream openDataOutputStream(RelativeFilePath filePath) throws IOException {

        Objects.requireNonNull(filePath);

        final DataOutputStream result;

        final AbsoluteFilePath absoluteFilePath = toAbsoluteFilePath(filePath);

        try {
            result = delegate.openDataOutputStream(absoluteFilePath);
        }
        finally {

            freeFilePath(absoluteFilePath);
        }

        return result;
    }

    @Override
    public FileOutputStream openFileOutputStream(RelativeFilePath filePath) throws IOException {

        Objects.requireNonNull(filePath);

        final FileOutputStream result;

        final AbsoluteFilePath absoluteFilePath = toAbsoluteFilePath(filePath);

        try {
            result = delegate.openFileOutputStream(absoluteFilePath);
        }
        finally {

            freeFilePath(absoluteFilePath);
        }

        return result;
    }

    @Override
    public FileChannel openFileChannel(RelativeFilePath filePath, OpenMode openMode) throws IOException {

        Objects.requireNonNull(filePath);
        Objects.requireNonNull(openMode);

        final FileChannel result;

        final AbsoluteFilePath absoluteFilePath = toAbsoluteFilePath(filePath);

        try {
            result = delegate.openFileChannel(absoluteFilePath, openMode);
        }
        finally {

            freeFilePath(absoluteFilePath);
        }

        return result;
    }

    @Override
    public SequentialFileAccess openSequentialFileAccess(RelativeFilePath filePath, OpenMode openMode) throws IOException {

        Objects.requireNonNull(filePath);
        Objects.requireNonNull(openMode);

        final SequentialFileAccess result;

        final AbsoluteFilePath absoluteFilePath = toAbsoluteFilePath(filePath);

        try {
            result = delegate.openSequentialFileAccess(absoluteFilePath, openMode);
        }
        finally {

            freeFilePath(absoluteFilePath);
        }

        return result;
    }

    @Override
    public RandomFileAccess openRandomFileAccess(RelativeFilePath filePath, OpenMode openMode) throws IOException {

        Objects.requireNonNull(filePath);
        Objects.requireNonNull(openMode);

        final RandomFileAccess result;

        final AbsoluteFilePath absoluteFilePath = toAbsoluteFilePath(filePath);

        try {
            result = delegate.openRandomFileAccess(absoluteFilePath, openMode);
        }
        finally {

            freeFilePath(absoluteFilePath);
        }

        return result;
    }

    private AbsoluteDirectoryPath toAbsoluteDirectoryPath(RelativeDirectoryPath relativeDirectoryPath) {

        return delegate.append(rootPath, relativeDirectoryPath);
    }

    private AbsoluteFilePath toAbsoluteFilePath(RelativeFilePath relativeFilePath) {

        return delegate.append(rootPath, relativeFilePath);
    }

    private void freeDirectoryPath(AbsoluteDirectoryPath absoluteDirectoryPath) {

        absolutePathAllocator.freeDirectoryPath(absoluteDirectoryPath);
    }

    private void freeFilePath(AbsoluteFilePath absoluteFilePath) {

        absolutePathAllocator.freeFilePath(absoluteFilePath);
    }
}
