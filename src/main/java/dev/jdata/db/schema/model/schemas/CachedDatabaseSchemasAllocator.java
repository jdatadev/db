package dev.jdata.db.schema.model.schemas;

import java.util.Objects;

import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElements;
import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElementsAllocators;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.classes.Classes;

public final class CachedDatabaseSchemasAllocator<T extends IMutableIntSet, U extends IMutableIntToObjectWithRemoveStaticMap<T>> extends DatabaseSchemasAllocator<T, U> {

    private final NodeObjectCache<SchemaDroppedElements<T, U>> schemaDroppedElementsCache;

    public CachedDatabaseSchemasAllocator(SchemaDroppedElementsAllocators<T, U> droppedSchemaObjectsAllocator) {
        super(droppedSchemaObjectsAllocator);

        this.schemaDroppedElementsCache = new NodeObjectCache<>(() -> new SchemaDroppedElements<T, U>(AllocationType.CACHING_ALLOCATOR));
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        super.gatherStatistics(statisticsGatherer);

        statisticsGatherer.addNodeObjectCache("schemaDroppedElementsCache", Classes.genericClass(SchemaDroppedElements.class), schemaDroppedElementsCache);
    }

    @Override
    public SchemaDroppedElements<T, U> allocateSchemaDroppedElements() {

        return schemaDroppedElementsCache.allocate();
    }

    @Override
    public void freeSchemaDroppedElements(SchemaDroppedElements<T, U> schemaDroppedElements) {

        Objects.requireNonNull(schemaDroppedElements);

        schemaDroppedElementsCache.free(schemaDroppedElements);
    }
}
