package dev.jdata.db.utils.adt.maps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MapOfList<K, V> extends MapOfCollection<K, V, List<V>, Map<K, List<V>>> {

    public MapOfList(int initialCapacity) {
        super(initialCapacity, HashMap::new);
    }

    public MapOfList(int initialCapacity, int collectionInitialCapacity) {
        super(initialCapacity, collectionInitialCapacity, HashMap::new);
    }

    @Override
    protected List<V> createCollection(int initialCapacity) {

        return new ArrayList<>(initialCapacity);
    }

    @Override
    List<V> createUnmodifible(List<V> collection) {

        return Collections.unmodifiableList(collection);
    }
}
