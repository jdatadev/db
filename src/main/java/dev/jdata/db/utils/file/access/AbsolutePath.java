package dev.jdata.db.utils.file.access;

import java.io.File;
import java.nio.file.Path;

abstract class AbsolutePath extends BasePath {

    AbsolutePath(Path path) {
        super(path);
    }

    AbsolutePath(AbsolutePath path, RelativePath additionalPath) {
        super(path, additionalPath);
    }

    AbsolutePath(AbsolutePath path, String additionalPathName) {
        super(path, additionalPathName);
    }

    AbsolutePath(String[] pathNames, String additionalPathName) {
        super(pathNames, additionalPathName);
    }

    final Path getPath() {

        return Path.of(asString(true));
    }

    final File toFile() {

        return new File(asString(true));
    }
}
