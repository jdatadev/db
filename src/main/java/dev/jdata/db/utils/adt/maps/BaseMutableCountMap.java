package dev.jdata.db.utils.adt.maps;

import java.util.Map;
import java.util.Objects;

abstract class BaseMutableCountMap<T, M extends Map<T, Integer>> implements IBaseMutableCountMap<T> {

    private final M map;

    BaseMutableCountMap(M map) {

        this.map = Objects.requireNonNull(map);
    }

    @Override
    public final boolean isEmpty() {

        return map.isEmpty();
    }

    @Override
    public final long getNumElements() {

        return map.size();
    }

    @Override
    public final void clear() {

        map.clear();
    }

    @Override
    public final long getCapacity() {

        return getCapacity();
    }

    @Override
    public final void add(T key) {

        Objects.requireNonNull(key);

        final Integer count = map.get(key);

        final int updatedCount = count != null ? count + 1 : 1;

        map.put(key, updatedCount);
    }

    @Override
    public final long getCount(T key) {

        Objects.requireNonNull(key);

        final Integer count = map.get(key);

        return count != null ? count : 0;
    }

    @Override
    public final <E extends Exception> void forEach(IObjectForEachCountMapElement<T, E> forEachElement) throws E {

        Objects.requireNonNull(forEachElement);

        for (Map.Entry<T, Integer> entry : map.entrySet()) {

            forEachElement.each(entry.getKey(), entry.getValue());
        }
    }
}
