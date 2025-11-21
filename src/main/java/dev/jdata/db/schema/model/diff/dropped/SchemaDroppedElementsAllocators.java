package dev.jdata.db.schema.model.diff.dropped;

import java.util.Objects;

import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMapAllocator;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

public abstract class SchemaDroppedElementsAllocators<T extends IMutableIntSet, U extends IMutableIntToObjectWithRemoveStaticMap<T>> implements IAllocators {

    private final IMutableIntSetAllocator<T> mutableIntSetAllocator;
    private final IMutableIntToObjectWithRemoveStaticMapAllocator<T, U> mutableIntToObjectMapAllocator;

    abstract DroppedElements<T, U> allocateDroppedElements();
    abstract void freeDroppedElements(DroppedElements<T, U> droppedElements);

    SchemaDroppedElementsAllocators(IMutableIntSetAllocator<T> mutableIntSetAllocator, IMutableIntToObjectWithRemoveStaticMapAllocator<T, U> mutableIntToObjectMapAllocator) {

        this.mutableIntSetAllocator = Objects.requireNonNull(mutableIntSetAllocator);
        this.mutableIntToObjectMapAllocator = Objects.requireNonNull(mutableIntToObjectMapAllocator);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addInstanceAllocator("mutableIntSetAllocator", RefType.PASSED, IMutableIntSetAllocator.class, mutableIntSetAllocator);
        statisticsGatherer.addInstanceAllocator("mutableIntToObjectMapAllocator", RefType.PASSED, IMutableIntToObjectWithRemoveStaticMapAllocator.class,
                mutableIntToObjectMapAllocator);
    }

    final IMutableIntSetAllocator<T> getMutableIntSetAllocator() {

        return mutableIntSetAllocator;
    }

    final IMutableIntToObjectWithRemoveStaticMapAllocator<T, U> getMutableIntToObjectMapAllocator() {

        return mutableIntToObjectMapAllocator;
    }
}
