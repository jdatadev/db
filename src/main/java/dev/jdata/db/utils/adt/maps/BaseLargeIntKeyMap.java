package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.arrays.LargeIntArray;
import dev.jdata.db.utils.adt.hashed.helpers.LargeHashArray;
import dev.jdata.db.utils.function.BiIntToObjectFunction;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLargeIntKeyMap<T> extends BaseLargeIntegerKeyMap<LargeIntArray, T> implements ILargeIntKeyMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_INT_KEY_MAP;

    static final long NO_INDEX = LargeHashArray.NO_INDEX;

    static final int NO_KEY = -1;

    protected abstract long getHashArrayIndex(int key, long keyMask);

    abstract <S, D> long keysAndValues(LargeIntArray keysDst, S src, D dst, LongMapIndexValueSetter<S, D> valueSetter);

    BaseLargeIntKeyMap(int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<LargeIntArray> createHashed, Consumer<LargeIntArray> clearHashed, BiIntToObjectFunction<T> createValues) {
        super(initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed, createValues);
    }

    BaseLargeIntKeyMap(BaseLargeIntKeyMap<T> toCopy, BiConsumer<T, T> copyValuesContent) {
        super(toCopy, LargeIntArray::copyOf, copyValuesContent);
    }

    @Override
    public final LargeIntArray keys() {

        if (DEBUG) {

            enter();
        }

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());
        final int innerCapacityExponent = getInnerCapacityExponent();

        final int outerCapacity = CapacityExponents.computeArrayOuterCapacity(numElements, innerCapacityExponent);

        final LargeIntArray result = new LargeIntArray(outerCapacity, innerCapacityExponent);

        keys(result);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    private long keys(LargeIntArray dst) {

        Objects.requireNonNull(dst);

        return keysAndValues(dst, null, null, null);
    }
}
