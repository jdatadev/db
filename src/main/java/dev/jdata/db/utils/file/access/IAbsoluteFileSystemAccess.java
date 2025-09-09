package dev.jdata.db.utils.file.access;

import java.nio.file.FileSystem;

public interface IAbsoluteFileSystemAccess extends IFileSystemAccess<AbsoluteFilePath, AbsoluteDirectoryPath> {

    public static IAbsoluteFileSystemAccess ofHeapAllocated(FileSystem fileSystem) {

        return of(fileSystem, HeapAbsolutePathAllocator.INSTANCE, HeapRelativePathAllocator.INSTANCE, HeapPathObjectsAllocator.INSTANCE);
    }

    public static IAbsoluteFileSystemAccess of(FileSystem fileSystem, IPathAllocator<AbsoluteFilePath, AbsoluteDirectoryPath> absolutePathAllocator,
            IPathAllocator<RelativeFilePath, RelativeDirectoryPath> relativePathAllocator, IPathObjectsAllocator pathObjectsAllocator) {

        return new AbsoluteFileSystemAccess(fileSystem, absolutePathAllocator, relativePathAllocator, pathObjectsAllocator);
    }

    AbsoluteDirectoryPath append(AbsoluteDirectoryPath absoluteDirectoryPath, RelativeDirectoryPath relativeDirectoryPath);
    AbsoluteFilePath append(AbsoluteDirectoryPath absoluteDirectoryPath, RelativeFilePath relativeFilePath);

    RelativeDirectoryPath relativize(AbsoluteDirectoryPath absoluteDirectoryPath, AbsoluteDirectoryPath absoluteSubDirectoryPath);
    RelativeFilePath relativize(AbsoluteDirectoryPath absoluteDirectoryPath, AbsoluteFilePath absoluteSubFilePath);
}
