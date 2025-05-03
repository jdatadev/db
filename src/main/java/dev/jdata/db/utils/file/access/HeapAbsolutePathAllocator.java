package dev.jdata.db.utils.file.access;

final class HeapAbsolutePathAllocator implements IPathAllocator<AbsoluteFilePath, AbsoluteDirectoryPath> {

    static final HeapAbsolutePathAllocator INSTANCE = new HeapAbsolutePathAllocator();

    private HeapAbsolutePathAllocator() {

    }

    @Override
    public AbsoluteDirectoryPath allocateDirectoryPath() {

        return new AbsoluteDirectoryPath();
    }

    @Override
    public void freeDirectoryPath(AbsoluteDirectoryPath filePath) {

    }

    @Override
    public AbsoluteFilePath allocateFilePath() {

        return new AbsoluteFilePath();
    }

    @Override
    public void freeFilePath(AbsoluteFilePath filePath) {

    }
}
