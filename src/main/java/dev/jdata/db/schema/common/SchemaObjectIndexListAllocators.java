package dev.jdata.db.schema.common;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.schema.DDLObjectTypeInstances;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

public abstract class SchemaObjectIndexListAllocators<T extends IIndexListAllocator<? extends SchemaObject, ?, ?, ?>>

        extends DDLObjectTypeInstances<T>
        implements IAllocators {

    protected SchemaObjectIndexListAllocators(IntFunction<T[]> createIndexAllocatorArray, BiFunction<DDLObjectType, Function<DDLObjectType, T>, T> createElement) {
        super(createIndexAllocatorArray, createElement);
    }

    @Override
    public final void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            statisticsGatherer.addInstanceAllocator(ddlObjectType.name(), RefType.INSTANTIATED, ddlObjectType.getSchemaObjectType(), getInstance(ddlObjectType));
        }
    }

    public final T getIndexListAllocator(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return getInstance(ddlObjectType);
    }
}
