package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.ADTConstants;
import dev.jdata.db.utils.allocators.IBuilderAllocator;
import dev.jdata.db.utils.allocators.ITypedInstanceAllocator;

public interface IElementsAllocator<T extends IElements, U extends IMutableElements, V extends IElementsBuilder<T, ?>>

        extends IBaseElementsAllocator, ITypedInstanceAllocator<T>, IBuilderAllocator<V> {

    V createBuilder(long minimumCapacity);

    void freeImmutable(T immutable);

    T copyToImmutable(U mutable);

    default V createBuilder() {

        return createBuilder(ADTConstants.DEFAULT_INITIAL_CAPACITY);
    }
}
