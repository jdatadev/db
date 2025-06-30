package dev.jdata.db.schema.allocators.model.cache;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.cache.CachedSchemaMap;
import dev.jdata.db.schema.model.cache.CachedSchemaMapBuilder;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.CachedIndexList;
import dev.jdata.db.utils.adt.lists.CachedIndexList.CacheIndexListAllocator;
import dev.jdata.db.utils.allocators.ILongToObjectMaxDistanceMapAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;

public final class CacheSchemaMapBuilderAllocator<T extends SchemaObject>

        extends SchemaMapBuilderAllocator<
                        T,
                        CachedIndexList<T>,
                        CachedIndexList.CachedIndexListBuilder<T>,
                        CacheIndexListAllocator<T>,
                        CachedSchemaMap<T>,
                        CachedSchemaMapBuilder<T>> {

    private final NodeObjectCache<CachedSchemaMapBuilder<T>> objectCache;

    public CacheSchemaMapBuilderAllocator(IntFunction<T[]> createValuesArray, CacheIndexListAllocator<T> indexListAllocator,
            ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {
        super(createValuesArray, indexListAllocator, longToObjectMapAllocator);

        this.objectCache = new NodeObjectCache<>(() -> new CachedSchemaMapBuilder<>(createValuesArray, indexListAllocator, longToObjectMapAllocator));
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
    public CachedSchemaMapBuilder<T> allocateSchemaMapBuilder(int minimumCapacity) {

        final CachedSchemaMapBuilder<T> result = objectCache.allocate();

        result.initialize(minimumCapacity);

        return result;
    }

    @Override
    public void freeSchemaMapBuilder(CachedSchemaMapBuilder<T> builder) {

        Objects.requireNonNull(builder);

        builder.free();

        objectCache.free(builder);
    }
}
