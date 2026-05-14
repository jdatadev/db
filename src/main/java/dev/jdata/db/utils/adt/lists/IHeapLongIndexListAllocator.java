package dev.jdata.db.utils.adt.lists;

public interface IHeapLongIndexListAllocator extends ILongIndexListAllocator<IHeapLongIndexList, IHeapMutableLongIndexList, IHeapLongIndexListBuilder> {

    static IHeapLongIndexListAllocator create() {

        return HeapLongIndexListAllocator.INSTANCE;
    }
}
