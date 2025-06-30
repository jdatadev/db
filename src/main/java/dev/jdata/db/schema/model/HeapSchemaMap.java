package dev.jdata.db.schema.model;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.HeapIndexList;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListBuilder;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListAllocator;
import dev.jdata.db.utils.allocators.ILongToObjectMaxDistanceMapAllocator;

public final class HeapSchemaMap<T extends SchemaObject>

        extends SchemaMap<T, HeapIndexList<T>, HeapIndexListBuilder<T>, HeapIndexListAllocator<T>, HeapSchemaMap<T>> {

    public static final class HeapSchemaMapBuilderAllocator<T extends SchemaObject>

        extends SchemaMapBuilderAllocator<T, HeapIndexList<T>, HeapIndexListBuilder<T>, HeapIndexListAllocator<T>, HeapSchemaMap<T>, HeapSchemaMapBuilder<T>> {

        public HeapSchemaMapBuilderAllocator(IntFunction<T[]> createValuesArray, HeapIndexListAllocator<T> indexListAllocator,
                ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {
            super(createValuesArray, indexListAllocator, longToObjectMapAllocator);
        }

        @Override
        public HeapSchemaMapBuilder<T> allocateSchemaMapBuilder(int minimumCapacity) {

            return new HeapSchemaMapBuilder<>(getCreateValuesArray(), getIndexListAllocator(), getLongToObjectMapAllocator(), minimumCapacity);
        }

        @Override
        public void freeSchemaMapBuilder(HeapSchemaMapBuilder<T> builder) {

            Objects.requireNonNull(builder);
        }
    }

    public static final class HeapSchemaMapBuilder<T extends SchemaObject>

            extends SchemaMapBuilder<T, HeapIndexList<T>, HeapIndexListBuilder<T>, HeapIndexListAllocator<T>, HeapSchemaMap<T>, HeapSchemaMapBuilder<T>> {

        private HeapSchemaMapBuilder(IntFunction<T[]> createValuesArray, HeapIndexListAllocator<T> indexListAllocator,
                ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator, int initialCapacity) {
            super(createValuesArray, indexListAllocator, longToObjectMapAllocator, initialCapacity);
        }

        @Override
        protected HeapSchemaMap<T> empty() {

            return HeapSchemaMap.empty();
        }

        @Override
        protected HeapSchemaMap<T> create(HeapIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray, ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {

            return new HeapSchemaMap<>(AllocationType.HEAP_ALLOCATOR, schemaObjects, createValuesArray, longToObjectMapAllocator);
        }
    }

    public static <T extends SchemaObject> HeapSchemaMap<T> of(HeapIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray,
            ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {

        Objects.requireNonNull(schemaObjects);
        Objects.requireNonNull(createValuesArray);
        Objects.requireNonNull(longToObjectMapAllocator);

        return new HeapSchemaMap<>(AllocationType.HEAP, schemaObjects, createValuesArray, longToObjectMapAllocator);
    }

    private static final HeapSchemaMap<?> emptySchemaMap = new HeapSchemaMap<>(AllocationType.HEAP_CONSTANT);

    @SuppressWarnings("unchecked")
    public static <T extends SchemaObject> HeapSchemaMap<T> empty() {

        return (HeapSchemaMap<T>)emptySchemaMap;
    }

    private HeapSchemaMap(AllocationType allocationType, HeapIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray,
            ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {
        super(allocationType, schemaObjects, createValuesArray, longToObjectMapAllocator);
    }

    private HeapSchemaMap(AllocationType allocationType) {
        super(allocationType);
    }
}
