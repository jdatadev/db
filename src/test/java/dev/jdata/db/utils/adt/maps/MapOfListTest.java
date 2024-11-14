package dev.jdata.db.utils.adt.maps;

import java.util.List;
import java.util.Map;

public final class MapOfListTest extends BaseMapOfCollectionTest<List<String>, Map<Integer, List<String>>, MapOfList<Integer, String>> {

    @Override
    MapOfList<Integer, String> creatMapOfCollection(int initialCapacity) {

        return new MapOfList<>(initialCapacity);
    }
}
