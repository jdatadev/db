package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.ADTConstants;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.contains.builders.ContainsBuilder;

abstract class ElementsBuilder<T extends IElements, U extends IElements & IHeapContainsMarker, V extends IMutableElements>

        extends ContainsBuilder<T, U, V>
        implements IElementsBuilder<T, U> {

    protected static final int DEFAULT_INITIAL_CAPACITY = ADTConstants.DEFAULT_INITIAL_CAPACITY;

    <P> ElementsBuilder(AllocationType allocationType, long minimumCapacity, P parameter, IBuilderMutableAllocator<V, P> allocator) {
        super(allocationType, minimumCapacity, parameter, allocator);
    }
}
