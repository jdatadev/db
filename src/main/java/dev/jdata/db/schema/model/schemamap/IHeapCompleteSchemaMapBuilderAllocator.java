package dev.jdata.db.schema.model.schemamap;

public interface IHeapCompleteSchemaMapBuilderAllocator

                extends ICompleteSchemaMapBuilderAllocator<IHeapCompleteSchemaMap, IHeapCompleteSchemaMap, IHeapCompleteSchemaMapBuilder> {

    static IHeapCompleteSchemaMapBuilderAllocator create() {

        return HeapSimpleCompleteSchemaMapBuilderAllocator.INSTANCE;
    }
}
