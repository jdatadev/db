package dev.jdata.db.utils.file.access;

import java.nio.file.Path;

abstract class RelativePath extends BasePath {

    RelativePath() {

    }

    RelativePath(Path path) {
        super(path);
    }

    RelativePath(String pathName) {
        super(pathName);
    }

    RelativePath(String[] pathNames) {
        super(pathNames);
    }

    RelativePath(String[] pathNames, String additionalPathName) {
        super(pathNames, additionalPathName);
    }
}
