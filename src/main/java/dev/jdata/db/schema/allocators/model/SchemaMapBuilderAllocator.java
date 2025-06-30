package dev.jdata.db.schema.allocators.model;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilder;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.ILongToObjectMaxDistanceMapAllocator;

public abstract class SchemaMapBuilderAllocator<

                SCHEMA_OBJECT extends SchemaObject,
                INDEX_LIST extends IndexList<SCHEMA_OBJECT>,
                INDEX_LIST_BUILDER extends IndexList.IndexListBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER>,
                INDEX_LIST_ALLOCATOR extends IndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, ?>,
                SCHEMA_MAP extends SchemaMap<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP>,
                SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>>

        implements IAllocators {

    private final IntFunction<SCHEMA_OBJECT[]> createValuesArray;
    private final INDEX_LIST_ALLOCATOR indexListAllocator;
    private final ILongToObjectMaxDistanceMapAllocator<SCHEMA_OBJECT> longToObjectMapAllocator;

    public abstract SCHEMA_MAP_BUILDER allocateSchemaMapBuilder(int minimumCapacity);
    public abstract void freeSchemaMapBuilder(SCHEMA_MAP_BUILDER builder);

    protected SchemaMapBuilderAllocator(IntFunction<SCHEMA_OBJECT[]> createValuesArray, INDEX_LIST_ALLOCATOR indexListAllocator,
            ILongToObjectMaxDistanceMapAllocator<SCHEMA_OBJECT> longToObjectMapAllocator) {

        this.createValuesArray = Objects.requireNonNull(createValuesArray);
        this.indexListAllocator = Objects.requireNonNull(indexListAllocator);
        this.longToObjectMapAllocator = Objects.requireNonNull(longToObjectMapAllocator);
    }

    protected final IntFunction<SCHEMA_OBJECT[]> getCreateValuesArray() {
        return createValuesArray;
    }

    protected final INDEX_LIST_ALLOCATOR getIndexListAllocator() {
        return indexListAllocator;
    }

    protected final ILongToObjectMaxDistanceMapAllocator<SCHEMA_OBJECT> getLongToObjectMapAllocator() {
        return longToObjectMapAllocator;
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);
    }
}
