package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

interface ILongElementsAllocator<T extends IElements & ILongElementsView, U extends IMutableElements & ILongElementsView, V extends IElementsBuilder<T, ?> & ILongElementsMarker>

        extends IElementsArrayElementsAllocator<T, U, V, long[]> {

    default T allocateImmutableFrom(long[] values) {

        Objects.requireNonNull(values);

        return allocateImmutableFrom(values, values.length);
    }
}
