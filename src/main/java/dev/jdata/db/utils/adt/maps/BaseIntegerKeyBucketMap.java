package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.hashed.helpers.IntBuckets;
import dev.jdata.db.utils.adt.lists.BaseLargeSinglyLinkedList;
import dev.jdata.db.utils.adt.lists.BaseValues;
import dev.jdata.db.utils.adt.lists.LargeLists;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseIntegerKeyBucketMap<
                KEYS,
                LIST_T,
                LIST extends BaseLargeSinglyLinkedList<MAP, LIST_T, LIST, VALUES>,
                VALUES extends BaseValues<LIST_T, LIST, VALUES>,
                MAP extends BaseIntegerKeyBucketMap<KEYS, LIST_T, LIST, VALUES, MAP>>

        extends BaseIntCapacityExponentMap<KEYS> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INTEGER_KEY_BUCKET_MAP;

    static final long NO_LONG_NODE = LargeLists.NO_LONG_NODE;

    abstract LIST createBuckets(int outerInitialCapacity, int bucketsInnerCapacity);

    private LIST buckets;

    BaseIntegerKeyBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, int bucketsInnerCapacityExponent, IntFunction<KEYS> createHashed,
            Consumer<KEYS> clearHashed, BiIntToObjectFunction<LIST> createBuckets) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);

        Checks.isIntCapacityExponent(bucketsInnerCapacityExponent);
        Objects.requireNonNull(createBuckets);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("bucketsInnerCapacityExponent", bucketsInnerCapacityExponent).add("clearHashed", clearHashed).add("createBuckets", createBuckets));
        }

        final int bucketsOuterCapacity = DEFAULT_BUCKETS_OUTER_INITIAL_CAPACITY;
        final int bucketsInnerCapacity = CapacityExponents.computeIntCapacityFromExponent(bucketsInnerCapacityExponent);

        IntBuckets.checkIntNodeOuterCapacity(bucketsOuterCapacity);
        IntBuckets.checkIntNodeInnerCapacity(bucketsInnerCapacity);

        this.buckets = createBuckets.apply(bucketsOuterCapacity, bucketsInnerCapacity);

        if (DEBUG) {

            exit();
        }
    }

    BaseIntegerKeyBucketMap(MAP toCopy, Function<KEYS, KEYS> copyHashed) {
        super(toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        if (Boolean.TRUE) {

            throw new UnsupportedOperationException();
        }

        if (DEBUG) {

            exit();
        }
    }

    final int getBucketsInnerCapacity() {

        return buckets.getInnerArrayCapacity();
    }

    final LIST getBuckets() {
        return buckets;
    }

    final void setBuckets(LIST buckets) {

        this.buckets = Objects.requireNonNull(buckets);
    }

    final void clearMap() {

        clearHashed();

        buckets.clear();
    }
}
