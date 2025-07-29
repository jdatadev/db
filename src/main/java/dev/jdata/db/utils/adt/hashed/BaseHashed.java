package dev.jdata.db.utils.adt.hashed;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.BaseNumElements;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseHashed<T> extends BaseNumElements implements IElements, PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_HASHED;

    private final Consumer<T> clearHashed;

    private T hashed;

    BaseHashed(Supplier<T> createHashed, Consumer<T> clearHashed) {

        if (DEBUG) {

            enter(b -> b.add("createHashed", createHashed).add("clearHashed", clearHashed));
        }

        Objects.requireNonNull(createHashed);
        Objects.requireNonNull(clearHashed);

        this.clearHashed = clearHashed;

        this.hashed = createHashed.get();

        clearHashed.accept(hashed);

        if (DEBUG) {

            exit();
        }
    }

    BaseHashed(BaseHashed<T> toCopy, Function<T, T> copyHashed) {
        super(toCopy);

        Objects.requireNonNull(copyHashed);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        if (copyHashed == toCopy.hashed) {

            throw new IllegalArgumentException();
        }

        this.clearHashed = toCopy.clearHashed;

        this.hashed = copyHashed.apply(toCopy.hashed);

        if (DEBUG) {

            exit();
        }
    }

    protected final void clearHashed() {

        if (DEBUG) {

            enter();
        }

        super.clearNumElements();

        clearHashed(hashed);

        if (DEBUG) {

            exit();
        }
    }

    protected final void clearHashed(T hashed) {

        if (DEBUG) {

            enter(b -> b.add("hashed", hashed));
        }

        clearHashed.accept(hashed);

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

        Checks.isCapacity(newCapacity);
        Objects.requireNonNull(rehasher);

        this.hashed = rehasher.rehash(hashed, newCapacity, parameter);
    }
}
