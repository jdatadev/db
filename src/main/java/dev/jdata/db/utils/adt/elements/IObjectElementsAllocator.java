package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

interface IObjectElementsAllocator<

                T,
                IMMUTABLE extends IElements & IOnlyElementsView & IObjectElementsView<T>,
                MUTABLE extends IMutableElements & IOnlyElementsView & IObjectElementsView<T>,
                BUILDER extends IObjectOnlyElementsBuilder<T, IMMUTABLE, ?>>

        extends IElementsArrayElementsAllocator<IMMUTABLE, MUTABLE, BUILDER, T[]> {

    default IMMUTABLE allocateImmutableFrom(T[] instances) {

        Objects.requireNonNull(instances);

        return allocateImmutableFrom(instances, instances.length);
    }
}
