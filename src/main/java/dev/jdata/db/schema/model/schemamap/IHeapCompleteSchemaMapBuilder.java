package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.utils.allocators.Allocatable.AllocationMechanism;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapCompleteSchemaMapBuilder extends ICompleteSchemaMapBuilder<IHeapCompleteSchemaMap, IHeapCompleteSchemaMap, IHeapCompleteSchemaMapBuilder> {

    public static IHeapCompleteSchemaMapBuilder create() {

        return create(AllocationType.HEAP);
    }

    public static IHeapCompleteSchemaMapBuilder create(AllocationType allocationType) {

        AllocationType.checkIsAllocationMechanism(allocationType, AllocationMechanism.HEAP);

        return new HeapSimpleCompleteSchemaMapBuilder(allocationType);
    }
}
