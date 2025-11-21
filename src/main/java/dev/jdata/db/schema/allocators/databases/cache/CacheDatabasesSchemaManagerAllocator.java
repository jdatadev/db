package dev.jdata.db.schema.allocators.databases.cache;

import java.util.Objects;

import dev.jdata.db.review.CachedAllCompleteSchemaMaps;
import dev.jdata.db.review.CachedAllSimpleCompleteSchemaBuilder;
import dev.jdata.db.review.CachedCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.allocators.databases.DatabasesSchemaManagerAllocator;
import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElementsAllocators;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.CachedSchemaMap;
import dev.jdata.db.schema.model.schemas.IDatabaseSchemasAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

@Deprecated // currently not in use
final class CacheDatabasesSchemaManagerAllocator

        extends DatabasesSchemaManagerAllocator<CachedSchemaMap<SchemaObject>, CachedAllCompleteSchemaMaps, CachedAllSimpleCompleteSchemaBuilder>
        implements IAllocators {

    private final SchemaMapBuildersCache schemaMapBuildersCache;

    CacheDatabasesSchemaManagerAllocator(SchemaDroppedElementsAllocators droppedSchemaObjectsAllocator, IDatabaseSchemasAllocator databaseSchemasAllocator,
            CachedCompleteSchemaMapsBuilderAllocator completeSchemaMapsBuilderAllocator) {
        super(droppedSchemaObjectsAllocator, databaseSchemasAllocator, completeSchemaMapsBuilderAllocator);

        this.schemaMapBuildersCache = null; // new SchemaMapBuildersCache(completeSchemaMapsBuilderAllocator);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addAllocators("schemaMapBuildersCache", RefType.INSTANTIATED, schemaMapBuildersCache);
    }
}
