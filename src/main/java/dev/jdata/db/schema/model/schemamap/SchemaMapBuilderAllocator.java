package dev.jdata.db.schema.model.schemamap;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.ISchemaMapBuilderAllocator;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.allocators.IAllocators;

abstract class SchemaMapBuilderAllocator<

                SCHEMA_OBJECT extends SchemaObject,
                INDEX_LIST extends IIndexList<SCHEMA_OBJECT>,
                INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, INDEX_LIST, ?>,
                INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, ?, INDEX_LIST_BUILDER>,
                SCHEMA_MAP extends ISchemaMap<SCHEMA_OBJECT>,
                SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, ?>>

        implements ISchemaMapBuilderAllocator<SCHEMA_OBJECT, SCHEMA_MAP, SCHEMA_MAP_BUILDER>, IAllocators {

    private final IntFunction<SCHEMA_OBJECT[]> createValuesArray;
    private final INDEX_LIST_ALLOCATOR indexListAllocator;
    private final IHeapMutableLongToObjectDynamicMapAllocator<SCHEMA_OBJECT> longToObjectMapAllocator;

    protected SchemaMapBuilderAllocator(IntFunction<SCHEMA_OBJECT[]> createValuesArray, INDEX_LIST_ALLOCATOR indexListAllocator,
            IHeapMutableLongToObjectDynamicMapAllocator<SCHEMA_OBJECT> longToObjectMapAllocator) {

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

    protected final IHeapMutableLongToObjectDynamicMapAllocator<SCHEMA_OBJECT> getLongToObjectMapAllocator() {
        return longToObjectMapAllocator;
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);
    }
}
