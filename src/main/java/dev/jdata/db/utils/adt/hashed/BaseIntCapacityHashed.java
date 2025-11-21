package dev.jdata.db.utils.adt.hashed;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.allocators.BaseCapacityInstanceAllocator.CapacityMax;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseIntCapacityHashed<T, U, V> extends BaseCapacityHashed<T, U, V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_CAPACITY_HASHED;

    protected abstract T rehash(T hashed, int newCapacity);

    BaseIntCapacityHashed(AllocationType allocationType, int initialCapacity, float loadFactor, Supplier<T> createHashed, Consumer<T> clearHashed, Supplier<T> recreateHashed) {
        super(allocationType, initialCapacity, loadFactor, CapacityMax.INT, createHashed, clearHashed, recreateHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacity", initialCapacity).add("loadFactor", loadFactor).add("createHashed", createHashed)
                    .add("clearHashed", clearHashed).add("recreateHashed", recreateHashed));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntCapacityHashed(AllocationType allocationType, BaseIntCapacityHashed<T, U, V> toCopy, Function<T, T> copyHashed) {
        super(allocationType, toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final int getHashedCapacity() {

        return getIntCapacity();
    }

    protected final int increaseCapacityAndRehash() {

        if (DEBUG) {

            enter();
        }

        final int result = increaseCapacityAndRehashReturnIntCapacity(this, (h, c, i) -> i.rehash(h, Integers.checkUnsignedLongToUnsignedInt(c)));

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    protected final void checkCapacity(int numAdditionalElements) {

        if (DEBUG) {

            enter(b -> b.add("numAdditionalElements", numAdditionalElements));
        }

        checkCapacity(numAdditionalElements, this, (h, c, i) -> i.rehash(h, Integers.checkUnsignedLongToUnsignedInt(c)));

        if (DEBUG) {

            exit();
        }
    }

    protected final int getMakeFromElementsNumElements() {

        return getHashedCapacity();
    }
}
