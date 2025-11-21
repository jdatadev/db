package dev.jdata.db.utils.adt.contains.builders;

import dev.jdata.db.utils.adt.contains.IContains;
import dev.jdata.db.utils.adt.contains.IMutableContains;
import dev.jdata.db.utils.allocators.BuilderAllocator;

public abstract class ContainsBuilderAllocator<

                IMMUTABLE extends IContains,
                MUTABLE extends IMutableContains,
                HEAP_IMMUTABLE extends IContains & IHeapContainsMarker,
                BUILDER extends IContainsBuilder<IMMUTABLE, HEAP_IMMUTABLE>>

        extends BuilderAllocator<IMMUTABLE, BUILDER> {

    protected abstract MUTABLE allocateMutable(int minimumCapacity);
    protected abstract void freeMutable(MUTABLE mutable);
}
