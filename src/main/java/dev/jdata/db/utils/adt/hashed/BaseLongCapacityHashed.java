package dev.jdata.db.utils.adt.hashed;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Supplier;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.capacity.Capacity;
import dev.jdata.db.utils.adt.capacity.CapacityExponents;
import dev.jdata.db.utils.adt.capacity.CapacityMax;
import dev.jdata.db.utils.adt.capacity.IOuterInnerInstantiator;
import dev.jdata.db.utils.adt.capacity.IOuterInnerWithValuesInstantiator;
import dev.jdata.db.utils.adt.capacity.IWithCapacityExponentInstantiator;
import dev.jdata.db.utils.adt.capacity.IWithCapacityExponentInstantiator2;
import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.ObjIntFunction;

abstract class BaseLongCapacityHashed<T> extends BaseCapacityHashed<T, Void, Void, Void> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_CAPACITY_HASHED;

    protected static <T extends IMutable> T instantiateWithCapacityExponent(AllocationType allocationType, AllocationMechanism expectedAllocationMechanism,
            int initialCapacity, ObjIntFunction<AllocationType, T> instantiator) {

        checkInstantiateParameters(allocationType, expectedAllocationMechanism, initialCapacity, instantiator);

        return instantiateWithCapacityExponent(allocationType, initialCapacity, instantiator, null, null, (a, e, i, p2, p3) -> i.apply(a, e));
    }

    protected static <T extends IMutable, U> T instantiateWithCapacityExponent(AllocationType allocationType, AllocationMechanism expectedAllocationMechanism,
            int initialCapacity, IntFunction<U> createElements, IWithCapacityExponentInstantiator<T, IntFunction<U>> instantiator) {

        checkInstantiateParameters(allocationType, expectedAllocationMechanism, initialCapacity, instantiator);

        return instantiateWithCapacityExponent(allocationType, initialCapacity, instantiator, createElements, null, (a, e, i, c, p3) -> i.instantiate(a, e, c));
    }

    protected static <T extends IMutable, U, V> T instantiateWithCapacityExponent(AllocationType allocationType, AllocationMechanism expectedAllocationMechanism,
            int initialCapacity, IntFunction<U> createElements1, IntFunction<V> createElements2,
            IWithCapacityExponentInstantiator2<T, IntFunction<U>, IntFunction<V>> instantiator) {

        checkInstantiateParameters(allocationType, expectedAllocationMechanism, initialCapacity, instantiator);

        return instantiateWithCapacityExponent(allocationType, initialCapacity, instantiator, createElements1, createElements2, (a, e, i, c1, c2) -> i.instantiate(a, e, c1, c2));
    }

    private static <T extends IMutable, P1, P2, P3> T instantiateWithCapacityExponent(AllocationType allocationType, int initialCapacity, P1 parameter1, P2 parameter2,
            P3 parameter3, WithCapacityExponentInstantiator3<T, P1, P2, P3> instantiator) {

        return instantiator.instantiate(allocationType, CapacityExponents.computeIntCapacityExponentForAtOrAboveZero(initialCapacity), parameter1, parameter2, parameter3);
    }

    protected static <T> T instantiateWithOuterExponentInnerExponent(AllocationType allocationType, AllocationMechanism expectedAllocationMechanism, long initialCapacity,
            IOuterInnerInstantiator<T> instantiator) {

        checkInstantiateParameters(allocationType, expectedAllocationMechanism, initialCapacity, instantiator);

        return Capacity.instantiateOuterExponentInnerExponent(allocationType, initialCapacity, instantiator);
    }

    protected static <T, U> T instantiateWithOuterExponentInnerExponentAndValues(AllocationType allocationType, AllocationMechanism expectedAllocationMechanism,
            int initialOuterCapacityExponent, int innerCapacityExponent, U values, int valuesNumElements, IOuterInnerWithValuesInstantiator<T, U> instantiator) {

        Checks.isIntInitialOuterCapacityExponent(initialOuterCapacityExponent);
        Checks.isIntInnerCapacityExponent(innerCapacityExponent);
        Objects.requireNonNull(values);
        Checks.isAboveZero(valuesNumElements);
        Objects.requireNonNull(instantiator);

        return instantiator.instantiate(allocationType, initialOuterCapacityExponent, innerCapacityExponent, values);
    }

    @Deprecated // int initial capacity necessary?
    private static void checkInstantiateParameters(AllocationType allocationType, AllocationMechanism expectedAllocationMechanism, int initialCapacity, Object instantiator) {

        AllocationType.checkIsAllocationMechanism(allocationType, expectedAllocationMechanism);
        Checks.isIntInitialCapacityAtOrAboveZero(initialCapacity);
        Objects.requireNonNull(instantiator);
    }

    private static void checkInstantiateParameters(AllocationType allocationType, AllocationMechanism expectedAllocationMechanism, long initialCapacity, Object instantiator) {

        AllocationType.checkIsAllocationMechanism(allocationType, expectedAllocationMechanism);
        Checks.isLongInitialCapacityAtOrAboveZero(initialCapacity);
        Objects.requireNonNull(instantiator);
    }

    protected abstract void rehashWithCapacity(T hashed, T newHashed, long newCapacity);

    BaseLongCapacityHashed(AllocationType allocationType, long initialCapacity, float loadFactor, LongFunction<T> createHashed, Consumer<T> clearHashed,
            Supplier<T> recreateHashed) {
        super(allocationType, initialCapacity, loadFactor, CapacityMax.LONG, createHashed, clearHashed, recreateHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacity", initialCapacity).add("loadFactor", loadFactor).add("createHashed", createHashed)
                    .add("clearHashed", clearHashed).add("recreateHashed", recreateHashed));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongCapacityHashed(AllocationType allocationType, BaseLongCapacityHashed<T> toCopy, Function<T, T> copyHashed) {
        super(allocationType, toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter, IMakeFromElementsFunction<Void, Void, P, R> makeFromElements) {

        checkMakeFromElementsParameters(allocationType, parameter, makeFromElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void recreateElements() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void resetToNull() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected final Void copyValues(Void values, long startIndex, long numElements) {

        checkLongCopyValuesParameters(values, 0L, startIndex, numElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void initializeWithValues(Void values, long numElements) {

        checkLongIntitializeWithValuesParameters(values, 0L, numElements);

        throw new UnsupportedOperationException();
    }

    protected final long getHashedCapacity() {

        return getLongCapacity();
    }

    protected final long increaseCapacityAndRehash() {

        if (DEBUG) {

            enter();
        }

        final long result = increaseCapacityAndRehashAndReturnLongCapacity(this, (h, n, c, i) -> i.rehashWithCapacity(h, n, c));

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
