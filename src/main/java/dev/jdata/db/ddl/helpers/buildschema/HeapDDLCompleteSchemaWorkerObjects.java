package dev.jdata.db.ddl.helpers.buildschema;

import dev.jdata.db.schema.allocators.model.HeapSchemaMapBuilderAllocators;
import dev.jdata.db.schema.model.schemamaps.HeapCompleteSchemaMapsBuilder;

@Deprecated // currently not in use
public final class HeapDDLCompleteSchemaWorkerObjects extends DDLCompleteSchemaWorkerObjects<HeapCompleteSchemaMapsBuilder> {

    public HeapDDLCompleteSchemaWorkerObjects() {
        super(HeapSchemaMapBuilderAllocators.INSTANCE, () -> new HeapCompleteSchemaMapsBuilder());
    }
}
