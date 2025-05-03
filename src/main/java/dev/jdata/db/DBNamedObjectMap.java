package dev.jdata.db;

import java.util.Objects;
import java.util.function.IntFunction;

import org.jutils.io.strings.StringRef;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.maps.MutableLongToObjectMaxDistanceNonBucketMap;
import dev.jdata.db.utils.allocators.IFreeable;
import dev.jdata.db.utils.allocators.ILongToObjectMaxDistanceMapAllocator;
import dev.jdata.db.utils.checks.Checks;

public abstract class DBNamedObjectMap<T extends DBNamedObject, M extends DBNamedObjectMap<T, M>> implements IElements, IFreeable<ILongToObjectMaxDistanceMapAllocator<T>> {

    private final MutableLongToObjectMaxDistanceNonBucketMap<T> map;

    public abstract M makeCopy();

    @Deprecated
    private DBNamedObjectMap(int initialCapacity, IntFunction<T[]> createValuesArray, ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {

        Checks.isInitialCapacity(initialCapacity);
        Objects.requireNonNull(createValuesArray);
        Objects.requireNonNull(longToObjectMapAllocator);

        this.map = longToObjectMapAllocator.allocateLongToObjectMap(CapacityExponents.computeCapacityExponent(initialCapacity), createValuesArray);
    }

    protected DBNamedObjectMap() {

        this.map = null;
    }

    protected DBNamedObjectMap(IIndexList<T> namedObjects, IntFunction<T[]> createValuesArray, ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {

        Objects.requireNonNull(namedObjects);
        Objects.requireNonNull(createValuesArray);
        Objects.requireNonNull(longToObjectMapAllocator);

        final long numElements = namedObjects.getNumElements();

        this.map = longToObjectMapAllocator.allocateLongToObjectMap(CapacityExponents.computeCapacityExponent(numElements), createValuesArray);

        for (int i = 0; i < numElements; ++ i) {

            final T namedObject = namedObjects.get(i);

            put(namedObject.getHashName(), namedObject);
        }
    }

    protected DBNamedObjectMap(M toCopy, ILongToObjectMaxDistanceMapAllocator<T> longToObjectAllocator) {

        Objects.requireNonNull(toCopy);
        Objects.requireNonNull(longToObjectAllocator);

        this.map = longToObjectAllocator.copyLongToObjectMap(((DBNamedObjectMap<T, ?>)toCopy).map);
    }

    @Override
    public final boolean isEmpty() {

        return map.isEmpty();
    }

    @Override
    public final long getNumElements() {

        return map.getNumElements();
    }

    @Override
    public final void free(ILongToObjectMaxDistanceMapAllocator<T> longToObjectAllocator) {

        Objects.requireNonNull(longToObjectAllocator);

        longToObjectAllocator.freeLongToObjectMap(map);
    }

    private void put(long schemaName, T schemaObject) {

        StringRef.checkIsString(schemaName);
        Objects.requireNonNull(schemaObject);

        if (map.containsKey(schemaName)) {

            throw new IllegalStateException();
        }

        map.put(schemaName, schemaObject);
    }

    protected final boolean containsNamedObject(long name) {

        StringRef.checkIsString(name);

        return map.containsKey(name);
    }

    protected final T getNamedObject(long name) {

        StringRef.checkIsString(name);

        return map.get(name);
    }

    @Override
    public final String toString() {

        return getClass().getSimpleName() + " [map=" + map + "]";
    }
}
