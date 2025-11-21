package dev.jdata.db.utils.adt.maps;

final class HeapMutableOrderedIntCountMap extends MutableOrderedIntCountMap implements IHeapMutableOrderedIntCountMap {

    HeapMutableOrderedIntCountMap(int initialCapacity) {
        super(initialCapacity);
    }
}
