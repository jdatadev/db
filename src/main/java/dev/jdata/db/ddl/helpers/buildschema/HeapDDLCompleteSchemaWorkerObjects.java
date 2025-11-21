package dev.jdata.db.ddl.helpers.buildschema;

import dev.jdata.db.schema.model.schemamaps.HeapCompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.HeapSchemaMapBuilderAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

@Deprecated // currently not in use
public final class HeapDDLCompleteSchemaWorkerObjects extends DDLCompleteSchemaWorkerObjects<HeapCompleteSchemaMapsBuilder> {

    public HeapDDLCompleteSchemaWorkerObjects() {
        super(HeapSchemaMapBuilderAllocators.INSTANCE, () -> new HeapCompleteSchemaMapsBuilder(AllocationType.HEAP));
    }
}
