package dev.jdata.db.data.locktable;

import java.util.Objects;

import dev.jdata.db.LockType;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.checks.Checks;

public final class LockedTables extends LockedTableElements {

    private final int[] numReadLocked;
    private final int[] numWriteLocked;

    LockedTables(int[] numReadLocked, int[] numWriteLocked) {

        Objects.requireNonNull(numReadLocked);
        Objects.requireNonNull(numWriteLocked);
        Checks.areSameLength(numReadLocked, numWriteLocked);

        this.numReadLocked = Array.copyOf(numReadLocked);
        this.numWriteLocked = Array.copyOf(numWriteLocked);
    }

    @Override
    public boolean isEmpty() {

        return numReadLocked.length == 0;
    }

    @Override
    public long getNumElements() {

        return numReadLocked.length;
    }

    @Override
    public int getNumLocks(long index, LockType lockType) {

        final int result;

        final int tableId = IElements.intIndex(index);

        switch (lockType) {

        case READ:

            result = getNumReadLocked(tableId);
            break;

        case WRITE:

            result = getNumWriteLocked(tableId);
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return result;
    }

    public int getNumReadLocked(int tableId) {

        return numReadLocked[tableId];
    }

    public int getNumWriteLocked(int tableId) {

        return numWriteLocked[tableId];
    }
}
