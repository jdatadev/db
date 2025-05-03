package dev.jdata.db.utils.file.access;

final class HeapRelativePathAllocator implements IPathAllocator<RelativeFilePath, RelativeDirectoryPath> {

    static final HeapRelativePathAllocator INSTANCE = new HeapRelativePathAllocator();

    private HeapRelativePathAllocator() {

    }

    @Override
    public RelativeDirectoryPath allocateDirectoryPath() {

        return new RelativeDirectoryPath();
    }

    @Override
    public void freeDirectoryPath(RelativeDirectoryPath filePath) {

    }

    @Override
    public RelativeFilePath allocateFilePath() {

        return new RelativeFilePath();
    }

    @Override
    public void freeFilePath(RelativeFilePath filePath) {

    }
};
