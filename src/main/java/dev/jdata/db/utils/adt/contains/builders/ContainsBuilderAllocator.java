package dev.jdata.db.utils.adt.contains.builders;

import java.util.Objects;

import dev.jdata.db.utils.adt.contains.IContains;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.contains.IMutableContains;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.BuilderAllocator;
import dev.jdata.db.utils.checks.Checks;

public abstract class ContainsBuilderAllocator<

                IMMUTABLE extends IContains,
                HEAP_IMMUTABLE extends IContains & IHeapContainsMarker,
                MUTABLE extends IMutableContains,
                BUILDER extends IContainsBuilder<IMMUTABLE, HEAP_IMMUTABLE>>

        extends BuilderAllocator<IMMUTABLE, BUILDER> {

    protected abstract MUTABLE allocateMutable(long minimumCapacity);
    protected abstract void freeMutable(MUTABLE mutable);

    public final MUTABLE allocate(AllocationType allocationType, long minimumCapacity) {

        Objects.requireNonNull(allocationType);
        Checks.isIntOrLongCapacity(minimumCapacity);

        return allocateMutable(minimumCapacity);
    }
}
