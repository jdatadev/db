package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import dev.jdata.db.utils.checks.Checks;

public abstract class BaseArrayAllocator<T> extends InstanceNodeAllocator<T, BaseArrayAllocator.ArrayAllocatorNode<T>> {

    static final class ArrayAllocatorNode<T> extends InstanceNodeAllocator.InstanceAllocatorNode<T, ArrayAllocatorNode<T>> {

        int arrayLength;
    }

    private final IntFunction<T> createArray;
    private final ToIntFunction<T> arrayLengthGetter;
    private final boolean exactLengthMatch;

    private ArrayAllocatorNode<T> arrayFreeList;
    private ArrayAllocatorNode<T> nodeFreeList;

    protected BaseArrayAllocator(IntFunction<T> createArray, ToIntFunction<T> arrayLengthGetter) {
        this(createArray, arrayLengthGetter, false);
    }

    BaseArrayAllocator(IntFunction<T> createArray, ToIntFunction<T> arrayLengthGetter, boolean exactLengthMatch) {

        this.createArray = Objects.requireNonNull(createArray);
        this.arrayLengthGetter = Objects.requireNonNull(arrayLengthGetter);
        this.exactLengthMatch = exactLengthMatch;

        this.arrayFreeList = null;
        this.nodeFreeList = null;
    }

    protected final T allocateArrayInstance(int minimumCapacity) {

        Checks.isCapacity(minimumCapacity);

        final T result;

        final boolean fromFreeList;

        if (arrayFreeList == null) {

            result = createArray.apply(minimumCapacity);

            fromFreeList = false;
        }
        else {
            ArrayAllocatorNode<T> previousNode = null;
            ArrayAllocatorNode<T> foundNode = null;

            final boolean exactMatch = exactLengthMatch;

            for (ArrayAllocatorNode<T> node = arrayFreeList; node != null; node = node.next) {

                final boolean canBeAllocated = exactMatch
                        ? node.arrayLength == minimumCapacity
                        : node.arrayLength >= minimumCapacity;

                if (canBeAllocated) {

                    foundNode = node;
                    break;
                }

                previousNode = node;
            }

            if (foundNode != null) {

                final ArrayAllocatorNode<T> nextNode = foundNode.next;

                if (previousNode != null) {

                    previousNode.next = nextNode;
                }
                else {
                    this.arrayFreeList = nextNode;
                }

                result = foundNode.instance;

                foundNode.init(nodeFreeList, true, false, null);

                nodeFreeList = foundNode;

                fromFreeList = true;
            }
            else {
                result = createArray.apply(minimumCapacity);

                fromFreeList = false;
            }
        }

        addAllocatedInstance(fromFreeList);

        return result;
    }

    protected final void freeArrayInstance(T array) {

        Objects.requireNonNull(array);

        addFreedInstance(true);

        final ArrayAllocatorNode<T> allocatorNode;

        if (nodeFreeList == null) {

            allocatorNode = new ArrayAllocatorNode<>();
        }
        else {
            allocatorNode = nodeFreeList;

            this.nodeFreeList = nodeFreeList.next;
        }

        allocatorNode.arrayLength = arrayLengthGetter.applyAsInt(array);

        if (arrayFreeList == null) {

            allocatorNode.init(null, true, true, array);

            this.arrayFreeList = allocatorNode;
        }
        else {
            ArrayAllocatorNode<T> foundNode = null;

            for (ArrayAllocatorNode<T> node = arrayFreeList; node != null; node = node.next) {

                if (node.instance == array) {

                    throw new IllegalArgumentException();
                }
            }

            final int arrayLength = arrayLengthGetter.applyAsInt(array);

            for (ArrayAllocatorNode<T> node = arrayFreeList; node != null; node = node.next) {

                if (arrayLength >= node.arrayLength) {

                    foundNode = node;
                    break;
                }
            }

            final ArrayAllocatorNode<T> nextNode;

            if (foundNode != null) {

                nextNode = foundNode.next;
                foundNode.next = allocatorNode;
            }
            else {
                nextNode = arrayFreeList;
                this.arrayFreeList = allocatorNode;
            }

            allocatorNode.init(nextNode, true, true, array);
        }
    }
}
