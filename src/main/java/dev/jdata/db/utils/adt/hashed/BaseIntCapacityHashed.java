package dev.jdata.db.utils.adt.hashed;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.capacity.CapacityExponents;
import dev.jdata.db.utils.adt.capacity.CapacityMax;
import dev.jdata.db.utils.adt.capacity.IWithCapacityExponentInstantiator;
import dev.jdata.db.utils.adt.capacity.IWithCapacityExponentInstantiator2;
import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.ObjIntFunction;

abstract class BaseIntCapacityHashed<HASHED, CREATE_ELEMENTS, MAKE_ELEMENTS_FROM, INIT_FROM_VALUES>

        extends BaseCapacityHashed<HASHED, CREATE_ELEMENTS, MAKE_ELEMENTS_FROM, INIT_FROM_VALUES> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_CAPACITY_HASHED;

    protected static <T extends IMutable> T instantiateWithCapacityExponent(AllocationType allocationType, AllocationMechanism expectedAllocationMechanism,
            int initialCapacity, ObjIntFunction<AllocationType, T> instantiator) {

        checkInstantiateParameters(allocationType, expectedAllocationMechanism, initialCapacity, instantiator);

        return instantiateWithCapacityExponent(allocationType, initialCapacity, instantiator, null, null, (a, e, i, p2, p3) -> i.apply(a, e));
    }

    protected static <T, U> T instantiateWithCapacityExponent(AllocationType allocationType, AllocationMechanism expectedAllocationMechanism, int initialCapacity,
            IntFunction<U> createElements, IWithCapacityExponentInstantiator<T, IntFunction<U>> instantiator) {

        checkInstantiateParameters(allocationType, expectedAllocationMechanism, initialCapacity, instantiator);

        return instantiateWithCapacityExponent(allocationType, initialCapacity, instantiator, createElements, null, (a, e, i, c, p3) -> i.instantiate(a, e, c));
    }

    protected static <T, U, V> T instantiateWithCapacityExponent(AllocationType allocationType, AllocationMechanism expectedAllocationMechanism, int initialCapacity,
            IntFunction<U> createElements1, IntFunction<V> createElements2, IWithCapacityExponentInstantiator2<T, IntFunction<U>, IntFunction<V>> instantiator) {

        checkInstantiateParameters(allocationType, expectedAllocationMechanism, initialCapacity, instantiator);

        return instantiateWithCapacityExponent(allocationType, initialCapacity, instantiator, createElements1, createElements2, (a, e, i, c1, c2) -> i.instantiate(a, e, c1, c2));
    }

    private static void checkInstantiateParameters(AllocationType allocationType, AllocationMechanism expectedAllocationMechanism, int initialCapacity, Object instantiator) {

        AllocationType.checkIsAllocationMechanism(allocationType, expectedAllocationMechanism);
        Checks.isIntInitialCapacityAtOrAboveZero(initialCapacity);
        Objects.requireNonNull(instantiator);
    }

    private static <T, P1, P2, P3> T instantiateWithCapacityExponent(AllocationType allocationType, int initialCapacity, P1 parameter1, P2 parameter2, P3 parameter3,
            WithCapacityExponentInstantiator3<T, P1, P2, P3> instantiator) {

        return instantiator.instantiate(allocationType, CapacityExponents.computeIntCapacityExponentForAtOrAboveZero(initialCapacity), parameter1, parameter2, parameter3);
    }

    protected abstract void rehashWithCapacity(HASHED hashed, HASHED newHashed, int newCapacity);

    BaseIntCapacityHashed(AllocationType allocationType, int initialCapacity, float loadFactor, IntFunction<HASHED> createHashed, Consumer<HASHED> clearHashed,
            Supplier<HASHED> recreateHashed) {
        super(allocationType, initialCapacity, loadFactor, CapacityMax.INT, c -> createHashed.apply(intCapacity(c)), clearHashed, recreateHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacity", initialCapacity).add("loadFactor", loadFactor).add("createHashed", createHashed)
                    .add("clearHashed", clearHashed).add("recreateHashed", recreateHashed));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntCapacityHashed(AllocationType allocationType, BaseIntCapacityHashed<HASHED, ?, ?, ?> toInitializeFrom) {
        super(allocationType, toInitializeFrom);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toInitializeFrom", toInitializeFrom));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntCapacityHashed(AllocationType allocationType, BaseIntCapacityHashed<HASHED, ?, ?, ?> toCopy, Function<HASHED, HASHED> copyHashed) {
        super(allocationType, toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final int getHashedCapacity() {

        return getIntCapacity();
    }

    protected final int increaseCapacityAndRehash() {

        if (DEBUG) {

            enter();
        }

        final int result = increaseCapacityAndRehashReturnIntCapacity(this, (h, n, c, i) -> i.rehashWithCapacity(h, n, intCapacity(c)));

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    protected final void checkCapacityForOneMoreElement() {

        if (DEBUG) {

            enter();
        }

        checkCapacity(1);

        if (DEBUG) {

            exit();
        }
    }

    @Deprecated // common for long capacity?
    protected final void checkCapacity(int numAdditionalElements) {

        if (DEBUG) {

            enter(b -> b.add("numAdditionalElements", numAdditionalElements));
        }

        checkCapacity(numAdditionalElements, this, (h, n, c, i) -> i.rehashWithCapacity(h, n, intCapacity(c)));

        if (DEBUG) {

            exit();
        }
    }

    protected final int getMakeFromElementsNumElements() {

        return getHashedCapacity();
    }
}
