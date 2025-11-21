package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapLongIndexListBuilder extends ILongIndexListBuilder<IHeapLongIndexList, IHeapLongIndexList> {

    public static IHeapLongIndexListBuilder create() {

        return HeapLongIndexListBuilder.create(AllocationType.HEAP);
    }

    public static IHeapLongIndexListBuilder createBuilder(int initialCapacity) {

        return HeapLongIndexListBuilder.create(AllocationType.HEAP, initialCapacity);
    }
}
