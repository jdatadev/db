package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.ObjLongFunction;

public abstract class BaseCapacityInstanceAllocator<T> extends InstanceNodeAllocator<T, BaseCapacityInstanceAllocator.CapacityInstanceAllocatorNode<T>> {

    public static enum CapacityMax {

        INT,
        LONG;

        public static long checkLongMinimumCapacity(CapacityMax capacityMax, long minimumCapacity) {

            switch (capacityMax) {

            case INT:

                Checks.isIntMinimumCapacity(minimumCapacity);
                break;

            case LONG:

                Checks.isLongMinimumCapacity(minimumCapacity);
                break;

            default:
                throw new UnsupportedOperationException();
            }

            return minimumCapacity;
        }
    }

    static final class CapacityInstanceAllocatorNode<T> extends InstanceNodeAllocator.InstanceAllocatorNode<T, CapacityInstanceAllocatorNode<T>> {

        CapacityInstanceAllocatorNode(AllocationType allocationType) {
            super(allocationType);
        }

        long capacity;
    }

    private final CapacityMax capacityMax;
    private final Object parameter;
    private final ObjLongFunction<Object, T> createInstance;
    private final ToLongFunction<T> capacityGetter;
    private final boolean exactCapacityMatch;

    protected <P> BaseCapacityInstanceAllocator(CapacityMax capacityMax, P parameter, ObjLongFunction<P, T> createInstance, ToLongFunction<T> capacityGetter) {
        this(capacityMax, parameter, createInstance, capacityGetter, false);
    }

    <P> BaseCapacityInstanceAllocator(CapacityMax capacityMax, P parameter, ObjLongFunction<P, T> createInstance, ToLongFunction<T> capacityGetter, boolean exactCapacityMatch) {

        Objects.requireNonNull(capacityMax);
        Objects.requireNonNull(createInstance);
        Objects.requireNonNull(capacityGetter);

        this.capacityMax = capacityMax;
        this.parameter = parameter;

        @SuppressWarnings("unchecked")
        final ObjLongFunction<Object, T> createFunction = (ObjLongFunction<Object, T>)createInstance;

        this.createInstance = createFunction;

        this.capacityGetter = capacityGetter;
        this.exactCapacityMatch = exactCapacityMatch;
    }

    protected final T allocateFromFreeListOrCreateCapacityInstance(long minimumCapacity) {

        CapacityMax.checkLongMinimumCapacity(capacityMax, minimumCapacity);

        final T result;

        final boolean fromFreeList;

        if (instanceFreeList == null) {

            result = createInstance.apply(parameter, minimumCapacity);

            fromFreeList = false;
        }
        else {
            CapacityInstanceAllocatorNode<T> previousNode = null;
            CapacityInstanceAllocatorNode<T> foundNode = null;

            final boolean exactMatch = exactCapacityMatch;

            for (CapacityInstanceAllocatorNode<T> node = instanceFreeList; node != null; node = node.next) {

                final boolean canBeAllocated = exactMatch
                        ? node.capacity == minimumCapacity
                        : node.capacity >= minimumCapacity;

                if (canBeAllocated) {

                    foundNode = node;
                    break;
                }

                previousNode = node;
            }

            if (foundNode != null) {

                final CapacityInstanceAllocatorNode<T> nextNode = foundNode.next;

                if (previousNode != null) {

                    previousNode.next = nextNode;
                }
                else {
                    this.instanceFreeList = nextNode;
                }

                result = foundNode.instance;

                foundNode.init(nodeFreeList, true, false, null);

                nodeFreeList = foundNode;

                fromFreeList = true;
            }
            else {
                result = createInstance.apply(parameter, minimumCapacity);

                fromFreeList = false;
            }
        }

        addAllocatedInstance(fromFreeList);

        return result;
    }

    protected final void freeArrayInstance(T array) {

        Objects.requireNonNull(array);

        addFreedInstance(true);

        final CapacityInstanceAllocatorNode<T> allocatorNode;

        if (nodeFreeList == null) {

            allocatorNode = new CapacityInstanceAllocatorNode<>(AllocationType.CACHING_ALLOCATOR);
        }
        else {
            allocatorNode = nodeFreeList;

            this.nodeFreeList = nodeFreeList.next;
        }

        allocatorNode.capacity = capacityGetter.applyAsLong(array);

        if (instanceFreeList == null) {

            allocatorNode.init(null, true, true, array);

            this.instanceFreeList = allocatorNode;
        }
        else {
            CapacityInstanceAllocatorNode<T> foundNode = null;

            for (CapacityInstanceAllocatorNode<T> node = instanceFreeList; node != null; node = node.next) {

                if (node.instance == array) {

                    throw new IllegalArgumentException();
                }
            }

            final long capacity = capacityGetter.applyAsLong(array);

            for (CapacityInstanceAllocatorNode<T> node = instanceFreeList; node != null; node = node.next) {

                if (capacity >= node.capacity) {

                    foundNode = node;
                    break;
                }
            }

            final CapacityInstanceAllocatorNode<T> nextNode;

            if (foundNode != null) {

                nextNode = foundNode.next;
                foundNode.next = allocatorNode;
            }
            else {
                nextNode = instanceFreeList;
                this.instanceFreeList = allocatorNode;
            }

            allocatorNode.init(nextNode, true, true, array);
        }
    }

    final CapacityMax getCapacityMax() {

        return capacityMax;
    }
}
