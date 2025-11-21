package dev.jdata.db.schema.allocators.model.diff.dropped;

import java.util.Objects;

import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IBaseMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.allocators.IIntToObjectMapAllocator;

public abstract class DroppedSchemaObjectsAllocator implements IDroppedElementsAllocator, IAllocators {

    private final IBaseMutableIntSetAllocator intSetAllocator;
    private final IIntToObjectMapAllocator<IMutableIntSet> intToObjectMapAllocator;

    protected DroppedSchemaObjectsAllocator(IBaseMutableIntSetAllocator intSetAllocator, IIntToObjectMapAllocator<IMutableIntSet> intToObjectMapAllocator) {

        this.intSetAllocator = Objects.requireNonNull(intSetAllocator);
        this.intToObjectMapAllocator = Objects.requireNonNull(intToObjectMapAllocator);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addInstanceAllocator("intSetAllocator", RefType.INSTANTIATED, IBaseMutableIntSetAllocator.class, intSetAllocator);
        statisticsGatherer.addInstanceAllocator("intToObjectMapAllocator", RefType.INSTANTIATED, IIntToObjectMapAllocator.class, intToObjectMapAllocator);
    }

    @Override
    public final IBaseMutableIntSetAllocator getMutableIntSetAllocator() {

        return intSetAllocator;
    }

    @Override
    public final IIntToObjectMapAllocator<IMutableIntSet> getIntToObjectMapAllocator() {

        return intToObjectMapAllocator;
    }
}
