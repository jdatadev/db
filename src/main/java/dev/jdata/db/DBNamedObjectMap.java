package dev.jdata.db;

import java.util.Objects;
import java.util.function.IntFunction;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.adt.elements.IObjectUnorderedOnlyElementsView;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.lists.IIndexListView;
import dev.jdata.db.utils.adt.maps.IHeapMutableDynamicMap;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMap;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.adt.maps.ILongToObjectDynamicMapView;
import dev.jdata.db.utils.adt.maps.ILongToObjectMapView;
import dev.jdata.db.utils.adt.maps.IMutableDynamicMap;
import dev.jdata.db.utils.adt.maps.IMutableLongToObjectDynamicMap;
import dev.jdata.db.utils.adt.maps.IObjectToObjectDynamicMapView;
import dev.jdata.db.utils.adt.sets.IHeapMutableLongSet;
import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.IFreeable;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;

public abstract class DBNamedObjectMap<V extends DBNamedObject, M extends DBNamedObjectMap<V, M>>

        extends Allocatable
        implements IObjectUnorderedOnlyElementsView<V>, IFreeable<IHeapMutableLongToObjectDynamicMapAllocator<V>> {

    private static final boolean ASSERT = AssertionContants.ASSERT_DB_NAMED_OBJECT_MAP;

    protected static final boolean EQUALS_NAME_CASE_SENSITIVE = true;

    private final IntFunction<V[]> createValuesArray;

    private final IHeapMutableLongToObjectDynamicMap<V> schemaObjectByName;

    public abstract M makeCopy();

    protected DBNamedObjectMap(AllocationType allocationType) {
        super(allocationType);

        this.createValuesArray = null;
        this.schemaObjectByName = null;
    }

    protected DBNamedObjectMap(AllocationType allocationType, IIndexListView<V> namedObjects, IntFunction<V[]> createValuesArray,
            IHeapMutableLongToObjectDynamicMapAllocator<V> longToObjectMapAllocator) {
        super(allocationType);

        Objects.requireNonNull(namedObjects);
        Objects.requireNonNull(createValuesArray);
        Objects.requireNonNull(longToObjectMapAllocator);

        this.createValuesArray = Objects.requireNonNull(createValuesArray);

        final long numElements = namedObjects.getNumElements();

        this.schemaObjectByName = longToObjectMapAllocator.createMutable(numElements);

        for (int i = 0; i < numElements; ++ i) {

            final V namedObject = namedObjects.get(i);

            put(namedObject.getHashName(), namedObject);
        }
    }

    protected DBNamedObjectMap(AllocationType allocationType, M toCopy, IHeapMutableLongToObjectDynamicMapAllocator<V> longToObjectAllocator) {
        super(allocationType);

        Objects.requireNonNull(toCopy);
        Objects.requireNonNull(longToObjectAllocator);

        final DBNamedObjectMap<V, M> toCopyNamedObjectMap = toCopy;

        this.createValuesArray = toCopyNamedObjectMap.createValuesArray;

        this.schemaObjectByName = longToObjectAllocator.copyLongToObjectMap(((DBNamedObjectMap<V, ?>)toCopy).schemaObjectByName);
    }

    private DBNamedObjectMap(AllocationType allocationType, int initialCapacity, IntFunction<V[]> createValuesArray,
            IHeapMutableLongToObjectDynamicMapAllocator<V> longToObjectMapAllocator) {
        super(allocationType);

        Checks.isIntInitialCapacity(initialCapacity);
        Objects.requireNonNull(createValuesArray);
        Objects.requireNonNull(longToObjectMapAllocator);

        this.createValuesArray = Objects.requireNonNull(createValuesArray);

        this.schemaObjectByName = longToObjectMapAllocator.createMutable(initialCapacity);
    }

    @Override
    public boolean isEmpty() {

        return IContainsView.isNullOrEmpty(schemaObjectByName);
    }

    @Override
    public long getNumElements() {

        return isEmpty() ? 0L : schemaObjectByName.getNumElements();
    }

    @Override
    public final <P> void toString(StringBuilder sb, P parameter, IElementsToStringAdder<V, P> consumer) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(consumer);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void free(IHeapMutableLongToObjectDynamicMapAllocator<V> longToObjectAllocator) {

        Objects.requireNonNull(longToObjectAllocator);

        longToObjectAllocator.freeMutable(schemaObjectByName);
    }

    private void put(long schemaObjectName, V schemaObject) {

        StringRef.checkIsString(schemaObjectName);
        Objects.requireNonNull(schemaObject);

        if (schemaObjectByName.containsKey(schemaObjectName)) {

            throw new IllegalStateException();
        }

        schemaObjectByName.put(schemaObjectName, schemaObject);
    }

    protected final boolean containsNamedObject(long name) {

        StringRef.checkIsString(name);

        return isEmpty() ? false : schemaObjectByName.containsKey(name);
    }

    protected final V getNamedObject(long name) {

        StringRef.checkIsString(name);

        return isEmpty() ? null : schemaObjectByName.get(name);
    }

    private StringResolver scratchThisStringResolver;

    public final boolean equalsMap(StringResolver thisStringResolver, DBNamedObjectMap<V, M> other, StringResolver otherStringResolver) {

        Objects.requireNonNull(thisStringResolver);
        Objects.requireNonNull(other);
        Objects.requireNonNull(otherStringResolver);

        final boolean result;

        final boolean mapNullOrEmpty = IContainsView.isNullOrEmpty(schemaObjectByName);
        final boolean otherNullOrEmpty = IContainsView.isNullOrEmpty(other.schemaObjectByName);

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
            final IMutableLongToObjectDynamicMap<V> thisMap = schemaObjectByName;
            final IMutableLongToObjectDynamicMap<V> otherMap = other.schemaObjectByName;

            result = equals(thisMap, CapacityExponents.computeIntCapacityExponentExact(thisMap), thisStringResolver, otherMap,
                    CapacityExponents.computeIntCapacityExponentExact(otherMap), otherStringResolver);
        }

        return result;
    }

    private boolean equals(ILongToObjectDynamicMapView<V> thisMap, int thisMapCapacityExponent, StringResolver thisStringResolver, ILongToObjectDynamicMapView<V> otherMap,
            int otherMapCapacityExponent, StringResolver otherStringResolver) {

        boolean result;

        final IHeapMutableLongSet thisKeys = IHeapMutableLongSet.create(IOnlyElementsView.intNumElements(thisMap));
        final IHeapMutableLongSet otherKeys = IHeapMutableLongSet.create(IOnlyElementsView.intNumElements(otherMap));

        final long numThisKeys = thisMap.keys(thisKeys);
        final long numOtherKeys = otherMap.keys(otherKeys);

        if (numThisKeys != numOtherKeys) {

            result = false;
        }
        else {
            final IObjectToObjectDynamicMapView<String, V> thisStringMap = makeMapForEquals(thisMap, thisMapCapacityExponent, thisStringResolver);
            final IObjectToObjectDynamicMapView<String, V> otherStringMap = makeMapForEquals(otherMap, otherMapCapacityExponent, otherStringResolver);

            result = thisStringMap.equals(thisStringResolver, otherStringMap, otherStringResolver, (e1, r1, e2, r2) -> e1.equalsName(r1, e2, r2, EQUALS_NAME_CASE_SENSITIVE));
        }

        return result;
    }

    private IMutableDynamicMap<String, V> scratchMutableObjectMap;

    private IObjectToObjectDynamicMapView<String, V> makeMapForEquals(ILongToObjectMapView<V> map, int capacityExponent, StringResolver stringResolver) {

        if (ASSERT) {

            Assertions.isNull(scratchThisStringResolver);
            Assertions.isNull(scratchMutableObjectMap);
        }

        final IMutableDynamicMap<String, V> result = IHeapMutableDynamicMap.create(capacityExponent, String[]::new, createValuesArray);

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

            result = Objects.equals(schemaObjectByName, other.schemaObjectByName);
        }

        return result;
    }

    @Override
    public final String toString() {

        return getClass().getSimpleName() + " [map=" + schemaObjectByName + "]";
    }
}
