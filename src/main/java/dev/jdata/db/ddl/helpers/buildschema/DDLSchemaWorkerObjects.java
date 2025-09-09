package dev.jdata.db.ddl.helpers.buildschema;

import java.util.Objects;
import java.util.function.Supplier;

import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocators;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMapsBuilder;
import dev.jdata.db.utils.allocators.NodeObjectCache;

@Deprecated
class DDLSchemaWorkerObjects<T extends CompleteSchemaMapsBuilder<?, ?, ?, ?, ?, ?, ?>> {

    private final SchemaMapBuilderAllocators schemaMapBuilderAllocators;

    private final NodeObjectCache<T> completeSchemaMapsBuilderCache;

    DDLSchemaWorkerObjects(SchemaMapBuilderAllocators schemaMapBuilderAllocators, Supplier<T> createSchemaMapBuilders) {

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
