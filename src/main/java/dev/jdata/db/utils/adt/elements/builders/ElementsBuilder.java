package dev.jdata.db.utils.adt.elements.builders;

import dev.jdata.db.utils.adt.contains.builders.ContainsBuilder;
import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.builders.allocators.ElementsBuilderAllocator;

public abstract class ElementsBuilder<

                IMMUTABLE extends IElements,
                MUTABLE extends IMutableElements,
                HEAP_IMMUTABLE extends IElements & IHeapContainsMarker,
                ELEMENTS_ARRAY,
                BUILDER extends IElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>,
                BUILDER_ALLOCATOR extends ElementsBuilderAllocator<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, BUILDER, BUILDER_ALLOCATOR>>

        extends ContainsBuilder<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, BUILDER, BUILDER_ALLOCATOR>
        implements IElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE> {

    ElementsBuilder(AllocationType allocationType, int minimumCapacity, BUILDER_ALLOCATOR builderAllocator) {
        super(allocationType, minimumCapacity, builderAllocator);
    }
}
