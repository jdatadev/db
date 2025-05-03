package dev.jdata.db.utils.file.access;

import java.nio.file.Path;

public interface IPathFactory<F extends IFilePath, D extends IDirectoryPath<F, D>> {

    D directoryPathOf(CharSequence pathName);

    @Deprecated
    D directoryPathOf(CharSequence ... pathNames);

    D directoryPathOf(Path path);

    F filePathOf(CharSequence pathName);

    @Deprecated
    F filePathOf(CharSequence ... pathNames);

    D resolveDirectory(D directoryPath, CharSequence directoryName);

    F resolveFile(D directoryPath, CharSequence fileName);
}
