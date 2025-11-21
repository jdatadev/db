package dev.jdata.db.utils.adt.elements.builders;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.elements.builders.allocators.ElementsBuilderAllocator;

public abstract class OnlyElementsBuilder<

                IMMUTABLE extends IElements & IOnlyElementsView,
                MUTABLE extends IMutableElements,
                HEAP_IMMUTABLE extends IElements & IOnlyElementsView & IHeapContainsMarker,
                ELEMENTS_ARRAY,
                BUILDER extends IOnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>,
                BUILDER_ALLOCATOR extends ElementsBuilderAllocator<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, BUILDER, BUILDER_ALLOCATOR>>

        extends ElementsBuilder<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, BUILDER, BUILDER_ALLOCATOR> {

    protected OnlyElementsBuilder(AllocationType allocationType, int minimumCapacity, BUILDER_ALLOCATOR builderAllocator) {
        super(allocationType, minimumCapacity, builderAllocator);
    }

    protected final long getCapacity() {

        return getMutable().getCapacity();
    }
}
