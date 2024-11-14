package dev.jdata.db.utils.file.access;

import java.nio.file.Path;
import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public final class AbsoluteDirectoryPath extends AbsolutePath implements DirectoryPath<AbsoluteFilePath, AbsoluteDirectoryPath> {

    public static AbsoluteDirectoryPath of(Path path) {

        Objects.requireNonNull(path);

        if (!path.isAbsolute()) {

            throw new IllegalArgumentException();
        }

        return new AbsoluteDirectoryPath(path);
    }

    private AbsoluteDirectoryPath(Path path) {
        super(path);
    }

    private AbsoluteDirectoryPath(AbsoluteDirectoryPath path, RelativeDirectoryPath additionalPath) {
        super(path, additionalPath);
    }

    private AbsoluteDirectoryPath(AbsolutePath path, String additionalPathName) {
        super(path, additionalPathName);
    }

    private AbsoluteDirectoryPath(String[] pathNames, String additionalPathName) {
        super(pathNames, additionalPathName);
    }

    @Override
    public AbsoluteDirectoryPath resolveDirectory(String directoryName) {

        Checks.isDirectoryName(directoryName);

        return resolvePathName(directoryName, AbsoluteDirectoryPath::new);
    }

    @Override
    public AbsoluteFilePath resolveFile(String fileName) {

        Checks.isFileName(fileName);

        return resolvePathName(fileName, AbsoluteFilePath::new);
    }

    public AbsoluteDirectoryPath append(RelativeDirectoryPath relativeDirectoryPath) {

        return new AbsoluteDirectoryPath(this, relativeDirectoryPath);
    }

    public AbsoluteFilePath append(RelativeFilePath relativeFilePath) {

        return new AbsoluteFilePath(this, relativeFilePath);
    }

    public RelativeDirectoryPath relativize(AbsoluteDirectoryPath directoryPath) {

        Objects.requireNonNull(directoryPath);

        return RelativeDirectoryPath.of(getPath().relativize(directoryPath.getPath()));
    }

    public RelativeFilePath relativize(AbsoluteFilePath filePath) {

        Objects.requireNonNull(filePath);

        return RelativeFilePath.of(getPath().relativize(filePath.getPath()));
    }
}
