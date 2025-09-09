package dev.jdata.db.schema.allocators.model.schemamaps.cache;

import java.util.Objects;

import dev.jdata.db.schema.allocators.model.schemamaps.IAllCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.schemamaps.cache.CachedAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.cache.CachedAllSimpleCompleteSchemaBuilder;
import dev.jdata.db.utils.allocators.NodeObjectCache;

public final class CacheCompleteSchemaMapsBuilderAllocator

        implements IAllCompleteSchemaMapsBuilderAllocator<CachedAllCompleteSchemaMaps, CachedAllSimpleCompleteSchemaBuilder> {

    private final NodeObjectCache<CachedAllSimpleCompleteSchemaBuilder> completeSchemaMapsBuilderCache;

    CacheCompleteSchemaMapsBuilderAllocator() {

        this.completeSchemaMapsBuilderCache = new NodeObjectCache<>(() -> new CachedAllSimpleCompleteSchemaBuilder());
    }

    @Override
    public CachedAllSimpleCompleteSchemaBuilder allocateCompleteSchemaMapsBuilder() {

        return completeSchemaMapsBuilderCache.allocate();
    }

    @Override
    public void freeCompleteSchemaMapsBuilder(CachedAllSimpleCompleteSchemaBuilder builder) {

        Objects.requireNonNull(builder);

        completeSchemaMapsBuilderCache.free(builder);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        statisticsGatherer.addNodeObjectCache("completeSchemaMapsBuilderCache", CachedAllSimpleCompleteSchemaBuilder.class, completeSchemaMapsBuilderCache);
    }
}
