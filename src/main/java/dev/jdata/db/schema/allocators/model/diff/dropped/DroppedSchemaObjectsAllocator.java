package dev.jdata.db.schema.allocators.model.diff.dropped;

import java.util.Objects;

import dev.jdata.db.utils.adt.sets.MutableIntMaxDistanceNonBucketSet;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.allocators.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.IIntToObjectMapAllocator;

public abstract class DroppedSchemaObjectsAllocator implements IDroppedElementsAllocator, IAllocators {

    private final IMutableIntSetAllocator intSetAllocator;
    private final IIntToObjectMapAllocator<MutableIntMaxDistanceNonBucketSet> intToObjectMapAllocator;

    protected DroppedSchemaObjectsAllocator(IMutableIntSetAllocator intSetAllocator, IIntToObjectMapAllocator<MutableIntMaxDistanceNonBucketSet> intToObjectMapAllocator) {

        this.intSetAllocator = Objects.requireNonNull(intSetAllocator);
        this.intToObjectMapAllocator = Objects.requireNonNull(intToObjectMapAllocator);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addInstanceAllocator("intSetAllocator", RefType.INSTANTIATED, MutableIntMaxDistanceNonBucketSet.class, intSetAllocator);
        statisticsGatherer.addInstanceAllocator("intToObjectMapAllocator", RefType.INSTANTIATED, MutableIntMaxDistanceNonBucketSet.class, intToObjectMapAllocator);
    }

    @Override
    public final IMutableIntSetAllocator getIntSetAllocator() {

        return intSetAllocator;
    }

    @Override
    public final IIntToObjectMapAllocator<MutableIntMaxDistanceNonBucketSet> getIntToObjectMapAllocator() {

        return intToObjectMapAllocator;
    }
}
