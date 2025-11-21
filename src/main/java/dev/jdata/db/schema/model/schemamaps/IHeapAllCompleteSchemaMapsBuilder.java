package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.utils.allocators.Allocatable.AllocationMechanism;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapAllCompleteSchemaMapsBuilder

        extends IAllCompleteSchemaMapsBuilder<IHeapAllCompleteSchemaMaps, IHeapAllCompleteSchemaMaps, IHeapAllCompleteSchemaMapsBuilder> {

    public static IHeapAllCompleteSchemaMapsBuilder create() {

        return create(AllocationType.HEAP);
    }

    public static IHeapAllCompleteSchemaMapsBuilder create(AllocationType allocationType) {

        AllocationType.checkIsAllocationMechanism(allocationType, AllocationMechanism.HEAP);

        return new HeapAllSimpleCompleteSchemaMapsBuilder(allocationType);
    }
}
