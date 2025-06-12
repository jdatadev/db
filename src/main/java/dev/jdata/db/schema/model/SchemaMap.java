package dev.jdata.db.schema.model;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.DBNamedObjectMap;
import dev.jdata.db.schema.DDLObjectTypeAllocators;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.IContains;
import dev.jdata.db.utils.adt.elements.IIterableElements;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IBuilder;
import dev.jdata.db.utils.allocators.ILongToObjectMaxDistanceMapAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;

public final class SchemaMap<T extends SchemaObject> extends DBNamedObjectMap<T, SchemaMap<T>> implements ISchemaMap<T> {

    public static final class SchemaMapBuilderAllocators extends DDLObjectTypeAllocators<SchemaMapBuilderAllocator<?>> {

        public SchemaMapBuilderAllocators(Function<DDLObjectType, SchemaMapBuilderAllocator<?>> createSchemaMapBuilderAllocator) {
            super(SchemaMapBuilderAllocator<?>[]::new, (t, g) -> createSchemaMapBuilderAllocator.apply(t));
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Class<SchemaMapBuilderAllocator<?>> getCachedObjectClass() {

            return (Class<SchemaMapBuilderAllocator<?>>)(Class<?>)SchemaMapBuilderAllocator.class;
        }

        @SuppressWarnings("unchecked")
        public <T extends SchemaObject> SchemaMapBuilderAllocator<T> getAllocator(DDLObjectType ddlObjectType) {

            return (SchemaMapBuilderAllocator<T>)getInstance(ddlObjectType);
        }
    }

    public static abstract class SchemaMapBuilderAllocator<T extends SchemaObject> implements IAllocators {

        final IntFunction<T[]> createValuesArray;
        final IndexListAllocator<T> indexListAllocator;
        final ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator;

        abstract Builder<T> allocateSchemaMapBuilder(int minimumCapacity);

        public abstract void freeSchemaMapBuilder(Builder<T> builder);

        SchemaMapBuilderAllocator(IntFunction<T[]> createValuesArray, IndexListAllocator<T> indexListAllocator,
                ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {

            this.createValuesArray = Objects.requireNonNull(createValuesArray);
            this.indexListAllocator = Objects.requireNonNull(indexListAllocator);
            this.longToObjectMapAllocator = Objects.requireNonNull(longToObjectMapAllocator);
        }

        @Override
        public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

            Objects.requireNonNull(statisticsGatherer);
        }
    }

    public static final class HeapSchemaMapBuilderAllocator<T extends SchemaObject> extends SchemaMapBuilderAllocator<T> {

        public HeapSchemaMapBuilderAllocator(IntFunction<T[]> createValuesArray, IndexListAllocator<T> indexListAllocator,
                ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {
            super(createValuesArray, indexListAllocator, longToObjectMapAllocator);
        }

        @Override
        Builder<T> allocateSchemaMapBuilder(int minimumCapacity) {

            return new Builder<>(createValuesArray, indexListAllocator, longToObjectMapAllocator, this, minimumCapacity);
        }

        @Override
        public void freeSchemaMapBuilder(Builder<T> builder) {

            Objects.requireNonNull(builder);
        }
    }

    public static final class CacheSchemaMapBuilderAllocator<T extends SchemaObject> extends SchemaMapBuilderAllocator<T> {

        private final NodeObjectCache<Builder<T>> objectCache;

        public CacheSchemaMapBuilderAllocator(IntFunction<T[]> createValuesArray, IndexListAllocator<T> indexListAllocator,
                ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {
            super(createValuesArray, indexListAllocator, longToObjectMapAllocator);

            this.objectCache = new NodeObjectCache<>(() -> new Builder<>(createValuesArray, indexListAllocator, longToObjectMapAllocator, this));
        }

        @Override
        public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

            Objects.requireNonNull(statisticsGatherer);

            super.gatherStatistics(statisticsGatherer);

            @SuppressWarnings("unchecked")
            final Class<Builder<T>> builderClass = (Class<Builder<T>>)(Class<?>)Builder.class;

            statisticsGatherer.addNodeObjectCache("objectCache", builderClass, objectCache);
        }

        @Override
        Builder<T> allocateSchemaMapBuilder(int minimumCapacity) {

            final Builder<T> result = objectCache.allocate();

            result.initialize(minimumCapacity);

            return result;
        }

        @Override
        public void freeSchemaMapBuilder(Builder<T> builder) {

            Objects.requireNonNull(builder);

            builder.free();

            objectCache.free(builder);
        }
    }

    public static <T extends SchemaObject> Builder<T> createBuilder(int initialCapacity, SchemaMapBuilderAllocator<T> builderAllocator) {

        Checks.isInitialCapacity(initialCapacity);
        Objects.requireNonNull(builderAllocator);

        return builderAllocator.allocateSchemaMapBuilder(initialCapacity);
    }

    public static final class Builder<T extends SchemaObject> extends ObjectCacheNode implements IBuilder<T, Builder<T>> {

        private IntFunction<T[]> createValuesArray;
        private IndexListAllocator<T> indexListAllocator;
        private ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator;
        private SchemaMapBuilderAllocator<T> builderAllocator;

        private IndexList.Builder<T> schemaObjectsBuilder;

        Builder(IntFunction<T[]> createValuesArray, IndexListAllocator<T> indexListAllocator, ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator,
                SchemaMapBuilderAllocator<T> builderAllocator) {

            this.createValuesArray = Objects.requireNonNull(createValuesArray);
            this.indexListAllocator = Objects.requireNonNull(indexListAllocator);
            this.longToObjectMapAllocator = Objects.requireNonNull(longToObjectMapAllocator);
            this.builderAllocator = Objects.requireNonNull(builderAllocator);
        }

        Builder(IntFunction<T[]> createValuesArray, IndexListAllocator<T> indexListAllocator, ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator,
                SchemaMapBuilderAllocator<T> builderAllocator, int initialCapacity) {
            this(createValuesArray, indexListAllocator, longToObjectMapAllocator, builderAllocator);

            initialize(initialCapacity);
        }

        private void initialize(int initialCapacity) {

            this.schemaObjectsBuilder = IndexList.createBuilder(initialCapacity, indexListAllocator);
        }

        private void free() {

            indexListAllocator.freeIndexListBuilder(schemaObjectsBuilder);
            builderAllocator.freeSchemaMapBuilder(this);

            this.createValuesArray = null;
            this.builderAllocator = null;

            this.schemaObjectsBuilder = null;
        }

        @Override
        public boolean isEmpty() {

            return schemaObjectsBuilder.isEmpty();
        }

        public void add(T schemaObject) {

            Objects.requireNonNull(schemaObject);

            schemaObjectsBuilder.addTail(schemaObject);
        }

        public void add(IIterableElements<T> schemaObjects) {

            Checks.isNotEmpty(schemaObjects);

            schemaObjectsBuilder.addTail(schemaObjects);
        }

        public SchemaMap<T> build() {

            return schemaObjectsBuilder.isEmpty()
                    ? SchemaMap.empty()
                    : new SchemaMap<>(AllocationType.HEAP, schemaObjectsBuilder.build(), createValuesArray, longToObjectMapAllocator);
        }
    }

    private static final SchemaMap<?> emptySchemaMap = new SchemaMap<>(AllocationType.HEAP_CONSTANT);

    private final IIndexList<T> schemaObjects;

    @SuppressWarnings("unchecked")
    public static <T extends SchemaObject> SchemaMap<T> empty() {

        return (SchemaMap<T>)emptySchemaMap;
    }

    public static <T extends SchemaObject> SchemaMap<T> of(IIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray,
            ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {

        Objects.requireNonNull(schemaObjects);
        Objects.requireNonNull(createValuesArray);
        Objects.requireNonNull(longToObjectMapAllocator);

        return new SchemaMap<>(AllocationType.HEAP, schemaObjects, createValuesArray, longToObjectMapAllocator);
    }

    private SchemaMap(AllocationType allocationType) {
        super(allocationType);

        this.schemaObjects = null;
    }

    private SchemaMap(AllocationType allocationType, IIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray,
            ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {
        super(allocationType, schemaObjects, createValuesArray, longToObjectMapAllocator);

        this.schemaObjects = Objects.requireNonNull(schemaObjects);
    }

    private SchemaMap(AllocationType allocationType, SchemaMap<T> toCopy, ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {
        super(allocationType, toCopy, longToObjectMapAllocator);

        this.schemaObjects = toCopy.schemaObjects;
    }

    @Override
    public SchemaMap<T> makeCopy() {

        throw new UnsupportedOperationException(); // immutable
    }

    @Override
    public int maxInt(int defaultValue, ToIntFunction<? super T> mapper) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <P, E extends Exception> void forEach(P parameter, ForEach<T, P, E> forEach) throws E {

        forEach(parameter, forEach, (t, p, e) -> e.each(t, p));
    }

    @Override
    public <P1, P2, E extends Exception> void forEach(P1 parameter1, P2 parameter2, ForEach2<T, P1, P2, E> forEach) throws E {

        final long numElements = schemaObjects.getNumElements();

        for (long i = 0L; i < numElements; ++ i) {

            forEach.each(schemaObjects.get(i), parameter1, parameter2);
        }
    }

    @Override
    public boolean containsSchemaObjectName(long schemaObjectName) {

        StringRef.checkIsString(schemaObjectName);

        return containsNamedObject(schemaObjectName) ;
    }

    @Override
    public T getSchemaObjectByName(long schemaObjectName) {

        StringRef.checkIsString(schemaObjectName);

        return getNamedObject(schemaObjectName);
    }

    @Override
    public T getSchemaObjectById(int id) {

        Checks.isSchemaObjectId(id);

        return schemaObjects.get(id);
    }

    @Override
    public IIndexList<T> getSchemaObjects() {

        return schemaObjects;
    }

    @Override
    public T[] toArray(IntFunction<T[]> createArray) {

        return schemaObjects.toArray(createArray);
    }

    @Override
    public boolean isEqualTo(StringResolver thisStringResolver, ISchemaMap<T> other, StringResolver otherStringResolver) {

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
            final SchemaMap<T> otherSchemaMap = (SchemaMap<T>)other;

            final boolean schemaObjectsNullOrEmpty = IContains.isNullOrEmpty(schemaObjects);
            final boolean otherNullOrEmpty = IContains.isNullOrEmpty(otherSchemaMap.schemaObjects);

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

    private static <T extends SchemaObject> boolean equalsIndexList(IIndexList<T> thisIndexList, StringResolver thisStringResolver,
            IIndexList<T> otherIndexList, StringResolver otherStringResolver) {

        return thisIndexList.equals(thisStringResolver, otherIndexList, otherStringResolver,
                (e1, p1, e2, p2) -> e1.equalsName(thisStringResolver, e2, otherStringResolver, EQUALS_NAME_CASE_SENSITIVE));
    }

/*
    private static boolean isEmpty(SchemaMap<?> schemaMap) {

        final IIndexList<?> schemaObjects = schemaMap.schemaObjects;

        return schemaObjects == null || schemaObjects.isEmpty();
    }
*/
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
        else if (!super.equals(object)) {

            result = false;
        }
        else {
            final SchemaMap<?> other = (SchemaMap<?>)object;

            result = Objects.equals(schemaObjects, other.schemaObjects);
        }

        return result;
    }
}
