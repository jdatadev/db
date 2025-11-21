package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.jdk.adt.strings.Strings;

public abstract class AllocatorNode<T extends AllocatorNode<T>> extends Allocatable {

    AllocatorNode(AllocationType allocationType) {
        super(allocationType);
    }

    T next;

    final void init(T next, boolean setAllocated , boolean allocated) {

        if (setAllocated) {

            setAllocated(allocated);
        }

        this.next = next;
    }

    @Override
    public String toString() {

        return AllocatorNode.class.getSimpleName() + " [super=" + super.toString() + ", next=" + Strings.nullOrNonNullString(next) + ']';
    }
}
