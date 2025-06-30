package dev.jdata.db.schema.allocators.schemas.cache;

import java.util.Objects;

import dev.jdata.db.schema.allocators.model.diff.dropped.DroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.allocators.schemas.DatabaseSchemasAllocator;
import dev.jdata.db.schema.model.diff.dropped.DroppedSchemaObjects;
import dev.jdata.db.utils.allocators.NodeObjectCache;

public final class CacheDatabaseSchemasAllocator extends DatabaseSchemasAllocator {

    private final NodeObjectCache<DroppedSchemaObjects> droppedSchemaObjectsCache;

    public CacheDatabaseSchemasAllocator(DroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator) {
        super(droppedSchemaObjectsAllocator);

        this.droppedSchemaObjectsCache = new NodeObjectCache<>(DroppedSchemaObjects::new);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        super.gatherStatistics(statisticsGatherer);

        statisticsGatherer.addNodeObjectCache("droppedSchemaObjectsCache", DroppedSchemaObjects.class, droppedSchemaObjectsCache);
    }

    @Override
    public DroppedSchemaObjects allocateDroppedSchemaObjects() {

        return droppedSchemaObjectsCache.allocate();
    }

    @Override
    public void freeDroppedSchemaObjects(DroppedSchemaObjects droppedSchemaObjects) {

        Objects.requireNonNull(droppedSchemaObjects);

        droppedSchemaObjectsCache.free(droppedSchemaObjects);
    }
}
