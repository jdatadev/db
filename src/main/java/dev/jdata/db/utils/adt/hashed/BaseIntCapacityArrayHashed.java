package dev.jdata.db.utils.adt.hashed;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;

abstract class BaseIntCapacityArrayHashed<HASHED, CREATE_ELEMENTS, MAKE_ELEMENTS_FROM, INIT_FROM_VALUES>

        extends BaseIntCapacityHashed<HASHED, CREATE_ELEMENTS, MAKE_ELEMENTS_FROM, INIT_FROM_VALUES> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_CAPACITY_ARRAY_HASHED;

    BaseIntCapacityArrayHashed(AllocationType allocationType, int initialCapacity, float loadFactor, int recreateCapacity, IntFunction<HASHED> createHashed,
            Consumer<HASHED> clearHashed, IntFunction<HASHED> recreateHashed) {
        super(allocationType, initialCapacity, loadFactor, createHashed, clearHashed, () -> recreateHashed.apply(recreateCapacity));

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacity", initialCapacity).add("loadFactor", loadFactor).add("recreateCapacity", recreateCapacity)
                    .add("createHashed", createHashed).add("clearHashed", clearHashed));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntCapacityArrayHashed(AllocationType allocationType, BaseIntCapacityArrayHashed<HASHED, ?, ?, ?> toInitializeFrom) {
        super(allocationType, toInitializeFrom);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toInitializeFrom", toInitializeFrom));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntCapacityArrayHashed(AllocationType allocationType, BaseIntCapacityArrayHashed<HASHED, ?, ?, ?> toCopy, Function<HASHED, HASHED> copyHashed) {
        super(allocationType, toCopy, copyHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed));
        }

        if (DEBUG) {

            exit();
        }
    }
}
