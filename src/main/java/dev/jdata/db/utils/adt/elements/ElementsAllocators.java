package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.Supplier;

abstract class ElementsAllocators<IMMUTABLE extends IElements, ALLOCATE_FROM_ARRAY> {

    private final Supplier<IMMUTABLE> emptyImmutableSupplier;

    ElementsAllocators(Supplier<IMMUTABLE> emptyImmutableSupplier) {

        this.emptyImmutableSupplier = Objects.requireNonNull(emptyImmutableSupplier);
    }

    public final IMMUTABLE emptyImmutable() {

        return emptyImmutableSupplier.get();
    }
}
