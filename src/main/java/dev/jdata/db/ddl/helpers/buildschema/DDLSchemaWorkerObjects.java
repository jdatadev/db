package dev.jdata.db.ddl.helpers.buildschema;

import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.ISchemaMapBuilderAllocators;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.ObjectCache;

@Deprecated
class DDLSchemaWorkerObjects<T extends ICompleteSchemaMapsBuilder<?, ?, ?, ?>> implements IAllocators {

    private final ISchemaMapBuilderAllocators schemaMapBuilderAllocators;

    private final ObjectCache<T> completeSchemaMapsBuilderCache;

    DDLSchemaWorkerObjects(ISchemaMapBuilderAllocators schemaMapBuilderAllocators, Supplier<T> createSchemaMapBuilders, IntFunction<T[]> createSchemaMapBuildersArray) {

        Objects.requireNonNull(schemaMapBuilderAllocators);
        Objects.requireNonNull(createSchemaMapBuilders);
        Objects.requireNonNull(createSchemaMapBuildersArray);

        this.schemaMapBuilderAllocators = Objects.requireNonNull(schemaMapBuilderAllocators);

        this.completeSchemaMapsBuilderCache = new ObjectCache<>(createSchemaMapBuilders, createSchemaMapBuildersArray);
    }

    @Override
    public final void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        throw new UnsupportedOperationException();
    }

    public final T allocateCompleteSchemaMapBuilders() {

        final T result =  completeSchemaMapsBuilderCache.allocate();
/*
        result.initialize(schemaMapBuilderAllocators);

        return result;
*/
        throw new UnsupportedOperationException();
    }

    public final void freeCompleteSchemaMapBuilders(T schemaMapBuilders) {

        Objects.requireNonNull(schemaMapBuilders);

//        schemaMapBuilders.reset(schemaMapBuilderAllocators);

        completeSchemaMapsBuilderCache.free(schemaMapBuilders);

        throw new UnsupportedOperationException();
    }
}
