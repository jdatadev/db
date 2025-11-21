package dev.jdata.db.utils.adt.hashed;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLongCapacityHashed<T> extends BaseCapacityHashed<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_CAPACITY_HASHED;

    protected abstract T rehash(T hashed, long newCapacity);

    BaseLongCapacityHashed(long initialCapacity, float loadFactor, Supplier<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacity, loadFactor, true, createHashed, clearHashed);

        if (DEBUG) {

            enter(b -> b.add("initialCapacity", initialCapacity).add("loadFactor", loadFactor).add("createHashed", createHashed).add("clearHashed", clearHashed));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongCapacityHashed(BaseLongCapacityHashed<T> toCopy, Function<T, T> copyHashed) {
        super(toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final long getHashedCapacity() {

        return getLongCapacity();
    }

    protected final long increaseCapacityAndRehash() {

        if (DEBUG) {

            enter();
        }

        final long result = increaseCapacityAndRehash(this, (h, c, i) -> i.rehash(h, Integers.checkUnsignedLongToUnsignedInt(c)));

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
