package dev.jdata.db.utils.adt.hashed;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;

abstract class BaseIntCapacityArrayHashed<T> extends BaseIntCapacityHashed<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_CAPACITY_ARRAY_HASHED;

    private final IntFunction<T> createHashed;

    BaseIntCapacityArrayHashed(int initialCapacity, float loadFactor, IntFunction<T> createHashed, Consumer<T> clearHashed) {
        super(initialCapacity, loadFactor, () -> createHashed.apply(initialCapacity), clearHashed);

        if (DEBUG) {

            enter(b -> b.add("initialCapacity", initialCapacity).add("loadFactor", loadFactor).add("createHashed", createHashed).add("clearHashed", clearHashed));
        }

        this.createHashed = Objects.requireNonNull(createHashed);

        if (DEBUG) {

            exit();
        }
    }

    BaseIntCapacityArrayHashed(BaseIntCapacityArrayHashed<T> toCopy, Function<T, T> copyHashed) {
        super(toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        this.createHashed = toCopy.createHashed;

        if (DEBUG) {

            exit();
        }
    }
}
