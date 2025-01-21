package dev.jdata.db.utils.adt.maps;

abstract class BaseIntToIntegerOrObjectTest<V, M extends IntKeyMap> extends BaseIntegerToIntegerOrObjectTest<int[], V, M> {

    @Override
    final int[] createKeysArray(int length) {

        return new int[length];
    }

    @Override
    final int getKey(int[] keys, int index) {

        return keys[index];
    }

    @Override
    final boolean contains(M map, int key) {

        return map.containsKey(key);
    }

    @Override
    final int[] getKeys(M map) {

        return map.keys();
    }

    @Override
    final boolean remove(M map, int key) {

        return map.remove(key);
    }
}
