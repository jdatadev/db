package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.strings.Strings;

abstract class BaseNodeAllocator<T, U extends BaseNodeAllocator.AllocatorNode<U>> extends BaseObjectAllocator<T> {

    public static class AllocatorNode<T extends AllocatorNode<T>> extends Allocatable {

        AllocatorNode() {
            super(AllocationType.CACHING_ALLOCATOR);
        }

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
}
