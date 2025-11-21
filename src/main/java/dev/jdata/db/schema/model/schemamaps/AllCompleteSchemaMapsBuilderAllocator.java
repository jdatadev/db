package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.objects.SchemaObject;

public abstract class AllCompleteSchemaMapsBuilderAllocator<

                T extends IAllCompleteSchemaMaps,
                U extends IAllCompleteSchemaMaps & IHeapSchemaMapsMarker,
                V extends IAllCompleteSchemaMapsBuilder<T, U, V>>

        extends CompleteSchemaMapsBuilderAllocator<SchemaObject, T, U, V>
        implements IAllCompleteSchemaMapsBuilderAllocator<T, U, V> {

}
