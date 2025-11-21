package dev.jdata.db.utils.adt.maps;

final class HeapMutableOrderedCountMap<T> extends MutableOrderedCountMap<T> implements IHeapMutableOrderedCountMap<T> {

    HeapMutableOrderedCountMap(int initialCapacity) {
        super(initialCapacity);
    }
}
