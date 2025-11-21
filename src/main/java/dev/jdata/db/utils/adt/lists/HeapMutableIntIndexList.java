package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;

final class HeapMutableIntIndexList extends MutableIntIndexList implements IHeapMutableIntIndexList {

    static HeapMutableIntIndexList create(AllocationType allocationType, int initialCapacity) {

        checkMutableCreateParameters(allocationType, AllocationMechanism.HEAP, initialCapacity);

        return new HeapMutableIntIndexList(allocationType, initialCapacity);
    }

    static HeapMutableIntIndexList copyToMutable(AllocationType allocationType, IIntIterableElementsView mutableFrom) {

        checkCopyToMutableParameters(allocationType, AllocationMechanism.HEAP, mutableFrom);

        return new HeapMutableIntIndexList(allocationType, mutableFrom);
    }

    private HeapMutableIntIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }

    private HeapMutableIntIndexList(AllocationType allocationType, IIntIterableElementsView mutableFrom) {
        super(allocationType, mutableFrom);
    }
}
