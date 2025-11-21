package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.ADTConstants;
import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.builders.IElementsBuilder;

public interface IElementsAllocator<

                IMMUTABLE extends IElements,
                HEAP_IMMUTABLE extends IElements & IHeapContainsMarker,
                BUILDER extends IElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>,
                ELEMENTS_ARRAY> extends IBaseElementsAllocator {

    BUILDER createBuilder(long minimumCapacity);

    void freeBuilder(BUILDER builder);

    void freeImmutable(IMMUTABLE immutable);

    default BUILDER createBuilder() {

        return createBuilder(ADTConstants.DEFAULT_INITIAL_CAPACITY);
    }

    IMMUTABLE allocateImmutableFrom(ELEMENTS_ARRAY values, int numElements);
}
