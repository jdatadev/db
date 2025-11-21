package dev.jdata.db.utils.adt.maps;

import java.util.LinkedHashMap;

abstract class MutableOrderedIntCountMap extends BaseMutableIntCountMap<LinkedHashMap<Integer,Integer>, MutableOrderedCountMap<Integer>> implements IMutableOrderedIntCountMap {

    MutableOrderedIntCountMap(int initialCapacity) {
        super(new MutableOrderedCountMap<>(initialCapacity));
    }
}
