package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.elements.builders.IOnlyElementsBuilder;
import dev.jdata.db.utils.adt.elements.builders.allocators.OnlyElementsBuilderAllocator;

abstract class SetBuilderAllocator<

                IMMUTABLE extends IBaseSet & IOnlyElementsView,
                MUTABLE extends IBaseMutableSet,
                HEAP_IMMUTABLE extends IBaseSet & IHeapContainsMarker,
                ELEMENTS_ARRAY,
                BUILDER extends IOnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>,
                BUILDER_ALLOCATOR extends OnlyElementsBuilderAllocator<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, BUILDER, BUILDER_ALLOCATOR>>

        extends OnlyElementsBuilderAllocator<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, BUILDER, BUILDER_ALLOCATOR> {

}
