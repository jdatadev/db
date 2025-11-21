package dev.jdata.db.utils.adt.maps;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntFunction;

import dev.jdata.db.utils.checks.Checks;

public abstract class MapOfCollection<K, V, C extends Collection<V>, M extends Map<K, C>> implements IMapOfCollection<K, V, C> {

    private static final int DEFAULT_COLLECTION_INITIAL_CAPACITY = 1;

    private final M map;
    private final int collectionInitialCapacity;

    private int numElements;

    protected abstract C createCollection(int initialCapacity);

    abstract C createUnmodifible(C collection);

    protected MapOfCollection(int initialCapacity, IntFunction<M> createMap) {
        this(initialCapacity, DEFAULT_COLLECTION_INITIAL_CAPACITY, createMap);
    }

    protected MapOfCollection(int initialCapacity, int collectionInitialCapacity, IntFunction<M> createMap) {

        Checks.isIntInitialCapacity(initialCapacity);
        Checks.isIntInitialCapacity(collectionInitialCapacity);
        Objects.requireNonNull(createMap);

        this.map = createMap.apply(initialCapacity);
        this.collectionInitialCapacity = collectionInitialCapacity;

        this.numElements = 0;
    }

    @Override
    public final void add(K key, V value) {

        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        C collection = map.get(key);

        if (collection == null) {

            collection = createCollection(collectionInitialCapacity);

            map.put(key, collection);
        }

        collection.add(value);

        ++ this.numElements;
    }

    @Override
    public final Set<K> unmdifiableKeySet() {

        return numElements == 0 ? Collections.emptySet() : Collections.unmodifiableSet(map.keySet());
    }

    @Override
    public final C getUnmodifiable(K key) {

        final C collection = map.get(key);

        return collection != null ? createUnmodifible(collection) : null;
    }

    @Override
    public final boolean isEmpty() {

        return numElements == 0;
    }

    @Override
    public final long getNumElements() {
        return numElements;
    }

    @Override
    public final void clear() {

        map.clear();

        this.numElements = 0;
    }

    @Override
    public final int getNumKeys() {

        return map.size();
    }
}
