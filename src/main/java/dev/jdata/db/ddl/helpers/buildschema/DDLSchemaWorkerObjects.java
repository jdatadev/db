package dev.jdata.db.ddl.helpers.buildschema;

import java.util.Objects;
import java.util.function.Supplier;

import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.ISchemaMapBuilderAllocators;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.NodeObjectCache;

@Deprecated
class DDLSchemaWorkerObjects<T extends ICompleteSchemaMapsBuilder<?, ?, ?, ?>> implements IAllocators {

    private final ISchemaMapBuilderAllocators schemaMapBuilderAllocators;

    private final NodeObjectCache<T> completeSchemaMapsBuilderCache;

    DDLSchemaWorkerObjects(ISchemaMapBuilderAllocators schemaMapBuilderAllocators, Supplier<T> createSchemaMapBuilders) {

        Objects.requireNonNull(schemaMapBuilderAllocators);
        Objects.requireNonNull(createSchemaMapBuilders);

        this.schemaMapBuilderAllocators = Objects.requireNonNull(schemaMapBuilderAllocators);

        this.completeSchemaMapsBuilderCache = new NodeObjectCache<>(createSchemaMapBuilders);
    }

    public final T allocateCompleteSchemaMapBuilders() {

        final T result =  completeSchemaMapsBuilderCache.allocate();

        result.initialize(schemaMapBuilderAllocators);

        return result;
    }

    public final void freeCompleteSchemaMapBuilders(T schemaMapBuilders) {

        Objects.requireNonNull(schemaMapBuilders);

        schemaMapBuilders.reset(schemaMapBuilderAllocators);

        completeSchemaMapsBuilderCache.free(schemaMapBuilders);
    }
}
