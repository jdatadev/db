package dev.jdata.db.schema.allocators.model.diff.dropped;

import java.util.Objects;

import dev.jdata.db.schema.model.diff.dropped.BaseDroppedElements;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.MutableIntMaxDistanceNonBucketSet;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.allocators.IIntToObjectMapAllocator;
import dev.jdata.db.utils.allocators.IMutableIntSetAllocator;

public abstract class BaseDroppedSchemaObjectsAllocator<T extends IMutableIntSet, U extends BaseDroppedElements<T>> implements IDroppedElementsAllocator<T, U>, IAllocators {

    private final IMutableIntSetAllocator<T> intSetAllocator;
    private final IIntToObjectMapAllocator<T> intToObjectMapAllocator;

    BaseDroppedSchemaObjectsAllocator(IMutableIntSetAllocator<T> intSetAllocator, IIntToObjectMapAllocator<T> intToObjectMapAllocator) {

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
    public final IMutableIntSetAllocator<T> getIntSetAllocator() {

        return intSetAllocator;
    }

    @Override
    public final IIntToObjectMapAllocator<T> getIntToObjectMapAllocator() {

        return intToObjectMapAllocator;
    }
}
