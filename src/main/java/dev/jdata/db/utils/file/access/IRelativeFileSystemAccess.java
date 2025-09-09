package dev.jdata.db.utils.file.access;

import java.io.IOException;
import java.util.Objects;

public interface IRelativeFileSystemAccess extends IFileSystemAccess<RelativeFilePath, RelativeDirectoryPath> {

    public static IRelativeFileSystemAccess create(AbsoluteDirectoryPath rootPath, IAbsoluteFileSystemAccess absoluteFileSystemAccess) {

        Objects.requireNonNull(rootPath);

        return absoluteFileSystemAccess instanceof BaseNIOFileSystemAccess
                ? new RelativeFileSystemAccess(rootPath, (BaseNIOFileSystemAccess)absoluteFileSystemAccess)
                : new RelativeFileSystemAccess(rootPath, absoluteFileSystemAccess);
    }

    void deleteAll() throws IOException;
}
