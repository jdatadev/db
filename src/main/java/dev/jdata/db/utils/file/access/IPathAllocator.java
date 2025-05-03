package dev.jdata.db.utils.file.access;

public interface IPathAllocator<F extends IFilePath, D extends IDirectoryPath<F, D>> {

    D allocateDirectoryPath();
    void freeDirectoryPath(D filePath);

    F allocateFilePath();
    void freeFilePath(F filePath);
}
