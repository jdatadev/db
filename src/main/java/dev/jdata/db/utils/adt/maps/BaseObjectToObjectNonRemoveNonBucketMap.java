package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.IntFunction;

abstract class BaseObjectToObjectNonRemoveNonBucketMap<K, V> extends BaseObjectToObjectNonContainsKeyNonBucketMap<K, V> {

    BaseObjectToObjectNonRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createKeysArray, createValuesArray);
    }

    BaseObjectToObjectNonRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeysArray, createValuesArray);
    }

    BaseObjectToObjectNonRemoveNonBucketMap(AllocationType allocationType, BaseObjectToObjectNonRemoveNonBucketMap<K, V> toCopy, BiConsumer<V[], V[]> copyValuesContent) {
        super(allocationType, toCopy, copyValuesContent);
    }
}
