package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public final class NodeObjectCache<T extends ObjectCacheNode> extends BaseNodeAllocator<T, ObjectCacheNode> implements IObjectCache<T> {

    public static abstract class ObjectCacheNode extends AllocatorNode<ObjectCacheNode> {

        protected ObjectCacheNode(AllocationType allocationType) {
            super(allocationType);
        }
    }

    private final Function<?, T> allocator;
    private final Object allocatorParameter;

    private T freeList;

    public NodeObjectCache(Supplier<T> allocator) {

        Objects.requireNonNull(allocator);

        this.allocator = p -> allocator.get();
        this.allocatorParameter = null;
    }

    public NodeObjectCache(Function<AllocationType, T> allocator) {

        Objects.requireNonNull(allocator);

        this.allocator = p -> allocator.apply(AllocationType.CACHING_ALLOCATOR);
        this.allocatorParameter = null;
    }

    public <P> NodeObjectCache(P parameter, Function<P, T> allocator) {

        this.allocatorParameter = parameter;
        this.allocator = Objects.requireNonNull(allocator);
    }

    @Override
    public void addCache(IAllocatorsStatisticsGatherer statisticsGatherer, String name, Class<T> objectType) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addNodeObjectCache(name, objectType, this);
    }

    public T allocate() {

        final T result;

        final T free = freeList;

        final boolean fromFreeList;

        if (free != null) {

            result = free;

            @SuppressWarnings("unchecked")
            final T next = (T)free.next;

            this.freeList = next;

            fromFreeList = true;
        }
        else {
            @SuppressWarnings("unchecked")
            final Function<Object, T> allocatorFuction = (Function<Object, T>) allocator;

            result = allocatorFuction.apply(allocatorParameter);

            fromFreeList = false;
        }

        result.init(null, true, true);

        addAllocatedInstance(fromFreeList);

        return result;
    }

    public void free(T instance) {

        Objects.requireNonNull(instance);

        if (!instance.isAllocated()) {

            throw new IllegalArgumentException();
        }

        addFreedInstance(true);

        instance.init(freeList, true, false);

        this.freeList = instance;
    }
}
