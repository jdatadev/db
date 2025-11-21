package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.Supplier;

import dev.jdata.db.utils.allocators.BaseAllocatorChecks;

abstract class ElementsAllocators<IMMUTABLE extends IElements, ALLOCATE_FROM_ARRAY> extends BaseAllocatorChecks {

    private final Supplier<IMMUTABLE> emptyImmutableSupplier;

    ElementsAllocators(Supplier<IMMUTABLE> emptyImmutableSupplier) {

        this.emptyImmutableSupplier = Objects.requireNonNull(emptyImmutableSupplier);
    }

    public final IMMUTABLE emptyImmutable() {

        return emptyImmutableSupplier.get();
    }
}
