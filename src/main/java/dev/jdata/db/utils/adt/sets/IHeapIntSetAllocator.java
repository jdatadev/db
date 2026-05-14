package dev.jdata.db.utils.adt.sets;

public interface IHeapIntSetAllocator extends IIntSetAllocator<IHeapIntSet, IHeapMutableIntSet, IHeapIntSetBuilder> {

    public static IHeapIntSetAllocator create() {

        return HeapIntSetAllocator.INSTANCE;
    }
}
