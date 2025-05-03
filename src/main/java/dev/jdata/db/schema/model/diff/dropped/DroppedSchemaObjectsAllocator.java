package dev.jdata.db.schema.model.diff.dropped;

import java.util.Objects;

import dev.jdata.db.schema.model.diff.dropped.DroppedSchemaObjects.IDroppedSchemaObjectsAllocator;
import dev.jdata.db.utils.adt.maps.MutableIntToObjectWithRemoveNonBucketMap;
import dev.jdata.db.utils.adt.sets.MutableIntBucketSet;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IIntSetAllocator;
import dev.jdata.db.utils.allocators.IIntToObjectMapAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;

public final class DroppedSchemaObjectsAllocator implements IDroppedSchemaObjectsAllocator, IAllocators {

    private final IIntSetAllocator intSetAllocator;
    private final IIntToObjectMapAllocator<MutableIntBucketSet> intToObjectMapAllocator;

    private final NodeObjectCache<DroppedElements> droppedElementsCache;

    public DroppedSchemaObjectsAllocator(IIntSetAllocator intSetAllocator, IIntToObjectMapAllocator<MutableIntBucketSet> intToObjectMapAllocator) {

        this.intSetAllocator = Objects.requireNonNull(intSetAllocator);
        this.intToObjectMapAllocator = Objects.requireNonNull(intToObjectMapAllocator);

        this.droppedElementsCache = new NodeObjectCache<>(DroppedElements::new);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addNodeObjectCache("droppedElementsCache", DroppedElements.class, droppedElementsCache);
    }

    @Override
    public MutableIntBucketSet allocateIntSet(int minimumCapacityExponent) {

        return intSetAllocator.allocateIntSet(minimumCapacityExponent);
    }

    @Override
    public void freeIntSet(MutableIntBucketSet intSet) {

        intSetAllocator.freeIntSet(intSet);
    }

    @Override
    public MutableIntToObjectWithRemoveNonBucketMap<MutableIntBucketSet> allocateIntToObjectMap(int minimumCapacityExponent) {

        return intToObjectMapAllocator.allocateIntToObjectMap(minimumCapacityExponent);
    }

    @Override
    public void freeIntToObjectMap(MutableIntToObjectWithRemoveNonBucketMap<MutableIntBucketSet> intToObjectMap) {

        intToObjectMapAllocator.freeIntToObjectMap(intToObjectMap);
    }

    @Override
    public DroppedElements allocateDroppedElements() {

        return droppedElementsCache.allocate();
    }

    @Override
    public void freeDroppedElements(DroppedElements droppedElements) {

        droppedElementsCache.free(droppedElements);
    }
}
