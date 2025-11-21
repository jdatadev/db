package dev.jdata.db.utils.adt.contains.builders;

import java.util.Objects;

import dev.jdata.db.utils.adt.contains.IContains;
import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.adt.contains.IMutableContains;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.builders.IBuilder;
import dev.jdata.db.utils.checks.Checks;

public abstract class ContainsBuilder<

                IMMUTABLE extends IContains,
                MUTABLE extends IMutableContains,
                HEAP_IMMUTABLE extends IContains & IHeapContainsMarker,
                BUILDER extends IContainsBuilder<IMMUTABLE, HEAP_IMMUTABLE>,
                BUILDER_ALLOCATOR extends ContainsBuilderAllocator<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, BUILDER>>

        extends ObjectCacheNode
        implements IContainsView, IContainsBuildable<IMMUTABLE, HEAP_IMMUTABLE>, IBuilder {

    private final MUTABLE mutable;

    protected abstract IMMUTABLE empty();
    protected abstract IMMUTABLE build();

    protected abstract HEAP_IMMUTABLE heapEmpty();
    protected abstract HEAP_IMMUTABLE heapBuild();

    protected abstract HEAP_IMMUTABLE fromMutableHeap(AllocationType allocationType);

    protected ContainsBuilder(AllocationType allocationType, int minimumCapacity, BUILDER_ALLOCATOR builderAllocator) {
        super(allocationType);

        Checks.isInitialCapacity(minimumCapacity);
        Objects.requireNonNull(builderAllocator);

        this.mutable = builderAllocator.allocateMutable(minimumCapacity);
    }

    @Override
    public final boolean isEmpty() {

        return mutable.isEmpty();
    }

    @Override
    public final IMMUTABLE buildOrEmpty() {

        return isEmpty() ? empty() : build();
    }

    @Override
    public final IMMUTABLE buildOrNull() {

        return isEmpty() ? null : build();
    }

    @Override
    public final HEAP_IMMUTABLE buildHeapAllocatedOrEmpty() {

        return isEmpty() ? heapEmpty() : heapBuild();
    }

    @Override
    public final HEAP_IMMUTABLE buildHeapAllocatedOrNull() {

        return isEmpty() ? null : heapBuild();
    }

    protected final MUTABLE getMutable() {
        return mutable;
    }

    @Override
    public final String toString() {

        return getClass().getSimpleName() + " [ mutable=" + mutable + "]";
    }
}
