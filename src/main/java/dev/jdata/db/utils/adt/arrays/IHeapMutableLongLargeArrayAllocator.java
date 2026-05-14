package dev.jdata.db.utils.adt.arrays;

public interface IHeapMutableLongLargeArrayAllocator extends IMutableLongLargeArrayAllocator<IHeapMutableLongLargeArray> {

    public static IHeapMutableLongLargeArrayAllocator create() {

        return HeapMutableLongLargeArrayAllocator.INSTANCE;
    }
}
