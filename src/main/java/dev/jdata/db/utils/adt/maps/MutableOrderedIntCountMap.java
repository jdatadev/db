package dev.jdata.db.utils.adt.maps;

import java.util.LinkedHashMap;

final class MutableOrderedIntCountMap extends BaseMutableIntCountMap<LinkedHashMap<Integer,Integer>, MutableOrderedCountMap<Integer>> {

    MutableOrderedIntCountMap(int initialCapacity) {
        super(new MutableOrderedCountMap<>(initialCapacity));
    }
}
