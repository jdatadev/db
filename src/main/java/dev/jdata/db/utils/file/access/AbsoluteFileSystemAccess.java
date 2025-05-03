package dev.jdata.db.utils.file.access;

import java.nio.file.FileSystem;

final class AbsoluteFileSystemAccess extends BaseNIOFileSystemAccess {

    AbsoluteFileSystemAccess(FileSystem fileSystem, IPathAllocator<AbsoluteFilePath, AbsoluteDirectoryPath> absolutePathAllocator,
            IPathAllocator<RelativeFilePath, RelativeDirectoryPath> relativePathAllocator, IPathObjectsAllocator pathObjectsAllocator) {
        super(fileSystem, absolutePathAllocator, relativePathAllocator, pathObjectsAllocator);
    }
}
