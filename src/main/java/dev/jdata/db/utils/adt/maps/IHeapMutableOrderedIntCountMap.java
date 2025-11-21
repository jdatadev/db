package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableOrderedIntCountMap extends IMutableOrderedIntCountMap {

    public static IHeapMutableOrderedIntCountMap create(int initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return new HeapMutableOrderedIntCountMap(initialCapacity);
    }
}
