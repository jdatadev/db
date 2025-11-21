package dev.jdata.db.utils.adt.hashed;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.adt.lists.IMutableLongLargeSinglyLinkedMultiHeadNodeList;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLongCapacityArrayHashed<T extends IMutableLargeArrayMarker> extends BaseLongCapacityHashed<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_CAPACITY_ARRAY_HASHED;

    protected static final int DEFAULT_INNER_CAPACITY_EXPONENT = HashedConstants.DEFAULT_INNER_CAPACITY_EXPONENT;

    private final int innerCapacityExponent;
    private final BiIntToObjectFunction<T> createHashed;

    BaseLongCapacityArrayHashed(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, float loadFactor, int recreateOuterCapacity,
            BiIntToObjectFunction<T> createHashed, Consumer<T> clearHashed) {
        super(allocationType, initialOuterCapacity *  CapacityExponents.computeLongCapacityFromExponent(innerCapacityExponent), loadFactor,
                () -> createHashed.apply(initialOuterCapacity, innerCapacityExponent), clearHashed, () -> createHashed.apply(recreateOuterCapacity, innerCapacityExponent));

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent)
                    .add("loadFactor", loadFactor).add("recreateOuterCapacity", recreateOuterCapacity).add("createHashed", createHashed).add("clearHashed", clearHashed));
        }

        this.innerCapacityExponent = Checks.isIntCapacityExponent(innerCapacityExponent);
        this.createHashed = Objects.requireNonNull(createHashed);

        if (DEBUG) {

            exit();
        }
    }

    BaseLongCapacityArrayHashed(AllocationType allocationType, BaseLongCapacityArrayHashed<T> toCopy, Function<T, T> copyHashed) {
        super(allocationType, toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        this.innerCapacityExponent = toCopy.innerCapacityExponent;
        this.createHashed = toCopy.createHashed;

        if (DEBUG) {

            exit();
        }
    }

    protected final int getInnerCapacityExponent() {
        return innerCapacityExponent;
    }

    protected static <T> IMutableLongLargeSinglyLinkedMultiHeadNodeList<T> createBuckets(int initialOuterCapacityExponent, int innerCapacityExponent) {

        final int initialOuterCapacity = CapacityExponents.computeIntCapacityFromExponent(initialOuterCapacityExponent);
        final int innerCapacity = CapacityExponents.computeIntCapacityFromExponent(innerCapacityExponent);

        return IMutableLongLargeSinglyLinkedMultiHeadNodeList.create(initialOuterCapacity, innerCapacity);
    }
}
