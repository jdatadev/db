package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.allocators.IOnlyElementsAllocator;
import dev.jdata.db.utils.adt.elements.builders.IElementsBuilder;

interface IBaseSetAllocator<

                IMMUTABLE extends IBaseSet,
                HEAP_IMMUTABLE extends IBaseSet & IHeapContainsMarker,
                BUILDER extends IElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>,
                ELEMENTS_ARRAY>

        extends IOnlyElementsAllocator<IMMUTABLE, HEAP_IMMUTABLE, BUILDER, ELEMENTS_ARRAY> {

}
