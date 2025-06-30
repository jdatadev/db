package dev.jdata.db.ddl.allocators;

import java.util.function.Supplier;

import dev.jdata.db.ddl.scratchobjects.DDLCompleteSchemaParameter;
import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocators;
import dev.jdata.db.schema.model.schemamaps.SchemaMapBuilders;
import dev.jdata.db.utils.allocators.NodeObjectCache;

public final class DDLCompleteSchemaCachedObjects<T extends SchemaMapBuilders<?, ?, ?, ?, ?, ?, ?>> extends DDLSchemaCachedObjects<T> {

    private final NodeObjectCache<DDLCompleteSchemaParameter<?>> ddlEffectiveSchemaParameterCache;

    public DDLCompleteSchemaCachedObjects(SchemaMapBuilderAllocators schemaMapBuilderAllocators, Supplier<T> createSchemaMapBuilders) {
        super(schemaMapBuilderAllocators, createSchemaMapBuilders);

        this.ddlEffectiveSchemaParameterCache = new NodeObjectCache<>(DDLCompleteSchemaParameter::new);
    }

    public DDLCompleteSchemaParameter<?> allocateDDLCompleteSchemaParameter() {

        return ddlEffectiveSchemaParameterCache.allocate();
    }

    public void freeDDLEffectiveSchemaParameter(DDLCompleteSchemaParameter<?> ddlEffectiveSchemaParameter) {

        ddlEffectiveSchemaParameterCache.free(ddlEffectiveSchemaParameter);
    }
}
