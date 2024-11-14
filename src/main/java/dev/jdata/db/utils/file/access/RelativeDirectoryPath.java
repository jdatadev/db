package dev.jdata.db.utils.file.access;

import java.nio.file.Path;

import dev.jdata.db.utils.checks.Checks;

public final class RelativeDirectoryPath extends RelativePath implements DirectoryPath<RelativeFilePath, RelativeDirectoryPath> {

    public static RelativeDirectoryPath ROOT = new RelativeDirectoryPath();

    public static RelativeDirectoryPath of(String ... pathNames) {

        return new RelativeDirectoryPath(pathNames);
    }

    static RelativeDirectoryPath of(Path path) {

        return new RelativeDirectoryPath(path);
    }

    private RelativeDirectoryPath() {

    }

    private RelativeDirectoryPath(Path path) {
        super(path);
    }

    private RelativeDirectoryPath(String[] pathNames) {
        super(pathNames);
    }

    RelativeDirectoryPath(String[] pathNames, String additionalPathName) {
        super(pathNames, additionalPathName);
    }

    @Override
    public RelativeDirectoryPath resolveDirectory(String directoryName) {

        Checks.isDirectoryName(directoryName);

        return resolvePathName(directoryName, RelativeDirectoryPath::new);
    }

    @Override
    public RelativeFilePath resolveFile(String fileName) {

        Checks.isFileName(fileName);

        return resolvePathName(fileName, RelativeFilePath::new);
    }
}
