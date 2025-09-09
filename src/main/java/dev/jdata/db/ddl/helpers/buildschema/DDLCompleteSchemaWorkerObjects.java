package dev.jdata.db.ddl.helpers.buildschema;

import java.util.function.Supplier;

import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocators;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMapsBuilder;
import dev.jdata.db.utils.allocators.NodeObjectCache;

@Deprecated
class DDLCompleteSchemaWorkerObjects<T extends CompleteSchemaMapsBuilder<?, ?, ?, ?, ?, ?, ?>> extends DDLSchemaWorkerObjects<T> {

    private final NodeObjectCache<DDLCompleteSchemaParameter<T, ?>> ddlEffectiveSchemaParameterCache;

    public DDLCompleteSchemaWorkerObjects(SchemaMapBuilderAllocators schemaMapBuilderAllocators, Supplier<T> createSchemaMapBuilders) {
        super(schemaMapBuilderAllocators, createSchemaMapBuilders);

        this.ddlEffectiveSchemaParameterCache = new NodeObjectCache<>(DDLCompleteSchemaParameter::new);
    }

    public final DDLCompleteSchemaParameter<T, ?> allocateDDLCompleteSchemaParameter() {

        return ddlEffectiveSchemaParameterCache.allocate();
    }

    public final void freeDDLEffectiveSchemaParameter(DDLCompleteSchemaParameter<T, ?> ddlEffectiveSchemaParameter) {

        ddlEffectiveSchemaParameterCache.free(ddlEffectiveSchemaParameter);
    }
}
