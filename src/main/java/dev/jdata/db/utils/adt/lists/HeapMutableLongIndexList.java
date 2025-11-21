package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;

final class HeapMutableLongIndexList extends MutableLongIndexList implements IHeapMutableLongIndexList {

    static HeapMutableLongIndexList create(AllocationType allocationType, int initialCapacity) {

        checkMutableCreateParameters(allocationType, AllocationMechanism.HEAP, initialCapacity);

        return new HeapMutableLongIndexList(allocationType, initialCapacity);
    }

    static HeapMutableLongIndexList copyToMutable(AllocationType allocationType, ILongIterableElementsView mutableFrom) {

        checkCopyToMutableParameters(allocationType, AllocationMechanism.HEAP, mutableFrom);

        return new HeapMutableLongIndexList(allocationType, mutableFrom);
    }

    private HeapMutableLongIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }

    private HeapMutableLongIndexList(AllocationType allocationType, ILongIterableElementsView mutableFrom) {
        super(allocationType, mutableFrom);
    }
}
