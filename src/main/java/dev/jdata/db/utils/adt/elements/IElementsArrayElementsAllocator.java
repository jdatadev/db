package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

interface IElementsArrayElementsAllocator<

                IMMUTABLE extends IElements,
                MUTABLE extends IMutableElements,
                BUILDER extends IElementsBuilder<IMMUTABLE, ?>,
                ELEMENTS_ARRAY>

        extends IElementsAllocator<IMMUTABLE, MUTABLE, BUILDER> {

    IMMUTABLE allocateImmutableFrom(ELEMENTS_ARRAY values, long startIndex, long numElements);

    default IMMUTABLE allocateImmutableFrom(ELEMENTS_ARRAY values, long numElements) {

        Objects.requireNonNull(values);
        Checks.isLongNumElements(numElements);

        return allocateImmutableFrom(values, 0L, numElements);
    }
}
