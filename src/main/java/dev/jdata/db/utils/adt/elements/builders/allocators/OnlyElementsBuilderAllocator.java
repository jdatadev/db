package dev.jdata.db.utils.adt.elements.builders.allocators;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IOnlyElementsMutable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.elements.builders.IOnlyElementsBuilder;

public abstract class OnlyElementsBuilderAllocator<

                IMMUTABLE extends IElements & IOnlyElementsView,
                MUTABLE extends IMutableElements & IOnlyElementsMutable,
                HEAP_IMMUTABLE extends IElements & IOnlyElementsView & IHeapContainsMarker,
                ELEMENTS_ARRAY,
                BUILDER extends IOnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>,
                BUILDER_ALLOCATOR extends OnlyElementsBuilderAllocator<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, BUILDER, BUILDER_ALLOCATOR>>

        extends ElementsBuilderAllocator<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, BUILDER, BUILDER_ALLOCATOR> {

}
