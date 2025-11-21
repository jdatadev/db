package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.checks.Checks;

interface IMutableOrderedCountMap<T> extends IMutableCountMap<T> {

heap
    public static <T> IMutableOrderedCountMap<T> create(int initialCapacity) {

        Checks.isInitialCapacity(initialCapacity);

        return new MutableOrderedCountMap<>(initialCapacity);
    }
}
