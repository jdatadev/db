package dev.jdata.db.utils.allocators;

import java.util.Objects;

import dev.jdata.db.utils.adt.mutability.IMutable;

abstract class HeapMutableInstanceAllocator<T extends IMutable> extends HeapInstanceAllocator implements IMutableAllocator<T> {

    @Override
    public final void freeMutable(T mutable) {

        Objects.requireNonNull(mutable);
    }
}
