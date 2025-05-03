package dev.jdata.db.utils.adt.maps;

@Deprecated // currently not in use
public final class AppendOnlyLinkedIntToIntMap {

    private final MutableIntToIntMaxDistanceNonBucketMap map;
    private int[] list;

    public AppendOnlyLinkedIntToIntMap() {

        throw new UnsupportedOperationException();
    }

    public void put(int key, int value) {

        if (map.containsKey(key)) {

            throw new IllegalStateException();
        }
    }
}
