package dev.jdata.db.utils.adt.sets;

public interface IHeapMutableLongLargeSetAllocator extends IMutableLongLargeSetAllocator<IHeapMutableLongLargeSet> {

    static IHeapMutableLongLargeSetAllocator create() {

        return HeapMutableLongLargeSetAllocator.INSTANCE;
    }
}
