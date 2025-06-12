package dev.jdata.db.utils.allocators;

abstract class InstanceNodeAllocator<T, N extends InstanceNodeAllocator.InstanceAllocatorNode<T, N>> extends BaseNodeAllocator<T, N> {

    static class InstanceAllocatorNode<T, N extends InstanceAllocatorNode<T, N>> extends AllocatorNode<N> {

        T instance;

        void init(N next, boolean setAllocated , boolean allocated, T instance) {

            super.init(next, setAllocated, allocated);

            this.instance = instance;
        }
    }

    private N instanceFreeList;
    private N nodeFreeList;

    InstanceNodeAllocator() {

        this.instanceFreeList = null;
        this.nodeFreeList = null;
    }
}
