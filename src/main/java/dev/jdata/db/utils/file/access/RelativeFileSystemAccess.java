package dev.jdata.db.utils.file.access;

import java.util.Objects;

public interface RelativeFileSystemAccess extends FileSystemAccess<RelativeFilePath, RelativeDirectoryPath> {

    public static RelativeFileSystemAccess create(AbsoluteDirectoryPath rootPath, AbsoluteFileSystemAccess absoluteFileSystemAccess) {

        Objects.requireNonNull(rootPath);

        return new RelativeFileSystemAccessImpl(rootPath, absoluteFileSystemAccess);
    }
}
