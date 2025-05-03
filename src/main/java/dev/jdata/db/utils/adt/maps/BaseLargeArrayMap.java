package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.Consumer;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.LargeExponentArray;
import dev.jdata.db.utils.adt.hashed.BaseLargeArrayHashed;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

public abstract class BaseLargeArrayMap<T extends LargeExponentArray, U> extends BaseLargeArrayHashed<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_NON_BUCKET_MAP;

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_OBJECT_NON_CONTAINS_KEY_NON_BUCKET_MAP;

    private static final int NO_KEY = -1;
/*
    protected abstract void put(T values, long index, T newValues, long newIndex);
    protected abstract void clearValues(T values);
*/
    private final BiIntToObjectFunction<U> createValues;
    private U values;

    protected BaseLargeArrayMap(int initialOuterCapacity, int innerCapacityExponent, float loadFactor, BiIntToObjectFunction<T> createHashed, Consumer<T> clearHashed,
            BiIntToObjectFunction<U> createValues) {
        super(initialOuterCapacity, innerCapacityExponent, loadFactor, createHashed, clearHashed);

        this.createValues = Objects.requireNonNull(createValues);

        this.values = createValues.apply(initialOuterCapacity, innerCapacityExponent);
    }

    protected final U getValues() {
        return values;
    }

    protected final U reallocateValues(int newOuterCapacity) {

        final U result = this.values = createValues.apply(newOuterCapacity, getInnerCapacityExponent());

        return result;
    }
}
