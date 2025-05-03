package dev.jdata.db.utils.file.access;

public interface IPathImplAllocator {

    PathImpl allocatePathImpl();

    void freePathImpl(PathImpl pathImpl);
}
