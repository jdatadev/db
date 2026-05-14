package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.SchemaObject;

public interface ICompleteSchemaMapBuilderAllocator<

                T extends ICompleteSchemaMap,
                U extends ICompleteSchemaMap & IHeapSchemaMapMarker,
                V extends ICompleteSchemaMapBuilder<T, U, V>>

        extends INonDiffSchemaMapBuilderAllocator<SchemaObject, T, V> {
}
