package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapAllCompleteSchemaMapsBuilder

        extends IAllCompleteSchemaMapsBuilder<IHeapAllCompleteSchemaMaps, IHeapAllCompleteSchemaMaps, IHeapAllCompleteSchemaMapsBuilder> {

    public static IHeapAllCompleteSchemaMapsBuilder create() {

        return new HeapAllSimpleCompleteSchemaMapsBuilder(AllocationType.HEAP);
    }
}
