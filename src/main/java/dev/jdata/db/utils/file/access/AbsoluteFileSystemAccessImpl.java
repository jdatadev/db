package dev.jdata.db.utils.file.access;

import java.nio.file.FileSystem;

final class AbsoluteFileSystemAccessImpl extends BaseNIOFileSystemAccess {

    AbsoluteFileSystemAccessImpl(FileSystem fileSystem) {
        super(fileSystem);
    }
}
