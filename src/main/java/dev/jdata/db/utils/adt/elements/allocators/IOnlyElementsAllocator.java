package dev.jdata.db.utils.adt.elements.allocators;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.elements.IElementsAllocator;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.elements.builders.IElementsBuilder;

public interface IOnlyElementsAllocator<

                IMMUTABLE extends IElements & IOnlyElementsView,
                HEAP_IMMUTABLE extends IElements & IOnlyElementsView & IHeapContainsMarker,
                BUILDER extends IElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>,
                ELEMENTS_ARRAY>

        extends IElementsAllocator<IMMUTABLE, HEAP_IMMUTABLE, BUILDER, ELEMENTS_ARRAY> {

}
