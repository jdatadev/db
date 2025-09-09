package dev.jdata.db.utils.file.access;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Objects;

import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.strings.Strings;
import dev.jdata.db.utils.checks.Checks;

abstract class AbsolutePath extends BasePath {

    private static final Boolean UTILIZE_PATH_IMPL = false;

    private PathImpl pathImpl;
    private Path nioPath;

    final void initialize(CharSequence pathName, FileSystem fileSystem, IPathObjectsAllocator pathObjectsAllocator) {

        Checks.isPathName(pathName);
        Objects.requireNonNull(fileSystem);
        Objects.requireNonNull(pathObjectsAllocator);

        super.initialize(pathName, pathObjectsAllocator);

        initializePathImpl(pathName, fileSystem, pathObjectsAllocator);
    }

    final void initialize(CharSequence[] pathNames, FileSystem fileSystem, IPathObjectsAllocator pathObjectsAllocator) {

        Checks.isNotEmpty(pathNames);
        Checks.areElements(pathNames, Checks::checkIsPathName);
        Objects.requireNonNull(fileSystem);
        Objects.requireNonNull(pathObjectsAllocator);

        super.initialize(pathNames, pathObjectsAllocator);

        initializePathImpl(fileSystem, pathObjectsAllocator);
    }

    final void initialize(String[] pathNames, CharSequence additionalPathName, FileSystem fileSystem, IPathObjectsAllocator pathObjectsAllocator) {

        Checks.isNotEmpty(pathNames);
        Checks.areElements(pathNames, Checks::checkIsPathName);
        Checks.isPathName(additionalPathName);
        Objects.requireNonNull(pathObjectsAllocator);

        super.initialize(pathNames, additionalPathName, pathObjectsAllocator);

        initializePathImpl(fileSystem, pathObjectsAllocator);
    }

    final void initialize(AbsolutePath path, BasePath additionalPath, IPathObjectsAllocator pathObjectsAllocator) {

        Objects.requireNonNull(path);
        Objects.requireNonNull(additionalPath);
        Objects.requireNonNull(pathObjectsAllocator);

        super.initialize(path, additionalPath, pathObjectsAllocator);

        initializePathImpl(getFileSystem(path), pathObjectsAllocator);
    }

    final void initialize(AbsolutePath path, CharSequence additionalPathName, IPathObjectsAllocator pathObjectsAllocator) {

        Objects.requireNonNull(path);
        Checks.isPathName(additionalPathName);
        Objects.requireNonNull(pathObjectsAllocator);

        super.initialize(path, additionalPathName, pathObjectsAllocator);

        initializePathImpl(getFileSystem(path), pathObjectsAllocator);
    }

    final void initialize(Path path, IPathObjectsAllocator pathObjectsAllocator) {

        Objects.requireNonNull(path);
        Objects.requireNonNull(pathObjectsAllocator);

        super.initialize(path, pathObjectsAllocator);

        initializePathImpl(path.getFileSystem(), pathObjectsAllocator);
    }

    final <P> void relativize(AbsolutePath path, AbsolutePath subPath, IPathObjectsAllocator pathArrayAllocator) {

        super.relativize(path, subPath, pathArrayAllocator);

        final FileSystem fileSystem = getFileSystem(path);

        if (!fileSystem.equals(getFileSystem(subPath))) {

            throw new IllegalArgumentException();
        }

        initializePathImpl(fileSystem, pathArrayAllocator);
    }

    final void reset(IPathImplAllocator pathImplAllocator) {

        Objects.requireNonNull(pathImplAllocator);

        if (UTILIZE_PATH_IMPL) {

            final PathImpl path = Initializable.checkResettable(pathImpl);

            pathImplAllocator.freePathImpl(path);

            this.pathImpl = null;
        }
    }

    final Path getPath() {

        final Path result;

        if (UTILIZE_PATH_IMPL) {

            result = Initializable.checkIsInitialized(pathImpl);
        }
        else {
            result = nioPath;
        }

        return result;
    }

    @Deprecated // utilize MutableFile for avoiding allocation
    final File toFile() {

        return new File(asString(true));
    }

    private void initializePathImpl(FileSystem fileSystem, IPathImplAllocator pathImplAllocator) {

        initializePathImpl(UTILIZE_PATH_IMPL ? null : asString(true), fileSystem, pathImplAllocator);
    }

    private void initializePathImpl(CharSequence pathName, FileSystem fileSystem, IPathImplAllocator pathImplAllocator) {

        if (UTILIZE_PATH_IMPL) {

            if (this.pathImpl != null) {

                throw new IllegalStateException();
            }

            final PathImpl path = this.pathImpl = Initializable.checkNotYetInitialized(pathImpl, pathImplAllocator.allocatePathImpl());

            path.initialize(this, fileSystem);
        }
        else {
            this.nioPath = fileSystem.getPath(Strings.of(pathName));
        }
    }

    private static FileSystem getFileSystem(AbsolutePath absolutePath) {

        return UTILIZE_PATH_IMPL
                ? Initializable.checkIsInitialized(absolutePath.pathImpl).getFileSystem()
                : Initializable.checkIsInitialized(absolutePath.nioPath).getFileSystem();
    }
}
