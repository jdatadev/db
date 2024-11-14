package dev.jdata.db.utils.file.access;

import java.nio.file.Path;

public final class RelativeFilePath extends RelativePath implements FilePath {

    public static RelativeFilePath ROOT = new RelativeFilePath();

    static RelativeFilePath of(Path path) {

        return new RelativeFilePath(path);
    }

    public static RelativeFilePath of(String pathName) {

        return new RelativeFilePath(pathName);
    }

    public static RelativeFilePath of(String ... pathNames) {

        return new RelativeFilePath(pathNames);
    }

    private RelativeFilePath() {

    }

    private RelativeFilePath(Path path) {
        super(path);
    }

    private RelativeFilePath(String pathName) {
        super(pathName);
    }

    private RelativeFilePath(String[] pathNames) {
        super(pathNames);
    }

    RelativeFilePath(String[] pathNames, String additionalPathName) {
        super(pathNames, additionalPathName);
    }

    @Override
    public final String getFileName() {

        return getLastName();
    }
}
