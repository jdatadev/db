package dev.jdata.db.utils.file.access;

import java.nio.file.FileSystem;

public interface AbsoluteFileSystemAccess extends FileSystemAccess<AbsoluteFilePath, AbsoluteDirectoryPath> {

    public static AbsoluteFileSystemAccess of(FileSystem fileSystem) {

        return new AbsoluteFileSystemAccessImpl(fileSystem);
    }
}
