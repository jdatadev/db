package dev.jdata.db.ddl.helpers.buildschema;

import dev.jdata.db.schema.allocators.model.HeapSchemaMapBuilderAllocators;
import dev.jdata.db.schema.model.schemamaps.HeapCompleteSchemaMapsBuilder;

@Deprecated // currently not in use
final class HeapDDLSchemaWorkerObjects extends DDLSchemaWorkerObjects<HeapCompleteSchemaMapsBuilder> {

    public HeapDDLSchemaWorkerObjects() {
        super(HeapSchemaMapBuilderAllocators.INSTANCE, HeapCompleteSchemaMapsBuilder::new);
    }
}
