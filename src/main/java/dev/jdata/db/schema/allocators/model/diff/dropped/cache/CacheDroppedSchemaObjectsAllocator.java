package dev.jdata.db.schema.allocators.model.diff.dropped.cache;

import java.util.Objects;

import dev.jdata.db.schema.allocators.model.diff.dropped.DroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.model.diff.dropped.BaseDroppedElements;
import dev.jdata.db.schema.model.diff.dropped.DroppedElements;
import dev.jdata.db.utils.adt.sets.MutableIntMaxDistanceNonBucketSet;
import dev.jdata.db.utils.allocators.IIntToObjectMapAllocator;
import dev.jdata.db.utils.allocators.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;

public final class CacheDroppedSchemaObjectsAllocator extends DroppedSchemaObjectsAllocator {

    private final NodeObjectCache<DroppedElements> droppedElementsCache;

    public CacheDroppedSchemaObjectsAllocator(IMutableIntSetAllocator<MutableIntMaxDistanceNonBucketSet> intSetAllocator,
            IIntToObjectMapAllocator<MutableIntMaxDistanceNonBucketSet> intToObjectMapAllocator) {
        super(intSetAllocator, intToObjectMapAllocator);

        this.droppedElementsCache = new NodeObjectCache<>(DroppedElements::new);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        super.gatherStatistics(statisticsGatherer);

        statisticsGatherer.addNodeObjectCacheForGenericType("droppedElementsCache", BaseDroppedElements.class, droppedElementsCache);
    }

    @Override
    public DroppedElements allocateDroppedElements() {

        return droppedElementsCache.allocate();
    }

    @Override
    public void freeDroppedElements(DroppedElements droppedElements) {

        Objects.requireNonNull(droppedElements);

        droppedElementsCache.free(droppedElements);
    }
}
