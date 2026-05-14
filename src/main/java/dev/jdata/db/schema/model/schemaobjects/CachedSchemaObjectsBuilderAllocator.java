package dev.jdata.db.schema.model.schemaobjects;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.ICachedIndexList;
import dev.jdata.db.utils.adt.lists.ICachedIndexListAllocator;
import dev.jdata.db.utils.adt.lists.ICachedIndexListBuilder;
import dev.jdata.db.utils.adt.maps.ICachedLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.adt.maps.IHeapLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.classes.Classes;

final class CachedSchemaObjectsBuilderAllocator<T extends SchemaObject>

        extends SchemaObjectsBuilderAllocator<

                        T,
                        ICachedIndexList<T>,
                        ICachedIndexListBuilder<T>,
                        ICachedIndexListAllocator<T>,
                        ICachedSchemaObjects<T>,
                        ICachedSchemaObjectsBuilder<T>> {

    private final NodeObjectCache<CachedSchemaObjectsBuilder<T>> schemaObjectsBuilderCache;

    CachedSchemaObjectsBuilderAllocator(ICachedIndexListAllocator<T> indexListAllocator, ICachedLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator,
            IHeapLongToObjectDynamicMapAllocator<T> heapLongToObjectMapAllocator, IntFunction<T[]> createValuesArray) {
        super(indexListAllocator, createValuesArray);

        this.schemaObjectsBuilderCache = new NodeObjectCache<>(() -> new CachedSchemaObjectsBuilder<>(AllocationType.CACHING_ALLOCATOR, indexListAllocator,
                longToObjectMapAllocator, heapLongToObjectMapAllocator, createValuesArray));
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        super.gatherStatistics(statisticsGatherer);

        statisticsGatherer.addNodeObjectCache("schemaObjectsBuilderCache", Classes.genericClass(CachedSchemaObjectsBuilder.class), schemaObjectsBuilderCache);
    }

    @Override
    public CachedSchemaObjectsBuilder<T> createBuilder(int minimumCapacity) {

        checkCreateBuilderParameters(minimumCapacity);

        final CachedSchemaObjectsBuilder<T> result = schemaObjectsBuilderCache.allocate();

        result.initialize(minimumCapacity);

        return result;
    }

    @Override
    public void freeBuilder(ICachedSchemaObjectsBuilder<T> builder) {

        checkFreeCreatedBuilderParameters(builder);

        final CachedSchemaObjectsBuilder<T> cachedSchemaObjectsBuilder = (CachedSchemaObjectsBuilder<T>)builder;

        cachedSchemaObjectsBuilder.free();

        schemaObjectsBuilderCache.free(cachedSchemaObjectsBuilder);
    }
}
