package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.checks.Checks;

class KeyMapChecks {

    static <KEYS, VALUES> void checkCreateParameters(int initialCapacity, IntFunction<VALUES> createValuesArray) {

        checkCreateParameters(initialCapacity);
        Objects.requireNonNull(createValuesArray);
    }

    static <VALUES> void checkCreateParameters(int initialCapacity, int capacityExponentIncrease, float loadFactor, IntFunction<VALUES> createValuesArray) {

        checkCreateParameters(initialCapacity, capacityExponentIncrease, loadFactor);
        Objects.requireNonNull(createValuesArray);
    }

    static <KEYS, VALUES> void checkCreateParameters(int initialCapacity, IntFunction<KEYS> createKeysArray, IntFunction<VALUES> createValuesArray) {

        checkCreateParameters(initialCapacity);
        Objects.requireNonNull(createKeysArray);
        Objects.requireNonNull(createValuesArray);
    }

    static <KEYS, VALUES> void checkCreateParameters(int initialCapacity, int capacityExponentIncrease, float loadFactor, IntFunction<KEYS> createKeysArray,
            IntFunction<VALUES> createValuesArray) {

        checkCreateParameters(initialCapacity, capacityExponentIncrease, loadFactor);
        Objects.requireNonNull(createKeysArray);
        Objects.requireNonNull(createValuesArray);
    }

    private static void checkCreateParameters(int initialCapacity, int capacityExponentIncrease, float loadFactor) {

        checkCreateParameters(initialCapacity);

        Checks.isIntCapacityExponentIncrease(capacityExponentIncrease);
        Checks.isLoadFactor(loadFactor);
    }

    private static <KEYS, VALUES> void checkCreateParameters(int initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);
    }
}
