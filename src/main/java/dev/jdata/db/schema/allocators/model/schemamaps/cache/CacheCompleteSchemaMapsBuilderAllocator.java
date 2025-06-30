package dev.jdata.db.schema.allocators.model.schemamaps.cache;

import java.util.Objects;

import dev.jdata.db.schema.allocators.model.schemamaps.ICompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.schemamaps.CachedCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.CachedCompleteSchemaMaps.CachedCompleteSchemaBuilder;
import dev.jdata.db.utils.allocators.NodeObjectCache;

public final class CacheCompleteSchemaMapsBuilderAllocator

        implements ICompleteSchemaMapsBuilderAllocator<CachedCompleteSchemaMaps, CachedCompleteSchemaMaps.CachedCompleteSchemaBuilder> {

    private final NodeObjectCache<CachedCompleteSchemaBuilder> completeSchemaMapsBuilderCache;

    CacheCompleteSchemaMapsBuilderAllocator() {

        this.completeSchemaMapsBuilderCache = new NodeObjectCache<>(() -> new CachedCompleteSchemaMaps.CachedCompleteSchemaBuilder());
    }

    @Override
    public CachedCompleteSchemaMaps.CachedCompleteSchemaBuilder allocateCompleteSchemaMapsBuilder() {

        return completeSchemaMapsBuilderCache.allocate();
    }

    @Override
    public void freeCompleteSchemaMapsBuilder(CachedCompleteSchemaMaps.CachedCompleteSchemaBuilder builder) {

        Objects.requireNonNull(builder);

        completeSchemaMapsBuilderCache.free(builder);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        statisticsGatherer.addNodeObjectCache("completeSchemaMapsBuilderCache", CachedCompleteSchemaBuilder.class, completeSchemaMapsBuilderCache);
    }
}
