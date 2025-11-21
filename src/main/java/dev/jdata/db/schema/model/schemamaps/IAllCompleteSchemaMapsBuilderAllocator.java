package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.objects.SchemaObject;

public interface IAllCompleteSchemaMapsBuilderAllocator<

                T extends IAllCompleteSchemaMaps,
                U extends IAllCompleteSchemaMaps & IHeapSchemaMapsMarker,
                V extends IAllCompleteSchemaMapsBuilder<T, U, V>>

        extends ICompleteSchemaMapsBuilderAllocator<SchemaObject, T, V> {
}
