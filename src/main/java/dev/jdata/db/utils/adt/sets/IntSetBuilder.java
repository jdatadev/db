package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.elements.builders.IOnlyElementsBuilder;
import dev.jdata.db.utils.adt.elements.builders.OnlyElementsBuilder;
import dev.jdata.db.utils.adt.elements.builders.allocators.OnlyElementsBuilderAllocator;

abstract class IntSetBuilder<

            IMMUTABLE extends IBaseSet,
            MUTABLE extends IBaseMutableSet,
            HEAP_IMMUTABLE extends IBaseSet & IOnlyElementsView & IHeapContainsMarker,
            BUILDER extends IOnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>,
            BUILDER_ALLOCATOR extends OnlyElementsBuilderAllocator<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, int[], BUILDER, BUILDER_ALLOCATOR>>

        extends OnlyElementsBuilder<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, int[], BUILDER, BUILDER_ALLOCATOR> {

    IntSetBuilder(AllocationType allocationType, int minimumCapacity, BUILDER_ALLOCATOR builderAllocator) {
        super(allocationType, minimumCapacity, builderAllocator);
    }
}