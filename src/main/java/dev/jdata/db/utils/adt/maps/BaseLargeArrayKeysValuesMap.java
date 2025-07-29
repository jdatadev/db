package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.LargeExponentArray;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

public abstract class BaseLargeArrayKeysValuesMap<T extends LargeExponentArray<?, ?>, U> extends BaseLargeArrayKeysMap<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_ARRAY_KEYS_VALUES_MAP;

    protected abstract int getOuterCapacity();
    protected abstract int getInnerCapacity();

    private final BiIntToObjectFunction<U> createValues;
    private U values;

    protected BaseLargeArrayKeysValuesMap(int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<T> createHashed, Consumer<T> clearHashed, BiIntToObjectFunction<U> createValues) {
        super(initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacityExponent).add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor)
                    .add("createHashed", createHashed).add("clearHashed", clearHashed).add("createValues", createValues));
        }

        this.createValues = Objects.requireNonNull(createValues);

        this.values = createValues.apply(initialOuterCapacityExponent, innerCapacityExponent);

        if (DEBUG) {

            exit();
        }
    }

    BaseLargeArrayKeysValuesMap(BaseLargeArrayKeysValuesMap<T, U> toCopy, Function<T, T> copyHashed, BiConsumer<U, U> copyValuesContent) {
        super(toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyHashed", copyHashed).add("copyValuesContent", copyValuesContent));
        }

        final BiIntToObjectFunction<U> createValues = this.createValues = toCopy.createValues;

        final U values = this.values = createValues.apply(getOuterCapacity(), getInnerCapacity());

        copyValuesContent.accept(toCopy.values, values);

        if (DEBUG) {

            exit();
        }
    }

    protected final U createValues(int outerCapacity, int innerCapacityExponent) {

        Checks.isCapacity(outerCapacity);
        Checks.isIntCapacityExponent(innerCapacityExponent);

        if (DEBUG) {

            enter(b -> b.add("outerCapacity", outerCapacity).add("innerCapacityExponent", innerCapacityExponent));
        }

        final U result = createValues.apply(outerCapacity, innerCapacityExponent);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    protected final U getValues() {
        return values;
    }

    final void setValues(U values) {

        this.values = Objects.requireNonNull(values);
    }

    protected final U reallocateValues(int newOuterCapacity) {

        if (DEBUG) {

            enter(b -> b.add("newOuterCapacity", newOuterCapacity));
        }

        final U result = this.values = createValues.apply(newOuterCapacity, getInnerCapacityExponent());

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
