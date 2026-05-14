package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.SchemaObject;

abstract class SimpleCompleteSchemaMapBuilderAllocator<

                T extends ICompleteSchemaMap,
                U extends ICompleteSchemaMap & IHeapSchemaMapMarker,
                V extends ICompleteSchemaMapBuilder<T, U, V>>

        extends NonDiffSchemaMapBuilderAllocator<SchemaObject, T, U, V>
        implements ICompleteSchemaMapBuilderAllocator<T, U, V> {

}
