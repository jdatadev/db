package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;

interface IIntSetAllocator<

                IMMUTABLE extends IBaseIntSet,
                HEAP_IMMUTABLE extends IBaseIntSet & IHeapContainsMarker,
                BUILDER extends IBaseIntSetBuilder<IMMUTABLE, HEAP_IMMUTABLE>>

        extends IBaseIntSetAllocator<IMMUTABLE, HEAP_IMMUTABLE, BUILDER> {

}
