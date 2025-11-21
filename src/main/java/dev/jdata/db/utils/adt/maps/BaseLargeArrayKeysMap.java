package dev.jdata.db.utils.adt.maps;

import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.adt.hashed.BaseLongCapacityExponentArrayHashed;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLargeArrayKeysMap<KEYS extends IMutableLargeArrayMarker> extends BaseLongCapacityExponentArrayHashed<KEYS> {

    @FunctionalInterface
    interface LongMapIndexValuesEqualityTester<T, P1, P2, DELEGATE> {

        boolean areValuesEqual(T values1, long index1, P1 parameter1, T values2, long index2, P2 parameter2, DELEGATE delegate);
    }

    @FunctionalInterface
    interface ForEachKeyAndValueWithKeysAndValuesWithResult<K, V, P1, P2, DELEGATE, R> {

        R each(K keys, int keyIndex, V values, int valueIndex, P1 parameter1, P2 parameter2, DELEGATE delegate);
    }

    BaseLargeArrayKeysMap(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<KEYS> createHashed, Consumer<KEYS> clearHashed) {
        super(allocationType, initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed);
    }

    BaseLargeArrayKeysMap(AllocationType allocationType, BaseLargeArrayKeysMap<KEYS> toCopy, Function<KEYS, KEYS> copyHashed) {
        super(allocationType, toCopy, copyHashed);
    }
}
