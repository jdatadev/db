package dev.jdata.db.utils.allocators;

abstract class BaseNodeAllocator<T, N extends BaseNodeAllocator.AllocatorNode<T, N>> {

    static class AllocatorNode<T, N extends AllocatorNode<T, N>> {

        N next;
        T instance;

        void init(N next, T instance) {

            this.next = next;
            this.instance = instance;
        }
    }

    N instanceFreeList;
    N nodeFreeList;

    BaseNodeAllocator() {

        this.instanceFreeList = null;
        this.nodeFreeList = null;
    }
}
