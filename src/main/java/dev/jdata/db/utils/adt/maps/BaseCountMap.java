package dev.jdata.db.utils.adt.maps;

import java.util.Map;
import java.util.Objects;

import dev.jdata.db.utils.adt.elements.IMutableElements;

public abstract class BaseCountMap<T, M extends Map<T, Integer>> implements IMutableElements {

    @FunctionalInterface
    public interface ForEachElement<T, E extends Exception> {

        void each(T value, int count) throws E;
    }

    private final M map;

    BaseCountMap(M map) {

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

    public final void add(T key) {

        Objects.requireNonNull(key);

        final Integer count = map.get(key);

        final int updatedCount = count != null ? count + 1 : 1;

        map.put(key, updatedCount);
    }

    public final int getCount(T key) {

        Objects.requireNonNull(key);

        final Integer count = map.get(key);

        return count != null ? count : 0;
    }

    public final <E extends Exception> void forEach(ForEachElement<T, E> forEachElement) throws E {

        Objects.requireNonNull(forEachElement);

        for (Map.Entry<T, Integer> entry : map.entrySet()) {

            forEachElement.each(entry.getKey(), entry.getValue());
        }
    }
}
