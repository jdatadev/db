package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IntCapacityMutableElementsAllocator;

abstract class MutableLongIndexListAllocator<T extends IMutableLongIndexList, U extends MutableLongIndexList>

        extends IntCapacityMutableElementsAllocator<T, U, long[]>
        implements IMutableLongIndexListAllocator<T> {

    MutableLongIndexListAllocator() {
        super(long[]::new);
    }
}
