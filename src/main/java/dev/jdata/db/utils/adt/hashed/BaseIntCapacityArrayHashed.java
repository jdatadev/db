package dev.jdata.db.utils.adt.hashed;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseIntCapacityArrayHashed<T> extends BaseIntCapacityHashed<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_ARRAY_HASHED;

    private static final Class<?> debugClass = BaseIntCapacityArrayHashed.class;

    private final IntFunction<T> createHashed;

    BaseIntCapacityArrayHashed(int initialCapacity, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacity, loadFactor, () -> createHashed.apply(initialCapacity), clearHashed);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("initialCapacity", initialCapacity).add("loadFactor=", loadFactor).add("createHashed", createHashed));
        }

        this.createHashed = Objects.requireNonNull(createHashed);

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }

    BaseIntCapacityArrayHashed(BaseIntCapacityArrayHashed<T> toCopy, BiConsumer<T, T> copyHashedContent) {
        super(toCopy, copyHashed(toCopy, copyHashedContent));

        this.createHashed = toCopy.createHashed;
    }

    private static <T> T copyHashed(BaseIntCapacityArrayHashed<T> toCopy, BiConsumer<T, T> copyHashedContent) {

        final T array = toCopy.createHashed(toCopy.getCapacity());

        copyHashedContent.accept(toCopy.getHashed(), array);

        return array;
    }

    protected final T createHashedNonCleared(int capacity) {

        Checks.isCapacity(capacity);

        return createHashed.apply(capacity);
    }

    protected final T createHashed(int capacity) {

        Checks.isInitialCapacity(capacity);

        if (DEBUG) {

            enter(b -> b.add("capacity", capacity));
        }

        final T result = createHashed.apply(capacity);

        clearHashed(result);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
