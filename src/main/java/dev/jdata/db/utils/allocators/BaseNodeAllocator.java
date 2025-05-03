package dev.jdata.db.utils.allocators;

abstract class BaseNodeAllocator<T, U extends BaseNodeAllocator.AllocatorNode<U>> extends BaseObjectAllocator<T> {

    public static class AllocatorNode<T extends AllocatorNode<T>> extends Allocatable {

        T next;

        final void init(T next, boolean allocated) {

            setAllocated(allocated);

            this.next = next;
        }
    }
}
