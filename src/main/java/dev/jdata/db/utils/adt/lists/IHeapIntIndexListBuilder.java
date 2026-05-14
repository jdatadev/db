package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsBuilderMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapIntIndexListBuilder extends IIntIndexListBuilder<IHeapIntIndexList, IHeapIntIndexList>, IHeapContainsBuilderMarker {

    public static IHeapIntIndexListBuilder create() {

        return HeapIntIndexListBuilder.create(AllocationType.HEAP);
    }

    public static IHeapIntIndexListBuilder create(int initialCapacity) {

        return HeapIntIndexListBuilder.create(AllocationType.HEAP, initialCapacity);
    }
}
