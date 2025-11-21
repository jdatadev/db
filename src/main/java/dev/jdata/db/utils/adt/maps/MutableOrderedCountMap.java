package dev.jdata.db.utils.adt.maps;

import java.util.LinkedHashMap;

abstract class MutableOrderedCountMap<T> extends BaseMutableCountMap<T, LinkedHashMap<T,Integer>> implements IMutableOrderedCountMap<T> {

    MutableOrderedCountMap(int initialCapacity) {
        super(new LinkedHashMap<>(initialCapacity));
    }
}
