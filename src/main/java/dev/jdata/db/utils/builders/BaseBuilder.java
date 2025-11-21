package dev.jdata.db.utils.builders;

import dev.jdata.db.utils.adt.marker.IHeapTypeMarker;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public abstract class BaseBuilder<T, U extends IHeapTypeMarker> extends ObjectCacheNode implements IBuildable<T>, IBuilder {

    protected abstract boolean isEmpty();

    protected abstract T build();
    protected abstract T empty();

    protected abstract U heapBuild();
    protected abstract U heapEmpty();

    protected BaseBuilder(AllocationType allocationType) {
        super(allocationType);
    }

    @Override
    public final T buildOrEmpty() {

        checkIsAllocatedRenamed();

        return isEmpty() ? empty() : build();
    }

    @Override
    public final T buildOrNull() {

        checkIsAllocatedRenamed();

        return isEmpty() ? null : build();
    }

    @Override
    public final T buildNotEmpty() {

        checkIsAllocatedRenamed();

        if (isEmpty()) {

            throw makeIsEmptyException();
        }

        return build();
    }

    public final U buildHeapAllocatedOrEmpty() {

        checkIsAllocatedRenamed();

        return isEmpty() ? heapEmpty() : heapBuild();
    }

    public final U buildHeapAllocatedOrNull() {

        checkIsAllocatedRenamed();

        return isEmpty() ? null : heapBuild();
    }

    public final U buildHeapAllocatedNotEmpty() {

        checkIsAllocatedRenamed();

        if (isEmpty()) {

            throw makeIsEmptyException();
        }

        return heapBuild();
    }

    private static IllegalStateException makeIsEmptyException() {

        return new IllegalStateException();
    }
}
