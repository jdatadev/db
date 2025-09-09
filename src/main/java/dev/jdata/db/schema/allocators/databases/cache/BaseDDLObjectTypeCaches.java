package dev.jdata.db.schema.allocators.databases.cache;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.schema.DDLObjectTypeInstances;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IObjectCache;

abstract class BaseDDLObjectTypeCaches<T, U extends IObjectCache<T>> extends DDLObjectTypeInstances<U> implements IAllocators {

    abstract Class<T> getCachedObjectClass();

    BaseDDLObjectTypeCaches(IntFunction<U[]> createArray, BiFunction<DDLObjectType, Function<DDLObjectType, U>, U> createElement) {
        super(createArray, createElement);
    }

    @Override
    public final void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        final Class<T> cachedObjectClass = getCachedObjectClass();

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            getObjectCache(ddlObjectType).addCache(statisticsGatherer, ddlObjectType.name(), cachedObjectClass);
        }
    }

    final U getObjectCache(DDLObjectType ddlObjectType) {

        return getInstance(ddlObjectType);
    }
}
