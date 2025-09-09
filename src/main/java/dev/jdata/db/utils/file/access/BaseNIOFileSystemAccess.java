package dev.jdata.db.utils.file.access;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.function.BiConsumer;

import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.paths.PathIOUtil;
import dev.jdata.db.utils.paths.PathUtil;

public abstract class BaseNIOFileSystemAccess implements IAbsoluteFileSystemAccess {

    private final FileSystem fileSystem;
    private final IPathAllocator<AbsoluteFilePath, AbsoluteDirectoryPath> absolutePathAllocator;
    private final IPathAllocator<RelativeFilePath, RelativeDirectoryPath> relativePathAllocator;
    private final IPathObjectsAllocator pathObjectsAllocator;

    private NodeObjectCache<ListPathParameters> listPathParametersCache;

    protected BaseNIOFileSystemAccess(FileSystem fileSystem) {
        this(fileSystem, HeapAbsolutePathAllocator.INSTANCE, HeapRelativePathAllocator.INSTANCE, HeapPathObjectsAllocator.INSTANCE);
    }

    protected BaseNIOFileSystemAccess(FileSystem fileSystem, IPathAllocator<AbsoluteFilePath, AbsoluteDirectoryPath> absolutePathAllocator,
            IPathAllocator<RelativeFilePath, RelativeDirectoryPath> relativePathAllocator, IPathObjectsAllocator pathObjectsAllocator) {

        this.fileSystem = Objects.requireNonNull(fileSystem);
        this.absolutePathAllocator = Objects.requireNonNull(absolutePathAllocator);
        this.relativePathAllocator = Objects.requireNonNull(relativePathAllocator);
        this.pathObjectsAllocator = Objects.requireNonNull(pathObjectsAllocator);
    }

    @Override
    public final AbsoluteDirectoryPath directoryPathOf(CharSequence pathName) {

        Checks.isPathName(pathName);

        final AbsoluteDirectoryPath result = absolutePathAllocator.allocateDirectoryPath();

        result.initialize(pathName, pathObjectsAllocator);

        return result;
    }

    @Override
    public final AbsoluteDirectoryPath directoryPathOf(CharSequence ... pathNames) {

        Checks.isNotEmpty(pathNames);
        Checks.areElements(pathNames, Checks::checkIsPathName);

        final AbsoluteDirectoryPath result = absolutePathAllocator.allocateDirectoryPath();

        result.initialize(pathNames, pathObjectsAllocator);

        return result;
    }

    @Override
    public final AbsoluteDirectoryPath directoryPathOf(Path path) {

        Objects.requireNonNull(path);

        final AbsoluteDirectoryPath result = absolutePathAllocator.allocateDirectoryPath();

        result.initialize(path, pathObjectsAllocator);

        return result;
    }

    @Override
    public final AbsoluteFilePath filePathOf(CharSequence pathName) {

        Checks.isPathName(pathName);

        final AbsoluteFilePath result = absolutePathAllocator.allocateFilePath();

        result.initialize(pathName, pathObjectsAllocator);

        return result;
    }

    @Override
    public final AbsoluteFilePath filePathOf(CharSequence ... pathNames) {

        Checks.isNotEmpty(pathNames);
        Checks.areElements(pathNames, Checks::checkIsPathName);

        final AbsoluteFilePath result = absolutePathAllocator.allocateFilePath();

        result.initialize(pathNames, pathObjectsAllocator);

        return result;
    }

    @Override
    public final AbsoluteDirectoryPath resolveDirectory(AbsoluteDirectoryPath directoryPath, CharSequence directoryName) {

        Objects.requireNonNull(directoryPath);
        Checks.isDirectoryName(directoryName);

        final AbsoluteDirectoryPath result = absolutePathAllocator.allocateDirectoryPath();

        result.initialize(directoryPath, directoryName, pathObjectsAllocator);

        return result;
    }

    @Override
    public final AbsoluteFilePath resolveFile(AbsoluteDirectoryPath directoryPath, CharSequence fileName) {

        Objects.requireNonNull(directoryPath);
        Checks.isFileName(fileName);

        final AbsoluteFilePath result = absolutePathAllocator.allocateFilePath();

        result.initialize(directoryPath, fileName, pathObjectsAllocator);

        return result;
    }

    @Override
    public final AbsoluteDirectoryPath append(AbsoluteDirectoryPath absoluteDirectoryPath, RelativeDirectoryPath relativeDirectoryPath) {

        Objects.requireNonNull(absoluteDirectoryPath);
        Objects.requireNonNull(relativeDirectoryPath);

        final AbsoluteDirectoryPath result = absolutePathAllocator.allocateDirectoryPath();

        result.initialize(absoluteDirectoryPath, relativeDirectoryPath, pathObjectsAllocator);

        return result;
    }

    @Override
    public final AbsoluteFilePath append(AbsoluteDirectoryPath absoluteDirectoryPath, RelativeFilePath relativeFilePath) {

        Objects.requireNonNull(absoluteDirectoryPath);
        Objects.requireNonNull(relativeFilePath);

        final AbsoluteFilePath result = absolutePathAllocator.allocateFilePath();

        result.initialize(absoluteDirectoryPath, relativeFilePath, pathObjectsAllocator);

        return result;
    }

    @Override
    public final RelativeDirectoryPath relativize(AbsoluteDirectoryPath absoluteDirectoryPath, AbsoluteDirectoryPath absoluteSubDirectoryPath) {

        Objects.requireNonNull(absoluteDirectoryPath);
        Objects.requireNonNull(absoluteSubDirectoryPath);

        final RelativeDirectoryPath result = relativePathAllocator.allocateDirectoryPath();

        result.relativize(absoluteDirectoryPath, absoluteSubDirectoryPath, pathObjectsAllocator);

        return result;
    }

    @Override
    public final RelativeFilePath relativize(AbsoluteDirectoryPath absoluteDirectoryPath, AbsoluteFilePath absoluteSubFilePath) {

        Objects.requireNonNull(absoluteDirectoryPath);
        Objects.requireNonNull(absoluteSubFilePath);

        final RelativeFilePath result = relativePathAllocator.allocateFilePath();

        result.relativize(absoluteDirectoryPath, absoluteSubFilePath, pathObjectsAllocator);

        return result;
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
    public final void createDirectory(AbsoluteDirectoryPath directoryPath) throws IOException {

        final Path nioPath = checkFileSystemNIO(directoryPath);

        Files.createDirectory(nioPath);
    }

    @Override
    public final void createDirectories(AbsoluteDirectoryPath directoryPath) throws IOException {

        final Path nioPath = checkFileSystemNIO(directoryPath);

        Files.createDirectories(nioPath);
    }

    private static final class ListPathParameters extends ObjectCacheNode {

        private Object parameter;
        private BiConsumer<AbsoluteFilePath, ?> consumer;

        <P> void initialize(P parameter, BiConsumer<AbsoluteFilePath, P> consumer) {

            Objects.requireNonNull(consumer);

            this.parameter = parameter;
            this.consumer = Initializable.checkNotYetInitialized(this.consumer, consumer);
        }

        void reset() {

            this.parameter = null;
            this.consumer = Initializable.checkResettable(consumer);
        }
    }

    @Override
    public final <P, R> void listFilePaths(AbsoluteDirectoryPath directoryPath, P parameter, BiConsumer<AbsoluteFilePath, P> consumer) throws IOException {

        Objects.requireNonNull(directoryPath);
        Objects.requireNonNull(consumer);

        final Path nioPath = checkFileSystemNIO(directoryPath);

        ListPathParameters listPathParameters = null;
        NodeObjectCache<ListPathParameters> cache = null;

        try {
            synchronized (this) {

                cache = listPathParametersCache;

                if (cache == null) {

                    cache = this.listPathParametersCache = new NodeObjectCache<>(ListPathParameters::new);
                }

                listPathParameters = cache.allocate();
            }

            listPathParameters.initialize(parameter, consumer);

            PathIOUtil.listPaths(nioPath, listPathParameters, (path, passedListPathParameters) -> Files.isRegularFile(path), (f, p) -> {

                final AbsoluteFilePath filePath = resolveFile(directoryPath, PathUtil.getFileName(f));

                @SuppressWarnings("unchecked")
                final P consumerParameter = (P)p.parameter;

                @SuppressWarnings("unchecked")
                final BiConsumer<AbsoluteFilePath, P> pathConsumer = (BiConsumer<AbsoluteFilePath, P>)p.consumer;

                pathConsumer.accept(filePath, consumerParameter);

                return null;
            });
        }
        finally {

            if (listPathParameters != null) {

                listPathParameters.reset();

                if (cache != null) {

                    synchronized (this) {

                        cache.free(listPathParameters);
                    }
                }
            }
        }
    }

    final IPathAllocator<AbsoluteFilePath, AbsoluteDirectoryPath> getAbsolutePathAllocator() {
        return absolutePathAllocator;
    }

    final IPathAllocator<RelativeFilePath, RelativeDirectoryPath> getRelativePathAllocator() {
        return relativePathAllocator;
    }

    final IPathObjectsAllocator getPathObjectsAllocator() {
        return pathObjectsAllocator;
    }

    final AbsoluteDirectoryPath allocateAbsoluteDirectoryPath() {

        return absolutePathAllocator.allocateDirectoryPath();
    }

    final AbsoluteFilePath allocateAbsoluteFilePath() {

        return absolutePathAllocator.allocateFilePath();
    }

    final RelativeDirectoryPath allocateRelativeDirectoryPath() {

        return relativePathAllocator.allocateDirectoryPath();
    }

    final RelativeFilePath allocateRelativeFilePath() {

        return relativePathAllocator.allocateFilePath();
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
    public final FileOutputStream openFileOutputStream(AbsoluteFilePath filePath) throws IOException {

        final AbsoluteFilePath chrootedFilePath = checkFileSystem(filePath);

        return new FileOutputStream(chrootedFilePath.toFile());
    }

    private static final StandardOpenOption[] READ_ONLY_OPEN_OPTIONS = new StandardOpenOption[] { StandardOpenOption.READ };

    private static final StandardOpenOption[] WRITE_ONLY_CREATE_FAIL_IF_EXISTS_OPEN_OPTIONS = new StandardOpenOption[] {

            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE_NEW
    };

    private static final StandardOpenOption[] READ_WRITE_CREATE_FAIL_IF_EXISTS_OPEN_OPTIONS = new StandardOpenOption[] {

            StandardOpenOption.READ,
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE_NEW
    };

    private static final StandardOpenOption[] READ_WRITE_CREATE_IF_NOT_EXISTS_OPEN_OPTIONS = new StandardOpenOption[] {

            StandardOpenOption.READ,
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE
    };

    private static final StandardOpenOption[] READ_WRITE_EXISTING_OPEN_OPTIONS = new StandardOpenOption[] {

            StandardOpenOption.READ,
            StandardOpenOption.WRITE,
    };

    private static final StandardOpenOption[] APPEND_EXISTING_OPEN_OPTIONS = new StandardOpenOption[] {

            StandardOpenOption.WRITE,
            StandardOpenOption.APPEND,
    };

    @Override
    public final FileChannel openFileChannel(AbsoluteFilePath filePath, OpenMode openMode) throws IOException {

        final OpenOption[] openOptions;

        switch (openMode) {

        case READ_ONLY:

            openOptions = READ_ONLY_OPEN_OPTIONS;
            break;

        case WRITE_ONLY_CREATE_FAIL_IF_EXISTS:

            openOptions = WRITE_ONLY_CREATE_FAIL_IF_EXISTS_OPEN_OPTIONS;
            break;

        case READ_WRITE_CREATE_FAIL_IF_EXISTS:

            openOptions = READ_WRITE_CREATE_FAIL_IF_EXISTS_OPEN_OPTIONS;
            break;

        case READ_WRITE_CREATE_IF_NOT_EXISTS:

            openOptions = READ_WRITE_CREATE_IF_NOT_EXISTS_OPEN_OPTIONS;
            break;

        case READ_WRITE_EXISTING:

            openOptions = READ_WRITE_EXISTING_OPEN_OPTIONS;
            break;

        case APPEND_EXISTING:

            openOptions = APPEND_EXISTING_OPEN_OPTIONS;
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return FileChannel.open(filePath.getPath(), openOptions);
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
