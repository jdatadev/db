package dev.jdata.db.utils.file.access;

import java.nio.file.Path;

public final class AbsoluteFilePath extends AbsolutePath implements FilePath {

    public static AbsoluteFilePath of(Path path) {

        return new AbsoluteFilePath(path);
    }

    private AbsoluteFilePath(Path path) {
        super(path);
    }

    AbsoluteFilePath(AbsoluteDirectoryPath path, RelativeFilePath additionalPath) {
        super(path, additionalPath);
    }

    AbsoluteFilePath(String[] pathNames, String additionalPathName) {
        super(pathNames, additionalPathName);
    }

    @Override
    public String getFileName() {

        return getLastName();
    }
}
