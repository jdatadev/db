package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.marker.IHeapTypeMarker;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableOrderedCountMap<T> extends IMutableOrderedCountMap<T>, IHeapTypeMarker {

    public static <T> IHeapMutableOrderedCountMap<T> create(int initialCapacity) {

        Checks.isLongInitialCapacityAtOrAboveZero(initialCapacity);

        return new HeapMutableOrderedCountMap<>(initialCapacity);
    }
}
