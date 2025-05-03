package dev.jdata.db.schema;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.IndexList.CacheIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

final class SchemaObjectIndexListAllocators extends DDLObjectTypeInstances<CacheIndexListAllocator<? extends SchemaObject>> implements IAllocators {

    SchemaObjectIndexListAllocators() {
        super(CacheIndexListAllocator[]::new, (t, g) -> new CacheIndexListAllocator<>(t.getCreateArray()));
    }

    @Override
    public final void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            statisticsGatherer.addAllocators(ddlObjectType.name(), RefType.INSTANTIATED, getInstance(ddlObjectType));
        }
    }

    <T> IndexListAllocator<T> getIndexListAllocator(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        @SuppressWarnings("unchecked")
        final IndexListAllocator<T> result = (IndexListAllocator<T>)getInstance(ddlObjectType);

        return result;
    }
}
