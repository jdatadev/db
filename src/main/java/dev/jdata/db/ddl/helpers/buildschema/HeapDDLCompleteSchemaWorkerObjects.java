package dev.jdata.db.ddl.helpers.buildschema;

import dev.jdata.db.schema.model.schemamaps.HeapSchemaMapBuilderAllocators;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMapsBuilder;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

@Deprecated // currently not in use
public final class HeapDDLCompleteSchemaWorkerObjects extends DDLCompleteSchemaWorkerObjects<IHeapAllCompleteSchemaMapsBuilder> {

    public HeapDDLCompleteSchemaWorkerObjects() {
        super(HeapSchemaMapBuilderAllocators.INSTANCE, () -> IHeapAllCompleteSchemaMapsBuilder.create(AllocationType.HEAP_ALLOCATOR), IHeapAllCompleteSchemaMapsBuilder[]::new);
    }
}
