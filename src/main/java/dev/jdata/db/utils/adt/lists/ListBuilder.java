package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IOnlyElementsMutable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.elements.builders.IOnlyElementsBuilder;
import dev.jdata.db.utils.adt.elements.builders.OnlyElementsBuilder;
import dev.jdata.db.utils.adt.elements.builders.allocators.OnlyElementsBuilderAllocator;

abstract class ListBuilder<

                IMMUTABLE extends IElements & IOnlyElementsView & IListMarker,
                MUTABLE extends IMutableElements & IOnlyElementsMutable & IMutableListMarker,
                HEAP_IMMUTABLE extends IElements & IOnlyElementsView & IListMarker & IHeapContainsMarker,
                ELEMENTS_ARRAY,
                BUILDER extends IOnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>,
                BUILDER_ALLOCATOR extends OnlyElementsBuilderAllocator<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, BUILDER, BUILDER_ALLOCATOR>>

        extends OnlyElementsBuilder<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, BUILDER, BUILDER_ALLOCATOR> {

    ListBuilder(AllocationType allocationType, int minimumCapacity, BUILDER_ALLOCATOR builderAllocator) {
        super(allocationType, minimumCapacity, builderAllocator);
    }
}
