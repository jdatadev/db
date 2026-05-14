package dev.jdata.db.schema.model.schemaobjects;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.capacity.CapacityMax;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.allocators.BuilderAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

abstract class SchemaObjectsBuilderAllocator<

                SCHEMA_OBJECT extends SchemaObject,
                INDEX_LIST extends IIndexList<SCHEMA_OBJECT>,
                INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, INDEX_LIST, ?>,
                INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, ?, INDEX_LIST_BUILDER>,
                SCHEMA_OBJECTS extends ISchemaObjects<SCHEMA_OBJECT>,
                SCHEMA_OBJECTS_BUILDER extends ISchemaObjectsBuilder<SCHEMA_OBJECT, SCHEMA_OBJECTS, ?>>

        extends BuilderAllocator<SCHEMA_OBJECTS, SCHEMA_OBJECTS_BUILDER>
        implements ISchemaObjectsBuilderAllocator<SCHEMA_OBJECT, SCHEMA_OBJECTS, SCHEMA_OBJECTS_BUILDER>, IAllocators {

    protected static void checkCreateBuilderParameters(int minimumCapacity) {

        checkCreateBuilderParameters(CapacityMax.INT, minimumCapacity);
    }

    private final INDEX_LIST_ALLOCATOR indexListAllocator;
    private final IntFunction<SCHEMA_OBJECT[]> createValuesArray;

    protected SchemaObjectsBuilderAllocator(INDEX_LIST_ALLOCATOR indexListAllocator, IntFunction<SCHEMA_OBJECT[]> createValuesArray) {

        this.indexListAllocator = Objects.requireNonNull(indexListAllocator);
        this.createValuesArray = Objects.requireNonNull(createValuesArray);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addInstanceAllocator("indexListAllocator", RefType.PASSED, SchemaObject.class, indexListAllocator);
    }

    protected final INDEX_LIST_ALLOCATOR getIndexListAllocator() {
        return indexListAllocator;
    }

    protected final IntFunction<SCHEMA_OBJECT[]> getCreateValuesArray() {
        return createValuesArray;
    }
}
