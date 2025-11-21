package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.mutability.IMutable;

public interface IMutableAllocator<T extends IMutable, U extends IMutableFrom> {

    T createMutable(long minimumCapacity);

    void freeMutable(T mutable);

    T copyToMutable(U mutableFrom);
}
