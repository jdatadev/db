package dev.jdata.db.schema.allocators.databases.cache;

import java.util.Objects;

import dev.jdata.db.schema.allocators.databases.DatabasesSchemaManagerAllocator;
import dev.jdata.db.schema.allocators.model.diff.dropped.DroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.allocators.model.schemamaps.cache.CacheCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.allocators.schemas.IDatabaseSchemasAllocator;
import dev.jdata.db.schema.model.cache.CachedSchemaMap;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.cache.CachedAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.cache.CachedAllSimpleCompleteSchemaBuilder;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

@Deprecated // currently not in use
final class CacheDatabasesSchemaManagerAllocator

        extends DatabasesSchemaManagerAllocator<CachedSchemaMap<SchemaObject>, CachedAllCompleteSchemaMaps, CachedAllSimpleCompleteSchemaBuilder>
        implements IAllocators {

    private final SchemaMapBuildersCache schemaMapBuildersCache;

    CacheDatabasesSchemaManagerAllocator(DroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator, IDatabaseSchemasAllocator databaseSchemasAllocator,
            CacheCompleteSchemaMapsBuilderAllocator completeSchemaMapsBuilderAllocator) {
        super(droppedSchemaObjectsAllocator, databaseSchemasAllocator, completeSchemaMapsBuilderAllocator);

        this.schemaMapBuildersCache = null; // new SchemaMapBuildersCache(completeSchemaMapsBuilderAllocator);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addAllocators("schemaMapBuildersCache", RefType.INSTANTIATED, schemaMapBuildersCache);
    }
}
