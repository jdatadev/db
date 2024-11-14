package dev.jdata.db.utils.adt.maps;

import java.util.LinkedHashMap;

public final class OrderedIntCountMap extends BaseIntCountMap<LinkedHashMap<Integer,Integer>, OrderedCountMap<Integer>> {

    public OrderedIntCountMap(int initialCapacity) {
        super(new OrderedCountMap<>(initialCapacity));
    }
}
