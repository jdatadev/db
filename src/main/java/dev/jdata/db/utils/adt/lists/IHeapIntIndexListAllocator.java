package dev.jdata.db.utils.adt.lists;

public interface IHeapIntIndexListAllocator extends IIntIndexListAllocator<IHeapIntIndexList, IHeapMutableIntIndexList, IHeapIntIndexListBuilder> {

    static IHeapIntIndexListAllocator create() {

        return HeapIntIndexListAllocator.INSTANCE;
    }
}
