package dev.jdata.db.utils.file.access;

public interface DirectoryPath<F extends FilePath, D extends DirectoryPath<F, D>> extends IPath {

    D resolveDirectory(String directoryName);

    F resolveFile(String fileName);
}
