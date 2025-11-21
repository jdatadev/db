package dev.jdata.db.utils.adt.sets;

public interface IHeapMutableLongSetAllocator extends IMutableLongSetAllocator<IHeapMutableLongSet> {

    public static IHeapMutableIntSetAllocator create() {

        return HeapMutableIntSetAllocator.INSTANCE;
    }
}
