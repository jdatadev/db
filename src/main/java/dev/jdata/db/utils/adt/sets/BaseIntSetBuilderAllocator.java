package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.elements.builders.IOnlyElementsBuilder;

abstract class BaseIntSetBuilderAllocator<

                IMMUTABLE extends IBaseSet & IOnlyElementsView,
                MUTABLE extends IBaseMutableSet,
                HEAP_IMMUTABLE extends IBaseSet & IHeapContainsMarker,
                BUILDER extends IOnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>,
                BUILDER_ALLOCATOR extends SetBuilderAllocator<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, int[], BUILDER, BUILDER_ALLOCATOR>>

        extends SetBuilderAllocator<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, int[], BUILDER, BUILDER_ALLOCATOR> {

}
