package dev.jdata.db.ddl.helpers.buildschema;

import java.util.function.IntFunction;
import java.util.function.Supplier;

import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.ISchemaMapBuilderAllocators;
import dev.jdata.db.utils.allocators.NodeObjectCache;

@Deprecated
class DDLCompleteSchemaWorkerObjects<T extends ICompleteSchemaMapsBuilder<?, ?, ?, ?>> extends DDLSchemaWorkerObjects<T> {

    private final NodeObjectCache<DDLCompleteSchemaParameter<T, ?>> ddlEffectiveSchemaParameterCache;

    public DDLCompleteSchemaWorkerObjects(ISchemaMapBuilderAllocators schemaMapBuilderAllocators, Supplier<T> createSchemaMapBuilders,
            IntFunction<T[]> createSchemaMapBuildersArray) {
        super(schemaMapBuilderAllocators, createSchemaMapBuilders, createSchemaMapBuildersArray);

        this.ddlEffectiveSchemaParameterCache = new NodeObjectCache<>(DDLCompleteSchemaParameter::new);
    }

    public final DDLCompleteSchemaParameter<T, ?> allocateDDLCompleteSchemaParameter() {

        return ddlEffectiveSchemaParameterCache.allocate();
    }

    public final void freeDDLEffectiveSchemaParameter(DDLCompleteSchemaParameter<T, ?> ddlEffectiveSchemaParameter) {

        ddlEffectiveSchemaParameterCache.free(ddlEffectiveSchemaParameter);
    }
}
