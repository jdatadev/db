package dev.jdata.db.utils.adt.hashed;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.allocators.BaseCapacityInstanceAllocator.CapacityMax;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLongCapacityHashed<T> extends BaseCapacityHashed<T, Void, Void> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_CAPACITY_HASHED;

    protected abstract T rehash(T hashed, long newCapacity);

    BaseLongCapacityHashed(AllocationType allocationType, long initialCapacity, float loadFactor, Supplier<T> createHashed, Consumer<T> clearHashed, Supplier<T> recreateHashed) {
        super(allocationType, initialCapacity, loadFactor, CapacityMax.LONG, createHashed, clearHashed, recreateHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacity", initialCapacity).add("loadFactor", loadFactor).add("createHashed", createHashed)
                    .add("clearHashed", clearHashed).add("recreateHashed", recreateHashed));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongCapacityHashed(AllocationType allocationType, BaseLongCapacityHashed<T> toCopy, Function<T, T> copyHashed) {
        super(allocationType, toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter, IMakeFromElementsFunction<T, Void, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void recreateElements() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void resetToNull() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected final Void copyValues(Void values, long startIndex, long numElements) {

        checkLongCopyValuesParameters(values, 0L, startIndex, numElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void initializeWithValues(Void values, long numElements) {

        checkLongIntitializeWithValuesParameters(values, 0L, numElements);

        throw new UnsupportedOperationException();
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
