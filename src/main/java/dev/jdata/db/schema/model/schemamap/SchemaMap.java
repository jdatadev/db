package dev.jdata.db.schema.model.schemamap;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.DBNamedObjectMap;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.adt.elements.IObjectForEach;
import dev.jdata.db.utils.adt.elements.IObjectForEachWithResult;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;

abstract class SchemaMap<SCHEMA_OBJECT extends SchemaObject, INDEX_LIST extends IIndexList<SCHEMA_OBJECT>, SCHEMA_MAP extends SchemaMap<SCHEMA_OBJECT, INDEX_LIST, SCHEMA_MAP>>

        extends DBNamedObjectMap<SCHEMA_OBJECT, SCHEMA_MAP>
        implements ISchemaMap<SCHEMA_OBJECT> {

    private static final boolean ASSERT = AssertionContants.ASSERT_SCHEMA_MAP;

    private final INDEX_LIST schemaObjects;

    protected SchemaMap(AllocationType allocationType) {
        super(allocationType);

        this.schemaObjects = null;
    }

    protected SchemaMap(AllocationType allocationType, INDEX_LIST schemaObjects, IntFunction<SCHEMA_OBJECT[]> createValuesArray,
            IHeapMutableLongToObjectDynamicMapAllocator<SCHEMA_OBJECT> longToObjectMapAllocator) {
        super(allocationType, schemaObjects, createValuesArray, longToObjectMapAllocator);

        this.schemaObjects = Objects.requireNonNull(schemaObjects);
    }

    private SchemaMap(AllocationType allocationType, SCHEMA_MAP toCopy, IHeapMutableLongToObjectDynamicMapAllocator<SCHEMA_OBJECT> longToObjectMapAllocator) {
        super(allocationType, toCopy, longToObjectMapAllocator);

        final SchemaMap<SCHEMA_OBJECT, INDEX_LIST, SCHEMA_MAP> toCopyMap = toCopy;

        this.schemaObjects = toCopyMap.schemaObjects;
    }

    @Override
    public final boolean isEmpty() {

        final boolean isEmpty = schemaObjects == null || schemaObjects.isEmpty();

        if (ASSERT) {

            Assertions.areEqual(isEmpty, super.isEmpty());
        }

        return isEmpty;
    }

    @Override
    public final long getNumElements() {

        final long numElements = schemaObjects != null ? schemaObjects.getNumElements() : 0L;

        if (ASSERT) {

            Assertions.areEqual(numElements, super.getNumElements());
        }

        return numElements;
    }

    @Override
    public final SCHEMA_MAP makeCopy() {

        throw new UnsupportedOperationException(); // immutable
    }

    @Override
    public final <P> long count(P parameter, BiPredicate<SCHEMA_OBJECT, P> predicate) {

        return isEmpty() ? 0L : schemaObjects.count(parameter, predicate);
    }

    @Override
    public final int maxInt(int defaultValue, ToIntFunction<? super SCHEMA_OBJECT> mapper) {

        return isEmpty() ? defaultValue : schemaObjects.maxInt(defaultValue, mapper);
    }

    @Override
    public final long maxLong(long defaultValue, ToLongFunction<? super SCHEMA_OBJECT> mapper) {

        return isEmpty() ? defaultValue : schemaObjects.maxLong(defaultValue, mapper);
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, IObjectForEach<SCHEMA_OBJECT, P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        if (!isEmpty()) {

            schemaObjects.forEach(parameter, forEach);
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IObjectForEachWithResult<SCHEMA_OBJECT, P1, P2, R, E> forEach)
            throws E {

        Objects.requireNonNull(forEach);

        return isEmpty() ? defaultResult : schemaObjects.forEachWithResult(defaultResult, parameter1, parameter2, forEach);
    }

    @Override
    public final <P> SCHEMA_OBJECT findAtMostOne(P parameter, BiPredicate<SCHEMA_OBJECT, P> predicate) {

        Objects.requireNonNull(predicate);

        return isEmpty() ? null : schemaObjects.findAtMostOne(parameter, predicate);
    }

    @Override
    public final <P> SCHEMA_OBJECT findExactlyOne(P parameter, BiPredicate<SCHEMA_OBJECT, P> predicate) {

        return isEmpty() ? null : schemaObjects.findExactlyOne(parameter, predicate);
    }

    @Override
    public final boolean containsSchemaObjectName(long schemaObjectName) {

        StringRef.checkIsString(schemaObjectName);

        return containsNamedObject(schemaObjectName);
    }

    @Override
    public final SCHEMA_OBJECT getSchemaObjectByName(long schemaObjectName) {

        StringRef.checkIsString(schemaObjectName);

        return getNamedObject(schemaObjectName);
    }

    @Override
    public final SCHEMA_OBJECT getSchemaObjectById(int id) {

        Checks.isSchemaObjectId(id);

        return schemaObjects.get(id);
    }

    @Override
    public final INDEX_LIST getSchemaObjects() {

        return schemaObjects;
    }

    @Override
    public final SCHEMA_OBJECT[] toArray(IntFunction<SCHEMA_OBJECT[]> createArray) {

        return schemaObjects.toArray(createArray);
    }

    @Override
    public final boolean isEqualTo(StringResolver thisStringResolver, ISchemaMap<SCHEMA_OBJECT> other, StringResolver otherStringResolver) {

        Objects.requireNonNull(thisStringResolver);
        Objects.requireNonNull(other);
        Objects.requireNonNull(otherStringResolver);

        final boolean result;

        if (this == other) {

            result = true;
        }
        else if (getClass() != other.getClass()) {

            result = false;
        }
        else {
            @SuppressWarnings("unchecked")
            final SchemaMap<SCHEMA_OBJECT, INDEX_LIST, SCHEMA_MAP> otherSchemaMap = (SchemaMap<SCHEMA_OBJECT, INDEX_LIST, SCHEMA_MAP>)other;

            final boolean schemaObjectsNullOrEmpty = IContainsView.isNullOrEmpty(schemaObjects);
            final boolean otherNullOrEmpty = IContainsView.isNullOrEmpty(otherSchemaMap.schemaObjects);

            if (schemaObjectsNullOrEmpty && otherNullOrEmpty) {

                result = true;
            }
            else if (schemaObjectsNullOrEmpty || otherNullOrEmpty) {

                result = false;
            }
            else {
                result =    equalsIndexList(schemaObjects, thisStringResolver, otherSchemaMap.schemaObjects, otherStringResolver)
                         && equalsMap(thisStringResolver, otherSchemaMap, otherStringResolver);
            }
        }

        return result;
    }

    private static <T extends SchemaObject> boolean equalsIndexList(IIndexList<T> thisIndexList, StringResolver thisStringResolver, IIndexList<T> otherIndexList,
            StringResolver otherStringResolver) {

        return thisIndexList.equals(thisStringResolver, otherIndexList, otherStringResolver,
                (e1, p1, e2, p2) -> e1.equalsName(thisStringResolver, e2, otherStringResolver, EQUALS_NAME_CASE_SENSITIVE));
    }

    @Override
    public final boolean equals(Object object) {

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
        else if (!super.equals(object)) {

            result = false;
        }
        else {
            final SchemaMap<?, ?, ?> other = (SchemaMap<?, ?, ?>)object;

            result = Objects.equals(schemaObjects, other.schemaObjects);
        }

        return result;
    }
}
