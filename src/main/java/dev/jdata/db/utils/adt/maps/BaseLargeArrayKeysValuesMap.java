package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

public abstract class BaseLargeArrayKeysValuesMap<KEYS extends IMutableLargeArrayMarker, VALUES> extends BaseLargeArrayKeysMap<KEYS> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_ARRAY_KEYS_VALUES_MAP;

    protected abstract int getOuterCapacity();
    protected abstract int getInnerCapacity();

    abstract <KEYS_DST, VALUES_DST> long keysAndValues(KEYS_DST keysDst, VALUES_DST valuesDst,
            ILongCapacityMapIndexKeyValueAdder<KEYS, VALUES, KEYS_DST, VALUES_DST> keyValueAdder);

    private final BiIntToObjectFunction<VALUES> createValues;
    private VALUES values;

    BaseLargeArrayKeysValuesMap(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<KEYS> createHashed, Consumer<KEYS> clearHashed, BiIntToObjectFunction<VALUES> createValues) {
        super(allocationType, initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacityExponent).add("innerCapacityExponent", innerCapacityExponent)
                    .add("loadFactor", loadFactor).add("createHashed", createHashed).add("clearHashed", clearHashed).add("createValues", createValues));
        }

        this.createValues = Objects.requireNonNull(createValues);

        this.values = createValues.apply(initialOuterCapacityExponent, innerCapacityExponent);

        if (DEBUG) {

            exit();
        }
    }

    BaseLargeArrayKeysValuesMap(AllocationType allocationType, BaseLargeArrayKeysValuesMap<KEYS, VALUES> toCopy, Function<KEYS, KEYS> copyHashed,
            BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed).add("copyValuesContent", copyValuesContent));
        }

        final BiIntToObjectFunction<VALUES> createValues = this.createValues = toCopy.createValues;

        final VALUES values = this.values = createValues.apply(getOuterCapacity(), getInnerCapacity());

        copyValuesContent.accept(toCopy.values, values);

        if (DEBUG) {

            exit();
        }
    }

    protected final VALUES createValues(int outerCapacity, int innerCapacityExponent) {

        Checks.isOuterCapacity(outerCapacity);
        Checks.isIntCapacityExponent(innerCapacityExponent);

        if (DEBUG) {

            enter(b -> b.add("outerCapacity", outerCapacity).add("innerCapacityExponent", innerCapacityExponent));
        }

        final VALUES result = createValues.apply(outerCapacity, innerCapacityExponent);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    protected final VALUES getValues() {
        return values;
    }

    final void setValues(VALUES values) {

        this.values = Objects.requireNonNull(values);
    }

    protected final VALUES reallocateValues(int newOuterCapacity) {

        if (DEBUG) {

            enter(b -> b.add("newOuterCapacity", newOuterCapacity));
        }

        final VALUES result = this.values = createValues.apply(newOuterCapacity, getInnerCapacityExponent());

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
