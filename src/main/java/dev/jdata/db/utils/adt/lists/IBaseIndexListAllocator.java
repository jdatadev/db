package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.review.IDeprecatedInstanceAllocator;
import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.allocators.IOnlyElementsAllocator;

interface IBaseIndexListAllocator<

        T,
        IMMUTABLE extends IBaseIndexList<T>,
        HEAP_IMMUTABLE extends IBaseIndexList<T> & IHeapContainsMarker,
        BUILDER extends IBaseObjectIndexListBuilder<T, IMMUTABLE, HEAP_IMMUTABLE>>

        extends IOnlyElementsAllocator<IMMUTABLE, HEAP_IMMUTABLE, BUILDER, T[]>, IDeprecatedInstanceAllocator<IMMUTABLE> {

}
