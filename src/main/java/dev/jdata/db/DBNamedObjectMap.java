package dev.jdata.db;

import java.util.Objects;
import java.util.function.IntFunction;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.adt.elements.IObjectUnorderedOnlyElementsView;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.lists.IIndexListView;
import dev.jdata.db.utils.adt.maps.IHeapMutableDynamicMap;
import dev.jdata.db.utils.adt.maps.ILongToObjectDynamicMap;
import dev.jdata.db.utils.adt.maps.ILongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.adt.maps.ILongToObjectDynamicMapBuilder;
import dev.jdata.db.utils.adt.maps.ILongToObjectDynamicMapView;
import dev.jdata.db.utils.adt.maps.ILongToObjectMapView;
import dev.jdata.db.utils.adt.maps.IMutableDynamicMap;
import dev.jdata.db.utils.adt.maps.IObjectToObjectDynamicMapView;
import dev.jdata.db.utils.adt.sets.IHeapMutableLongSet;
import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;

public abstract class DBNamedObjectMap<

                NAMED_OBJECT extends DBNamedObject,
                MAP extends ILongToObjectDynamicMap<NAMED_OBJECT>,
                MAP_BUILDER extends ILongToObjectDynamicMapBuilder<NAMED_OBJECT, MAP, ?>,
                MAP_ALLOCATOR extends ILongToObjectDynamicMapAllocator<NAMED_OBJECT, MAP, ?, MAP_BUILDER>,
                NAMED_OBJECT_MAP extends DBNamedObjectMap<NAMED_OBJECT, MAP, MAP_BUILDER, MAP_ALLOCATOR, NAMED_OBJECT_MAP>>

        extends Allocatable
        implements IObjectUnorderedOnlyElementsView<NAMED_OBJECT> {

    private static final boolean ASSERT = AssertionContants.ASSERT_DB_NAMED_OBJECT_MAP;

    protected static final boolean EQUALS_NAME_CASE_SENSITIVE = true;

    private final IntFunction<NAMED_OBJECT[]> createNamedObjectArray;

    private final MAP objectByName;

    public abstract NAMED_OBJECT_MAP makeCopy();

    protected DBNamedObjectMap(AllocationType allocationType) {
        super(allocationType);

        this.createNamedObjectArray = null;
        this.objectByName = null;
    }

    protected DBNamedObjectMap(AllocationType allocationType, IIndexListView<NAMED_OBJECT> namedObjects, IntFunction<NAMED_OBJECT[]> createNamedObjectArray,
            MAP_ALLOCATOR longToObjectMapAllocator) {
        super(allocationType);

        Checks.isNotEmpty(namedObjects);
        Objects.requireNonNull(createNamedObjectArray);
        Objects.requireNonNull(longToObjectMapAllocator);

        this.createNamedObjectArray = Objects.requireNonNull(createNamedObjectArray);

        final long numElements = namedObjects.getNumElements();

        final MAP_BUILDER builder = longToObjectMapAllocator.createBuilder(numElements);

        try {
            for (int i = 0; i < numElements; ++ i) {

                final NAMED_OBJECT namedObject = namedObjects.get(i);

                builder.add(namedObject.getHashName(), namedObject);
            }

            this.objectByName = builder.buildNotEmpty();
        }
        finally {

            longToObjectMapAllocator.freeBuilder(builder);
        }
    }

    @Override
    public boolean isEmpty() {

        return IContainsView.isNullOrEmpty(objectByName);
    }

    @Override
    public long getNumElements() {

        return isEmpty() ? 0L : objectByName.getNumElements();
    }

    @Override
    public final <P> void toString(StringBuilder sb, P parameter, IElementsToStringAdder<NAMED_OBJECT, P> consumer) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(consumer);

        throw new UnsupportedOperationException();
    }

    public final void free(MAP_ALLOCATOR longToObjectAllocator) {

        Objects.requireNonNull(longToObjectAllocator);

        longToObjectAllocator.freeImmutable(objectByName);
    }

    protected final boolean containsNamedObject(long name) {

        StringRef.checkIsString(name);

        return isEmpty() ? false : objectByName.containsKey(name);
    }

    protected final NAMED_OBJECT getNamedObject(long name) {

        StringRef.checkIsString(name);

        return isEmpty() ? null : objectByName.get(name);
    }

    private StringResolver scratchThisStringResolver;

    public final boolean equalsMap(StringResolver thisStringResolver, DBNamedObjectMap<NAMED_OBJECT, MAP, ?, ?, ?> other, StringResolver otherStringResolver) {

        Objects.requireNonNull(thisStringResolver);
        Objects.requireNonNull(other);
        Objects.requireNonNull(otherStringResolver);

        final boolean result;

        final boolean mapNullOrEmpty = IContainsView.isNullOrEmpty(objectByName);
        final boolean otherNullOrEmpty = IContainsView.isNullOrEmpty(other.objectByName);

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
            final ILongToObjectDynamicMap<NAMED_OBJECT> thisMap = objectByName;
            final ILongToObjectDynamicMap<NAMED_OBJECT> otherMap = other.objectByName;

            result = equals(thisMap, IOnlyElementsView.intNumElements(thisMap), thisStringResolver, otherMap, IOnlyElementsView.intNumElements(otherMap), otherStringResolver);
        }

        return result;
    }

    private boolean equals(ILongToObjectDynamicMapView<NAMED_OBJECT> thisMap, int thisMapInitialCapacity, StringResolver thisStringResolver, ILongToObjectDynamicMapView<NAMED_OBJECT> otherMap,
            int otherMapInitialCapacity, StringResolver otherStringResolver) {

        boolean result;

        final IHeapMutableLongSet thisKeys = IHeapMutableLongSet.create(IOnlyElementsView.intNumElements(thisMap));
        final IHeapMutableLongSet otherKeys = IHeapMutableLongSet.create(IOnlyElementsView.intNumElements(otherMap));

        final long numThisKeys = thisMap.keys(thisKeys);
        final long numOtherKeys = otherMap.keys(otherKeys);

        if (numThisKeys != numOtherKeys) {

            result = false;
        }
        else {
            final IObjectToObjectDynamicMapView<String, NAMED_OBJECT> thisStringMap = makeMapForEquals(thisMap, thisMapInitialCapacity, thisStringResolver);
            final IObjectToObjectDynamicMapView<String, NAMED_OBJECT> otherStringMap = makeMapForEquals(otherMap, otherMapInitialCapacity, otherStringResolver);

            result = thisStringMap.equals(thisStringResolver, otherStringMap, otherStringResolver, (e1, r1, e2, r2) -> e1.equalsName(r1, e2, r2, EQUALS_NAME_CASE_SENSITIVE));
        }

        return result;
    }

    private IMutableDynamicMap<String, NAMED_OBJECT> scratchMutableObjectMap;

    private IObjectToObjectDynamicMapView<String, NAMED_OBJECT> makeMapForEquals(ILongToObjectMapView<NAMED_OBJECT> map, int initialCapacity, StringResolver stringResolver) {

        if (ASSERT) {

            Assertions.isNull(scratchThisStringResolver);
            Assertions.isNull(scratchMutableObjectMap);
        }

        final IMutableDynamicMap<String, NAMED_OBJECT> result = IHeapMutableDynamicMap.create(initialCapacity, String[]::new, createNamedObjectArray);

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
            final DBNamedObjectMap<?, ?, ?, ?, ?> other = (DBNamedObjectMap<?, ?, ?, ?, ?>)object;

            result = Objects.equals(objectByName, other.objectByName);
        }

        return result;
    }

    @Override
    public final String toString() {

        return getClass().getSimpleName() + " [map=" + objectByName + "]";
    }
}
