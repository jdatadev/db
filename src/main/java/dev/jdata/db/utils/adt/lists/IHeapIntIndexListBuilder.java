package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapIntIndexListBuilder extends IIntIndexListBuilder<IHeapIntIndexList, IHeapIntIndexList> {

    public static IHeapIntIndexListBuilder create() {

        return new HeapIntIndexListBuilder(AllocationType.HEAP);
    }

    public static IHeapIntIndexListBuilder createBuilder(int initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return new HeapIntIndexListBuilder(AllocationType.HEAP, initialCapacity);
    }
}
