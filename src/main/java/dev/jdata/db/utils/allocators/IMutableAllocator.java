package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.mutability.IMutable;

public interface IMutableAllocator<T extends IMutable> {

    T createMutable(long minimumCapacity);

    void freeMutable(T mutable);
}
