package dev.jdata.db.utils.allocators;

public abstract class Allocatable {

    private boolean allocated;

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
}
