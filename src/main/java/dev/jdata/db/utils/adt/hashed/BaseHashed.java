package dev.jdata.db.utils.adt.hashed;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.BaseNumElements;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseHashed<HASHED, CREATE_ELEMENTS, MAKE_ELEMENTS_FROM, INIT_FROM_VALUES> extends BaseNumElements<CREATE_ELEMENTS, MAKE_ELEMENTS_FROM, INIT_FROM_VALUES> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_HASHED;

    private final Consumer<HASHED> clearHashed;
    private final Supplier<HASHED> recreateHashed;

    private HASHED hashed;

    BaseHashed(AllocationType allocationType, Supplier<HASHED> createHashed, Consumer<HASHED> clearHashed, Supplier<HASHED> recreateHashed) {
        super(allocationType);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("createHashed", createHashed).add("clearHashed", clearHashed).add("recreateHashed", recreateHashed));
        }

        Objects.requireNonNull(createHashed);
        Objects.requireNonNull(clearHashed);

        this.clearHashed = clearHashed;
        this.recreateHashed = recreateHashed;

        this.hashed = createHashed.get();

        clearHashed.accept(hashed);

        if (DEBUG) {

            exit();
        }
    }

    BaseHashed(AllocationType allocationType, BaseHashed<HASHED, ?, ?, ?> toInitializeFrom) {
        super(allocationType, toInitializeFrom);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toInitializeFrom", toInitializeFrom));
        }

        this.hashed = Objects.requireNonNull(hashed);

        this.clearHashed = null;
        this.recreateHashed = null;

        if (DEBUG) {

            exit();
        }
    }

    BaseHashed(AllocationType allocationType, BaseHashed<HASHED, ?, ?, ?> toCopy, Function<HASHED, HASHED> copyHashed) {
        super(allocationType, toCopy);

        Objects.requireNonNull(copyHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        if (copyHashed == toCopy.hashed) {

            throw new IllegalArgumentException();
        }

        this.clearHashed = toCopy.clearHashed;
        this.recreateHashed = toCopy.recreateHashed;

        this.hashed = copyHashed.apply(toCopy.hashed);

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected void recreateElements() {

        this.hashed = recreateHashed.get();
    }

    @Override
    protected void resetToNull() {

        this.hashed = null;
    }

    protected final void clearHashed() {

        if (DEBUG) {

            enter();
        }

        clearNumElements();

        clearHashed(hashed);

        if (DEBUG) {

            exit();
        }
    }

    private void clearHashed(HASHED hashed) {

        Objects.requireNonNull(hashed);

        if (DEBUG) {

            enter(b -> b.add("hashed", hashed));
        }

        clearHashed.accept(hashed);

        if (DEBUG) {

            exit();
        }
    }

    protected final void replaceAndClearHashed(HASHED hashed) {

        Objects.requireNonNull(hashed);

        if (DEBUG) {

            enter(b -> b.add("hashed", hashed));
        }

        this.hashed = hashed;

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }

    protected final HASHED getHashed() {
        return hashed;
    }

    @FunctionalInterface
    interface Rehasher<T, P> {

        void rehash(T hashed, T newHashed, long newCapacity, P parameter);
    }

    final <P> void rehash(HASHED newHashed, long newCapacity, P parameter, Rehasher<HASHED, P> rehasher) {

        Objects.requireNonNull(newHashed);
        Checks.isIntOrLongCapacityAboveZero(newCapacity);
        Objects.requireNonNull(rehasher);

        clearHashed(newHashed);

        rehasher.rehash(hashed, newHashed, newCapacity, parameter);

        this.hashed = newHashed;
    }
}
