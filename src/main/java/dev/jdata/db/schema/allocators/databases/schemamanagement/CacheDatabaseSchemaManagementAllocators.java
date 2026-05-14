package dev.jdata.db.schema.allocators.databases.schemamanagement;

import java.util.Objects;

import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElements;
import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElementsAllocators;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMapBuilderAllocator;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.classes.Classes;

public final class CacheDatabaseSchemaManagementAllocators<T extends IMutableIntSet, U extends IMutableIntToObjectWithRemoveStaticMap<T>>

        extends DatabaseSchemaManagementAllocators<T, U, IHeapCompleteSchemaMap, IHeapCompleteSchemaMap, IHeapCompleteSchemaMapBuilder> {

    private final NodeObjectCache<SchemaDroppedElements<T, U>> schemaDroppedElementsCache;

    public CacheDatabaseSchemaManagementAllocators(SchemaDroppedElementsAllocators<T, U> schemaDroppedElementsAllocators) {
        super(schemaDroppedElementsAllocators, IHeapCompleteSchemaMapBuilderAllocator.create());

        this.schemaDroppedElementsCache = new NodeObjectCache<>(() -> new SchemaDroppedElements<>(AllocationType.CACHING_ALLOCATOR));
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
