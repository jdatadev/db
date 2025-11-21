package dev.jdata.db.utils.allocators;

import java.util.Objects;

import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.mutability.IMutable;

abstract class HeapMutableInstanceAllocator<T extends IMutable, U extends IMutableFrom> extends HeapInstanceAllocator implements IMutableAllocator<T, U> {

    protected static void checkCopyToMutableParameters(IMutableFrom mutableFrom) {

        Objects.requireNonNull(mutableFrom);
    }

    @Override
    public final void freeMutable(T mutable) {

        Objects.requireNonNull(mutable);
    }
}
