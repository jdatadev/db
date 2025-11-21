package dev.jdata.db.utils.adt.elements.builders.allocators;

import dev.jdata.db.utils.adt.ADTConstants;
import dev.jdata.db.utils.adt.contains.builders.ContainsBuilderAllocator;
import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.elements.IElementsAllocator;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.builders.IElementsBuilder;

public abstract class ElementsBuilderAllocator<

                IMMUTABLE extends IElements,
                MUTABLE extends IMutableElements,
                HEAP_IMMUTABLE extends IElements & IHeapContainsMarker,
                ELEMENTS_ARRAY,
                BUILDER extends IElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>,
                BUILDER_ALLOCATOR extends ElementsBuilderAllocator<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, BUILDER, BUILDER_ALLOCATOR>>

        extends ContainsBuilderAllocator<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, BUILDER>
        implements IElementsAllocator<IMMUTABLE, HEAP_IMMUTABLE, BUILDER, ELEMENTS_ARRAY> {

    protected static final int DEFAULT_INITIAL_CAPACITY = ADTConstants.DEFAULT_INITIAL_CAPACITY;

    protected static final int DEFAULT_CAPACITY_MULTIPLICATOR = ADTConstants.DEFAULT_CAPACITY_MULTIPLICATOR;

    protected abstract IMMUTABLE copyToImmutable(MUTABLE mutable);

    protected abstract IMMUTABLE emptyImmutable();
}
