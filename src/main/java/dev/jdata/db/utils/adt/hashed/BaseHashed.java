package dev.jdata.db.utils.adt.hashed;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.BaseNumElements;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseHashed<T, U, V> extends BaseNumElements<T, U, V> implements IOnlyElementsView, PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_HASHED;

    private final Consumer<T> clearHashed;
    private final Supplier<T> recreateHashed;

    private T hashed;

    BaseHashed(AllocationType allocationType, Supplier<T> createHashed, Consumer<T> clearHashed, Supplier<T> recreateHashed) {
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

    BaseHashed(AllocationType allocationType, BaseHashed<T, U, V> toCopy, Function<T, T> copyHashed) {
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

    protected final void clearHashed(T hashed) {

        Objects.requireNonNull(hashed);

        if (DEBUG) {

            enter(b -> b.add("hashed", hashed));
        }

        clearHashed.accept(hashed);

        if (DEBUG) {

            exit();
        }
    }

    protected final void replaceAndClearHashed(T hashed) {

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

    protected final T getHashed() {
        return hashed;
    }

    @FunctionalInterface
    interface Rehasher<T, P> {

        T rehash(T hashed, long newCapacity, P parameter);
    }

    final <P> void rehash(long newCapacity, P parameter, Rehasher<T, P> rehasher) {

        Checks.isIntOrLongCapacity(newCapacity);
        Objects.requireNonNull(rehasher);

        this.hashed = rehasher.rehash(hashed, newCapacity, parameter);
    }
}
