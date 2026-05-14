package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.SchemaObject;

public interface ICompleteSchemaMapBuilder<

                T extends INonDiffSchemaMap,
                U extends INonDiffSchemaMap & IHeapSchemaMapMarker,
                V extends INonDiffSchemaMapBuilder<SchemaObject, T, U, V>>

        extends INonDiffSchemaMapBuilder<SchemaObject, T, U, V> {

}
