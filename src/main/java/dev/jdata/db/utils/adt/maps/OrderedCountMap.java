package dev.jdata.db.utils.adt.maps;

import java.util.LinkedHashMap;

public final class OrderedCountMap<T> extends BaseCountMap<T, LinkedHashMap<T,Integer>> {

    public OrderedCountMap(int initialCapacity) {
        super(new LinkedHashMap<>(initialCapacity));
    }
}
