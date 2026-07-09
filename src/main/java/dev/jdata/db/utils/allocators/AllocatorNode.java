package dev.jdata.db.utils.allocators;

public abstract class AllocatorNode<T extends AllocatorNode<T>> extends Allocatable {

    AllocatorNode(AllocationType allocationType) {
        super(allocationType);
    }

    @StringNullOrNonNull
    T next;

    final void init(T next, boolean setAllocated , boolean allocated) {

        if (setAllocated) {

            setAllocated(allocated);
        }

        this.next = next;
    }
}
