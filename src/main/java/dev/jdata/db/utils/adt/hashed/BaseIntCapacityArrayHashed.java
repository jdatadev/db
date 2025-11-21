package dev.jdata.db.utils.adt.hashed;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;

abstract class BaseIntCapacityArrayHashed<T, U, V> extends BaseIntCapacityHashed<T, U, V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_CAPACITY_ARRAY_HASHED;

    BaseIntCapacityArrayHashed(AllocationType allocationType, int initialCapacity, float loadFactor, int recreateCapacity, IntFunction<T> createHashed, Consumer<T> clearHashed,
            IntFunction<T> recreateHashed) {
        super(allocationType, initialCapacity, loadFactor, () -> createHashed.apply(initialCapacity), clearHashed, () -> recreateHashed.apply(recreateCapacity));

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacity", initialCapacity).add("loadFactor", loadFactor).add("recreateCapacity", recreateCapacity)
                    .add("createHashed", createHashed).add("clearHashed", clearHashed));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntCapacityArrayHashed(AllocationType allocationType, BaseIntCapacityArrayHashed<T, U, V> toCopy, Function<T, T> copyHashed) {
        super(allocationType, toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        if (DEBUG) {

            exit();
        }
    }
}
