package dev.jdata.db.utils.adt.hashed;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.arrays.LargeExponentArray;
import dev.jdata.db.utils.adt.lists.LargeLongMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLongCapacityArrayHashed<T extends LargeExponentArray<?, ?>> extends BaseLongCapacityHashed<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_CAPACITY_ARRAY_HASHED;

    protected static final int DEFAULT_INNER_CAPACITY_EXPONENT = HashedConstants.DEFAULT_INNER_CAPACITY_EXPONENT;

    private final int innerCapacityExponent;
    private final BiIntToObjectFunction<T> createHashed;

    BaseLongCapacityArrayHashed(int initialOuterCapacity, int innerCapacityExponent, float loadFactor, BiIntToObjectFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialOuterCapacity *  CapacityExponents.computeLongCapacityFromExponent(innerCapacityExponent), loadFactor,
                () -> createHashed.apply(initialOuterCapacity, innerCapacityExponent), clearHashed);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor)
                    .add("createHashed", createHashed).add("clearHashed", clearHashed));
        }

        this.innerCapacityExponent = Checks.isIntCapacityExponent(innerCapacityExponent);
        this.createHashed = Objects.requireNonNull(createHashed);

        if (DEBUG) {

            exit();
        }
    }

    protected BaseLongCapacityArrayHashed(BaseLongCapacityArrayHashed<T> toCopy, Function<T, T> copyHashed) {
        super(toCopy, copyHashed);

        this.innerCapacityExponent = toCopy.innerCapacityExponent;
        this.createHashed = toCopy.createHashed;
    }

    protected final int getInnerCapacityExponent() {
        return innerCapacityExponent;
    }

    protected static <T> LargeLongMultiHeadSinglyLinkedList<T> createBuckets(int initialOuterCapacityExponent, int innerCapacityExponent) {

        final int initialOuterCapacity = CapacityExponents.computeIntCapacityFromExponent(initialOuterCapacityExponent);
        final int innerCapacity = CapacityExponents.computeIntCapacityFromExponent(innerCapacityExponent);

        return new LargeLongMultiHeadSinglyLinkedList<>(initialOuterCapacity, innerCapacity);
    }
}
