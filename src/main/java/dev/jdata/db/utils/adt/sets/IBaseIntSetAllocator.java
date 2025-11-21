package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;

interface IBaseIntSetAllocator<

                IMMUTABLE extends IBaseIntSet,
                HEAP_IMMUTABLE extends IBaseIntSet & IHeapContainsMarker,
                BUILDER extends IBaseIntSetBuilder<IMMUTABLE, HEAP_IMMUTABLE>>

        extends IBaseSetAllocator<IMMUTABLE, HEAP_IMMUTABLE, BUILDER, int[]> {

}
