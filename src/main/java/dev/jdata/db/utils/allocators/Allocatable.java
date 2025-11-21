package dev.jdata.db.utils.allocators;

import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.checks.Checks;

public abstract class Allocatable {

    private static enum Mechanism {

        HEAP,
        CACHE,
        ARRAY
    }

    public static enum AllocationType {

        HEAP(Mechanism.HEAP, false, AllocatableState.ALLOCATED),
        HEAP_CONSTANT(Mechanism.HEAP, false, AllocatableState.ALLOCATED),
        HEAP_ALLOCATOR(Mechanism.HEAP, false, AllocatableState.DISPOSED),
        CACHING_ALLOCATOR(Mechanism.CACHE, true, AllocatableState.ALLOCATED, AllocatableState.FREE),
        ARRAY_ALLOCATOR(Mechanism.ARRAY, false, AllocatableState.ALLOCATED, AllocatableState.FREE);

        public static AllocationType checkIsHeap(AllocationType allocationType) {

            if (allocationType.isHeap()) {

                throw new IllegalArgumentException();
            }

            return allocationType;
        }

        public static AllocationType checkIsCached(AllocationType allocationType) {

            if (allocationType.isCached()) {

                throw new IllegalArgumentException();
            }

            return allocationType;
        }

        private final Mechanism mechanism;
        private final boolean isRecreatable;
        private final Allocatable.AllocatableState[] applicableStates;

        private AllocationType(Mechanism mechanism, boolean isRecreatable, Allocatable.AllocatableState ... applicableStates) {

            this.mechanism = Objects.requireNonNull(mechanism);
            this.isRecreatable = isRecreatable;
            this.applicableStates = Checks.isNotEmpty(applicableStates);
        }

        public boolean isHeap() {

            return mechanism == Mechanism.HEAP;
        }

        public boolean isCached() {

            return mechanism == Mechanism.CACHE;
        }

        public boolean isRecreatable() {
            return isRecreatable;
        }

        private boolean isApplicable(Allocatable.AllocatableState allocatableState) {

            Objects.requireNonNull(allocatableState);

            return Array.contains(applicableStates, allocatableState, (a, s) -> a == s);
        }

        private boolean isConstructorInitializedByDefault() {

            return isHeap();
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
        this(allocationType, allocationType.isConstructorInitializedByDefault());
    }

    protected Allocatable(AllocationType allocationType, boolean isConstructorInitialized) {

        this.allocationType = Objects.requireNonNull(allocationType);

        this.state = isConstructorInitialized ? AllocatableState.ALLOCATED : AllocatableState.FREE;
    }

    protected final AllocationType getAllocationType() {
        return allocationType;
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

        checkIsApplicableToAllocationType(AllocatableState.DISPOSED);

        if (state != AllocatableState.ALLOCATED) {

            throw new IllegalStateException();
        }

        this.state = AllocatableState.DISPOSED;
    }

    protected final void checkIsAllocatedRenamed() {

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

    private void checkIsApplicableToAllocationType(Allocatable.AllocatableState allocatableState) {

        Objects.requireNonNull(allocatableState);

        if (!allocationType.isApplicable(allocatableState)) {

            throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {

        return Allocatable.class.getSimpleName() + " [allocationType=" + allocationType + ", allocated=" + state + ']';
    }
}
