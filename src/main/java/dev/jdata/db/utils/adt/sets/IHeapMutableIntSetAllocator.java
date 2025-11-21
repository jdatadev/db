package dev.jdata.db.utils.adt.sets;

public interface IHeapMutableIntSetAllocator extends IMutableIntSetAllocator<IHeapMutableIntSet> {

    public static IHeapMutableIntSetAllocator create() {

        return HeapMutableIntSetAllocator.INSTANCE;
    }
}
