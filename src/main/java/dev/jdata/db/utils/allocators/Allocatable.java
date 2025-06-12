package dev.jdata.db.utils.allocators;

import java.util.Objects;

public abstract class Allocatable {

    public static enum AllocationType {

        HEAP,
        HEAP_CONSTANT,
        HEAP_ALLOCATOR,
        CACHING_ALLOCATOR,
        ARRAY_ALLOCATOR
    }

    private final AllocationType allocationType;

    private boolean allocated;

    protected Allocatable(AllocationType allocationType) {

        this.allocationType = Objects.requireNonNull(allocationType);

        this.allocated = allocationType == AllocationType.HEAP || allocationType == AllocationType.HEAP_ALLOCATOR;
    }

    final boolean isAllocated() {
        return allocated;
    }

    final void setAllocated(boolean allocated) {

        if (this.allocated == allocated) {

            throw new IllegalStateException();
        }

        this.allocated = allocated;
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

        return getClass().getSimpleName() + " [allocationType=" + allocationType + ", allocated=" + allocated + "]";
    }
}
