package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;

public abstract class IndexListAllocator<

            T,
            IMMUTABLE extends IIndexList<T>,
            MUTABLE extends MutableObjectIndexList<T, IMMUTABLE, BUILDER, BUILDER_ALLOCATOR>,
            HEAP_IMMUTABLE extends IIndexList<T> & IHeapContainsMarker,
            BUILDER extends IIndexListBuilder<T, IMMUTABLE, HEAP_IMMUTABLE>,
            BUILDER_ALLOCATOR extends IndexListAllocator<T, IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, BUILDER, BUILDER_ALLOCATOR>>

    extends ListBuilderAllocator<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, T[], BUILDER, BUILDER_ALLOCATOR>
    implements IIndexListAllocator<T, IMMUTABLE, BUILDER> {

}
