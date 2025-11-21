package dev.jdata.db.schema.model.diff.dropped;

import java.util.Objects;

import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMapAllocator;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;

final class CachedSchemaDroppedElementsAllocators<T extends IMutableIntSet, U extends IMutableIntToObjectWithRemoveStaticMap<T>>

        extends SchemaDroppedElementsAllocators<T, U> {

    private final NodeObjectCache<DroppedElements<T, U>> droppedElementsCache;

    public CachedSchemaDroppedElementsAllocators(IMutableIntSetAllocator<T> intSetAllocator, IMutableIntToObjectWithRemoveStaticMapAllocator<T, U> intToObjectMapAllocator) {
        super(intSetAllocator, intToObjectMapAllocator);

        this.droppedElementsCache = new NodeObjectCache<>(DroppedElements::new);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        super.gatherStatistics(statisticsGatherer);

        statisticsGatherer.addNodeObjectCacheForGenericType("droppedElementsCache", DroppedElements.class, droppedElementsCache);
    }

    @Override
    DroppedElements<T, U> allocateDroppedElements() {

        return droppedElementsCache.allocate();
    }

    @Override
    void freeDroppedElements(DroppedElements<T, U> droppedElements) {

        Objects.requireNonNull(droppedElements);

        droppedElementsCache.free(droppedElements);
    }
}
