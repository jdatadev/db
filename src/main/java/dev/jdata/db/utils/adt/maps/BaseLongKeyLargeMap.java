package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.elements.ILongAnyOrderAddable;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLongKeyLargeMap<VALUES> extends BaseIntegerKeyLargeMap<IMutableLongLargeArray, VALUES> implements ILongKeyMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_LONG_KEY_MAP;

    static final long NO_INDEX = HashArray.NO_INDEX;

    static final long NO_KEY = -1L;

    protected abstract long getHashArrayIndex(long key, long keyMask);

    abstract <S, D> long keysAndValues(IMutableLongLargeArray keysDst, S src, D dst, LongMapIndexValueSetter<S, D> valueSetter);

    BaseLongKeyLargeMap(int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<IMutableLongLargeArray> createHashed, Consumer<IMutableLongLargeArray> clearHashed, BiIntToObjectFunction<VALUES> createValues) {
        super(initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed, createValues);
    }

    BaseLongKeyLargeMap(BaseLongKeyLargeMap<VALUES> toCopy, BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(toCopy, IMutableLongLargeArray::copyOf, copyValuesContent);
    }

    @Override
    public final long keys(ILongAnyOrderAddable addable) {

        Objects.requireNonNull(addable);

        if (DEBUG) {

            enter(b -> b.add("addable", addable));
        }

        final long result = keysAndValues(addable, null, (index, keysSrc, keysDst, valuesSrc, valuesDst) -> keysDst.addInAnyOrder(keysSrc.get(index)));

        if (DEBUG) {

            exit(result, b -> b.add("addable", addable));
        }

        return result;
    }
}
