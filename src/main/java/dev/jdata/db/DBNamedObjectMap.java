package dev.jdata.db;

import java.util.Objects;
import java.util.function.IntFunction;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.IContains;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.maps.ILongToObjectCommonMapGetters;
import dev.jdata.db.utils.adt.maps.ILongToObjectDynamicMapCommon;
import dev.jdata.db.utils.adt.maps.IObjectToObjectDynamicMapCommon;
import dev.jdata.db.utils.adt.maps.MutableLongToObjectMaxDistanceNonBucketMap;
import dev.jdata.db.utils.adt.maps.MutableObjectMaxDistanceNonBucketMap;
import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.IFreeable;
import dev.jdata.db.utils.allocators.ILongToObjectMaxDistanceMapAllocator;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;

public abstract class DBNamedObjectMap<T extends DBNamedObject, M extends DBNamedObjectMap<T, M>>

        extends Allocatable
        implements IElements, IFreeable<ILongToObjectMaxDistanceMapAllocator<T>> {

    private static final boolean ASSERT = AssertionContants.ASSERT_DB_NAMED_OBJECT_MAP;

    protected static final boolean EQUALS_NAME_CASE_SENSITIVE = true;

    private final IntFunction<T[]> createValuesArray;

    private final MutableLongToObjectMaxDistanceNonBucketMap<T> map;

    public abstract M makeCopy();

    @Deprecated
    private DBNamedObjectMap(AllocationType allocationType, int initialCapacity, IntFunction<T[]> createValuesArray,
            ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {
        super(allocationType);

        Checks.isInitialCapacity(initialCapacity);
        Objects.requireNonNull(createValuesArray);
        Objects.requireNonNull(longToObjectMapAllocator);

        this.createValuesArray = Objects.requireNonNull(createValuesArray);

        this.map = longToObjectMapAllocator.allocateLongToObjectMap(CapacityExponents.computeCapacityExponent(initialCapacity), createValuesArray);
    }

    protected DBNamedObjectMap(AllocationType allocationType) {
        super(allocationType);

        this.createValuesArray = null;
        this.map = null;
    }

    protected DBNamedObjectMap(AllocationType allocationType, IIndexList<T> namedObjects, IntFunction<T[]> createValuesArray,
            ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {
        super(allocationType);

        Objects.requireNonNull(namedObjects);
        Objects.requireNonNull(createValuesArray);
        Objects.requireNonNull(longToObjectMapAllocator);

        this.createValuesArray = Objects.requireNonNull(createValuesArray);

        final long numElements = namedObjects.getNumElements();

        this.map = longToObjectMapAllocator.allocateLongToObjectMap(CapacityExponents.computeCapacityExponent(numElements), createValuesArray);

        for (int i = 0; i < numElements; ++ i) {

            final T namedObject = namedObjects.get(i);

            put(namedObject.getHashName(), namedObject);
        }
    }

    protected DBNamedObjectMap(AllocationType allocationType, M toCopy, ILongToObjectMaxDistanceMapAllocator<T> longToObjectAllocator) {
        super(allocationType);

        Objects.requireNonNull(toCopy);
        Objects.requireNonNull(longToObjectAllocator);

        final DBNamedObjectMap<T, M> toCopyNamedObjectMap = toCopy;

        this.createValuesArray = toCopyNamedObjectMap.createValuesArray;

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

    private StringResolver scratchThisStringResolver;

    public final boolean equalsMap(StringResolver thisStringResolver, DBNamedObjectMap<T, M> other, StringResolver otherStringResolver) {

        Objects.requireNonNull(thisStringResolver);
        Objects.requireNonNull(other);
        Objects.requireNonNull(otherStringResolver);

        final boolean result;

        final boolean mapNullOrEmpty = IContains.isNullOrEmpty(map);
        final boolean otherNullOrEmpty = IContains.isNullOrEmpty(other.map);

        if (this == other) {

            result = true;
        }
        else if (mapNullOrEmpty && otherNullOrEmpty) {

            result = true;
        }
        else if (mapNullOrEmpty || otherNullOrEmpty) {

            result = false;
        }
        else if (getClass() != other.getClass()) {

            result = false;
        }
        else {
            final MutableLongToObjectMaxDistanceNonBucketMap<T> thisMap = map;
            final MutableLongToObjectMaxDistanceNonBucketMap<T> otherMap = other.map;

            result = equals(thisMap, thisMap.getCapacityExponent(), thisStringResolver, otherMap, otherMap.getCapacityExponent(), otherStringResolver);
        }

        return result;
    }

    private boolean equals(ILongToObjectDynamicMapCommon<T> thisMap, int thisMapCapacityExponent, StringResolver thisStringResolver, ILongToObjectDynamicMapCommon<T> otherMap,
            int otherMapCapacityExponent, StringResolver otherStringResolver) {

        boolean result;

        final long[] thisKeys = thisMap.keys();
        final long[] otherKeys  = otherMap.keys();

        final int num = thisKeys.length;

        if (num != otherKeys.length) {

            result = false;
        }
        else {
            final IObjectToObjectDynamicMapCommon<String, T> thisStringMap = makeMapForEquals(thisMap, thisMapCapacityExponent, thisStringResolver);
            final IObjectToObjectDynamicMapCommon<String, T> otherStringMap = makeMapForEquals(otherMap, otherMapCapacityExponent, otherStringResolver);

            result = thisStringMap.equals(thisStringResolver, otherStringMap, otherStringResolver, (e1, r1, e2, r2) -> e1.equalsName(r1, e2, r2, EQUALS_NAME_CASE_SENSITIVE));
        }

        return result;
    }

    private MutableObjectMaxDistanceNonBucketMap<String, T> scratchMutableObjectMap;

    private IObjectToObjectDynamicMapCommon<String, T> makeMapForEquals(ILongToObjectCommonMapGetters<T> map, int capacityExponent, StringResolver stringResolver) {

        if (ASSERT) {

            Assertions.isNull(scratchThisStringResolver);
            Assertions.isNull(scratchMutableObjectMap);
        }

        final MutableObjectMaxDistanceNonBucketMap<String, T> result = new MutableObjectMaxDistanceNonBucketMap<>(capacityExponent, String[]::new, createValuesArray);

        this.scratchThisStringResolver = stringResolver;
        this.scratchMutableObjectMap = result;

        map.forEachKeyAndValue(this, (k, v, i) -> {

            final String stringKey = i.scratchThisStringResolver.asString(k);

            i.scratchMutableObjectMap.put(stringKey, v);
        });

        this.scratchThisStringResolver = null;
        this.scratchMutableObjectMap = null;

        return result;
    }

    @Override
    public boolean equals(Object object) {

        final boolean result;

        if (this == object) {

            result = true;
        }
        else if (object == null) {

            result = false;
        }
        else if (getClass() != object.getClass()) {

            result = false;
        }
        else {
            final DBNamedObjectMap<?, ?> other = (DBNamedObjectMap<?, ?>)object;

            result = Objects.equals(map, other.map);
        }

        return result;
    }

    @Override
    public final String toString() {

        return getClass().getSimpleName() + " [map=" + map + "]";
    }
}
