package dev.jdata.db.ddl.helpers.sqltoschema.complete;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMapMarker;
import dev.jdata.db.schema.model.schemamap.INonDiffSchemaMap;
import dev.jdata.db.schema.model.schemamap.INonDiffSchemaMapBuilder;

interface IDDLNonDiffSchemaMapAllocators<

                T extends INonDiffSchemaMap,
                U extends INonDiffSchemaMap & IHeapSchemaMapMarker,
                V extends INonDiffSchemaMapBuilder<SchemaObject, T, U, V>> {

    V allocateSchemaMapBuilder();
    void freeSchemaMapBuilder(V schemaMapsBuilder);
}
