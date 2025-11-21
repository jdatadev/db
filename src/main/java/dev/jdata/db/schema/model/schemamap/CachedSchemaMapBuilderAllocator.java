package dev.jdata.db.schema.model.schemamap;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.ICachedIndexListAllocator;
import dev.jdata.db.utils.adt.lists.ICachedIndexList;
import dev.jdata.db.utils.adt.lists.ICachedIndexListBuilder;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.NodeObjectCache;

final class CachedSchemaMapBuilderAllocator<T extends SchemaObject>

        extends SchemaMapBuilderAllocator<

                        T,
                        ICachedIndexList<T>,
                        ICachedIndexListBuilder<T>,
                        ICachedIndexListAllocator<T>,
                        ICachedSchemaMap<T>,
                        ICachedSchemaMapBuilder<T>> {

    private final NodeObjectCache<CachedSchemaMapBuilder<T>> objectCache;

    CachedSchemaMapBuilderAllocator(IntFunction<T[]> createValuesArray, ICachedIndexListAllocator<T> indexListAllocator,
            IHeapMutableLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator) {
        super(createValuesArray, indexListAllocator, longToObjectMapAllocator);

        this.objectCache = new NodeObjectCache<>(() -> new CachedSchemaMapBuilder<>(AllocationType.CACHING_ALLOCATOR, createValuesArray, indexListAllocator,
                longToObjectMapAllocator));
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        super.gatherStatistics(statisticsGatherer);

        @SuppressWarnings("unchecked")
        final Class<CachedSchemaMapBuilder<T>> builderClass = (Class<CachedSchemaMapBuilder<T>>)(Class<?>)CachedSchemaMapBuilder.class;

        statisticsGatherer.addNodeObjectCache("objectCache", builderClass, objectCache);
    }

    @Override
    public CachedSchemaMapBuilder<T> createBuilder(int minimumCapacity) {

        final CachedSchemaMapBuilder<T> result = objectCache.allocate();

        result.initialize(minimumCapacity);

        return result;
    }

    @Override
    public void freeBuilder(ICachedSchemaMapBuilder<T> builder) {

        Objects.requireNonNull(builder);

        final CachedSchemaMapBuilder<T> cachedSchemaMapBuilder = (CachedSchemaMapBuilder<T>)builder;

        cachedSchemaMapBuilder.free();

        objectCache.free(cachedSchemaMapBuilder);
    }
}
