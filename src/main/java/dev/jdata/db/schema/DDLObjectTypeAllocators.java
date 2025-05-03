package dev.jdata.db.schema;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

public abstract class DDLObjectTypeAllocators<T extends IAllocators> extends DDLObjectTypeInstances<T> implements IAllocators {

    protected abstract Class<T> getCachedObjectClass();

    protected DDLObjectTypeAllocators(IntFunction<T[]> createArray, BiFunction<DDLObjectType, Function<DDLObjectType, T>, T> createElement) {
        super(createArray, createElement);
    }

    @Override
    public final void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        final Class<T> cachedObjectClass = getCachedObjectClass();

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            statisticsGatherer.addAllocators(ddlObjectType.name(), RefType.INSTANTIATED, cachedObjectClass, getInstance(ddlObjectType));
        }
    }
}
