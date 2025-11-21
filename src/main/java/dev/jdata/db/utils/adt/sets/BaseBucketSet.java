package dev.jdata.db.utils.adt.sets;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.capacity.CapacityExponents;
import dev.jdata.db.utils.adt.lists.IMultiHeadNodeListMutable;
import dev.jdata.db.utils.adt.lists.ISinglyLinkedMultiHeadNodeListView;
import dev.jdata.db.utils.adt.lists.LargeNodeLists;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseBucketSet<T, U extends ISinglyLinkedMultiHeadNodeListView & IMultiHeadNodeListMutable<?>, V extends BaseBucketSet<T, U, V>>

        extends BaseIntCapacityExponentSet<T, V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_BUCKET_SET;

    static final long NO_LONG_NODE = LargeNodeLists.NO_LONG_NODE;

    private U buckets;

    abstract void rehashBuckets(T hashArray, T newHashArray, int newKeyMask, U buckets, U newBuckets);

    BaseBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, int bucketsInnerCapacityExponent,
            IntFunction<T> createHashed, Consumer<T> clearHashed, BiIntToObjectFunction<U> createBuckets) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, clearHashed);

        Checks.isIntCapacityExponent(bucketsInnerCapacityExponent);
        Objects.requireNonNull(createBuckets);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("bucketsInnerCapacityExponent", bucketsInnerCapacityExponent).add("createHashed", createHashed)
                    .add("clearHashed", clearHashed).add("createBuckets", createBuckets));
        }

        final int bucketsInnerCapacity = CapacityExponents.computeIntCapacityFromExponent(bucketsInnerCapacityExponent);

        this.buckets = createBuckets.apply(DEFAULT_BUCKETS_OUTER_INITIAL_CAPACITY, bucketsInnerCapacity);

        if (DEBUG) {

            exit();
        }
    }

    BaseBucketSet(AllocationType allocationType, BaseBucketSet<T, U, V> toCopy, Function<T, T> copyHashed) {
        super(allocationType, toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        @SuppressWarnings("unchecked")
        final U toCopyBuckets = (U)toCopy.getBuckets().createEmptyWithSameCapacity();

        this.buckets = toCopyBuckets;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected void resetToNull() {

        super.resetToNull();

        this.buckets = null;
    }

    @Override
    protected final void rehashWithKeyMask(T hashArray, T newHashArray, int newCapacity, int capacityExponentIncrease, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).add("newCapacity", newCapacity).add("capacityExponentIncrease", capacityExponentIncrease)
                    .hex("newKeyMask", newKeyMask));
        }

        final U oldBuckets = buckets;

        @SuppressWarnings("unchecked")
        final U newBuckets = this.buckets = (U)oldBuckets.createEmptyWithCapacityExponentIncrease(capacityExponentIncrease);

        rehashBuckets(hashArray, newHashArray, newKeyMask, oldBuckets, newBuckets);

        if (DEBUG) {

            exit(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).add("newCapacity", newCapacity).add("capacityExponentIncrease", capacityExponentIncrease)
                    .hex("newKeyMask", newKeyMask));
        }
    }

    final U getBuckets() {
        return buckets;
    }

    final void clearBaseBucketSet() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        buckets.clear();

        if (DEBUG) {

            exit();
        }
    }
}
