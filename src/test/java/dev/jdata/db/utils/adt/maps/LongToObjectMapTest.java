package dev.jdata.db.utils.adt.maps;

public final class LongToObjectMapTest extends BaseLongToIntegerOrObjectTest<LongToObjectMap<String>> {

    @Override
    LongToObjectMap<String> createMap(int initialCapacity) {

        return new LongToObjectMap<>(initialCapacity, String[]::new);
    }

    @Override
    int get(LongToObjectMap<String> map, long key) {

        return Integer.parseInt(map.get(key));
    }

    @Override
    void put(LongToObjectMap<String> map, long key, int value) {

        map.put(key, String.valueOf(value));
    }
}
