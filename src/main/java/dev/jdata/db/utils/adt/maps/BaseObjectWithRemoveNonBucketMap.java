package dev.jdata.db.utils.adt.maps;

import java.util.function.BiConsumer;
import java.util.function.IntFunction;

abstract class BaseObjectWithRemoveNonBucketMap<K, V> extends BaseObjectToObjectNonContainsKeyNonBucketMap<K, V> {

    BaseObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<K[]> createKeyArray, IntFunction<V[]> createValues) {
        super(allocationType, initialCapacityExponent, createKeyArray, createValues);
    }

    BaseObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<K[]> createKeyArray,
            IntFunction<V[]> createValues) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeyArray, createValues);
    }

    BaseObjectWithRemoveNonBucketMap(AllocationType allocationType, BaseObjectWithRemoveNonBucketMap<K, V> toCopy, BiConsumer<V[], V[]> copyValuesContent) {
        super(allocationType, toCopy, copyValuesContent);
    }
}
