package dev.jdata.db.utils.file.access;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

abstract class AbsolutePath extends BasePath {

    private PathImpl pathImpl;

    final void initialize(CharSequence pathName, FileSystem fileSystem, IPathObjectsAllocator pathObjectsAllocator) {

        Checks.isPathName(pathName);
        Objects.requireNonNull(fileSystem);
        Objects.requireNonNull(pathObjectsAllocator);

        super.initialize(pathName, pathObjectsAllocator);

        initializePathImpl(fileSystem, pathObjectsAllocator);
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

        initializePathImpl(path.pathImpl.getFileSystem(), pathObjectsAllocator);
    }

    final void initialize(AbsolutePath path, CharSequence additionalPathName, IPathObjectsAllocator pathObjectsAllocator) {

        Objects.requireNonNull(path);
        Checks.isPathName(additionalPathName);
        Objects.requireNonNull(pathObjectsAllocator);

        super.initialize(path, additionalPathName, pathObjectsAllocator);

        initializePathImpl(path.pathImpl.getFileSystem(), pathObjectsAllocator);
    }

    final void initialize(Path path, IPathObjectsAllocator pathObjectsAllocator) {

        Objects.requireNonNull(path);
        Objects.requireNonNull(pathObjectsAllocator);

        super.initialize(path, pathObjectsAllocator);

        initializePathImpl(path.getFileSystem(), pathObjectsAllocator);
    }

    final <P> void relativize(AbsolutePath path, AbsolutePath subPath, IPathObjectsAllocator pathArrayAllocator) {

        super.relativize(path, subPath, pathArrayAllocator);

        final FileSystem fileSystem = path.pathImpl.getFileSystem();

        if (!fileSystem.equals(subPath.pathImpl.getFileSystem())) {

            throw new IllegalArgumentException();
        }

        initializePathImpl(fileSystem, pathArrayAllocator);
    }

    final void reset(IPathImplAllocator pathImplAllocator) {

        Objects.requireNonNull(pathImplAllocator);

        final PathImpl path = pathImpl;

        if (path == null) {

            throw new IllegalStateException();
        }

        pathImplAllocator.freePathImpl(path);

        this.pathImpl = null;
    }

    final Path getPath() {

        final Path result = pathImpl;

        if (result == null) {

            throw new IllegalStateException();
        }

        return pathImpl;
    }

    @Deprecated
    final File toFile() {

        return new File(asString(true));
    }


    private void initializePathImpl(FileSystem fileSystem, IPathImplAllocator pathImplAllocator) {

        if (this.pathImpl != null) {

            throw new IllegalStateException();
        }

        final PathImpl path = this.pathImpl = pathImplAllocator.allocatePathImpl();

        path.initialize(this, fileSystem);
    }
}
