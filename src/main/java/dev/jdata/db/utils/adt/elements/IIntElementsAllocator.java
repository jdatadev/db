package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

interface IIntElementsAllocator<T extends IElements & IIntElementsView, U extends IMutableElements & IIntElementsView, V extends IElementsBuilder<T, ?> & IIntElementsMarker>

        extends IElementsArrayElementsAllocator<T, U, V, int[]> {

    default T allocateImmutableFrom(int[] values) {

        Objects.requireNonNull(values);

        return allocateImmutableFrom(values, values.length);
    }
}
