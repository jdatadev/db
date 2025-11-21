package dev.jdata.db.schema.allocators.databases.cache;

import java.util.Objects;

import dev.jdata.db.schema.allocators.databases.DatabasesSchemaManagerAllocator;
import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElementsAllocators;
import dev.jdata.db.schema.model.schemamaps.ICachedAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.ICachedAllCompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.ICachedAllCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemas.IDatabaseSchemasAllocator;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.allocators.IAllocators;

@Deprecated // currently not in use
final class CacheDatabasesSchemaManagerAllocator<T extends IMutableIntSet, U extends IMutableIntToObjectWithRemoveStaticMap<T>>

        extends DatabasesSchemaManagerAllocator<T, U, ICachedAllCompleteSchemaMaps, IHeapAllCompleteSchemaMaps, ICachedAllCompleteSchemaMapsBuilder>
        implements IAllocators {

    private final SchemaMapBuildersCache schemaMapBuildersCache;

    CacheDatabasesSchemaManagerAllocator(SchemaDroppedElementsAllocators droppedSchemaObjectsAllocator, IDatabaseSchemasAllocator<T, U> databaseSchemasAllocator,
            ICachedAllCompleteSchemaMapsBuilderAllocator allCompleteSchemaMapsBuilderAllocator) {
        super(droppedSchemaObjectsAllocator, databaseSchemasAllocator, allCompleteSchemaMapsBuilderAllocator);

        this.schemaMapBuildersCache = null; // new SchemaMapBuildersCache(completeSchemaMapsBuilderAllocator);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        super.gatherStatistics(statisticsGatherer);

        // fix
        // statisticsGatherer.addAllocators("schemaMapBuildersCache", RefType.INSTANTIATED, schemaMapBuildersCache);
        throw new UnsupportedOperationException();
    }
}
