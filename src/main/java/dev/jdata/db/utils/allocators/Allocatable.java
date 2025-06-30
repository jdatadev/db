package dev.jdata.db.utils.allocators;

import java.util.Objects;

public abstract class Allocatable {

    public static enum AllocationType {

        HEAP(true),
        HEAP_CONSTANT(true),
        HEAP_ALLOCATOR(true),
        CACHING_ALLOCATOR(false),
        ARRAY_ALLOCATOR(false);

        public static AllocationType checkIsHeap(AllocationType allocationType) {

            if (!allocationType.isHeap) {

                throw new IllegalArgumentException();
            }

            return allocationType;
        }

        private final boolean isHeap;

        private AllocationType(boolean isHeap) {

            this.isHeap = isHeap;
        }

        public boolean isHeap() {
            return isHeap;
        }
    }

    private static enum AllocatableState {

        FREE,
        ALLOCATED,
        DISPOSED
    }

    private final AllocationType allocationType;

    private AllocatableState state;

    protected Allocatable(AllocationType allocationType) {

        this.allocationType = Objects.requireNonNull(allocationType);

        this.state = allocationType == AllocationType.HEAP || allocationType == AllocationType.HEAP_ALLOCATOR ? AllocatableState.ALLOCATED : AllocatableState.FREE;
    }

    final boolean isAllocated() {

        return state == AllocatableState.ALLOCATED;
    }

    final void setAllocated(boolean allocated) {

        switch (state) {

        case DISPOSED:
            throw new IllegalStateException();

        case ALLOCATED:

            if (allocated) {

                throw new IllegalStateException();
            }

            this.state = AllocatableState.FREE;
            break;

        case FREE:

            if (!allocated) {

                throw new IllegalStateException();
            }

            this.state = AllocatableState.ALLOCATED;
            break;

        default:
            throw new UnsupportedOperationException();
        }
    }

    protected final void setDisposed() {

        if (state != AllocatableState.ALLOCATED) {

            throw new IllegalStateException();
        }

        this.state = AllocatableState.DISPOSED;
    }

    protected final void checkIsAllocated() {

        if (!isAllocated()) {

            throw new IllegalStateException();
        }
    }

    protected final void checkIsAllocated(boolean expectedIsAllocated) {

        if (isAllocated() != expectedIsAllocated) {

            throw new IllegalStateException();
        }
    }

    protected final void checkIsNotAllocated() {

        if (isAllocated()) {

            throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [allocationType=" + allocationType + ", allocated=" + state + "]";
    }
}
