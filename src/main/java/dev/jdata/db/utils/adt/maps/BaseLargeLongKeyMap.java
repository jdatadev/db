package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.function.BiIntToObjectFunction;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLargeLongKeyMap<T> extends BaseLargeIntegerKeyMap<LargeLongArray, T> implements ILargeLongKeyMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_LONG_KEY_MAP;

    static final long NO_INDEX = HashArray.NO_INDEX;

    static final long NO_KEY = -1L;

    protected abstract long getHashArrayIndex(long key, long keyMask);

    abstract <S, D> long keysAndValues(LargeLongArray keysDst, S src, D dst, LongMapIndexValueSetter<S, D> valueSetter);

    BaseLargeLongKeyMap(int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<LargeLongArray> createHashed, Consumer<LargeLongArray> clearHashed, BiIntToObjectFunction<T> createValues) {
        super(initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed, createValues);
    }

    BaseLargeLongKeyMap(BaseLargeLongKeyMap<T> toCopy, BiConsumer<T, T> copyValuesContent) {
        super(toCopy, LargeLongArray::copyOf, copyValuesContent);
    }

    @Override
    public final LargeLongArray keys() {

        if (DEBUG) {

            enter();
        }

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());
        final int innerCapacityExponent = getInnerCapacityExponent();

        final int outerCapacity = CapacityExponents.computeArrayOuterCapacity(numElements, innerCapacityExponent);

        final LargeLongArray result = new LargeLongArray(outerCapacity, innerCapacityExponent);

        keys(result);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    private long keys(LargeLongArray dst) {

        Objects.requireNonNull(dst);

        return keysAndValues(dst, null, null, null);
    }
}
