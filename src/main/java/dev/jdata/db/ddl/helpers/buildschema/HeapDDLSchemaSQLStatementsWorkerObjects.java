package dev.jdata.db.ddl.helpers.buildschema;

import dev.jdata.db.schema.model.HeapSchemaMap;
import dev.jdata.db.schema.model.HeapSchemaMap.HeapSchemaMapBuilder;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.HeapAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.HeapCompleteSchemaMapsBuilder;
import dev.jdata.db.utils.adt.lists.HeapIndexList;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListBuilder;

public final class HeapDDLSchemaSQLStatementsWorkerObjects extends DDLSchemaSQLStatementsWorkerObjects<

                HeapIndexList<SchemaObject>,
                HeapIndexListBuilder<SchemaObject>,
                HeapIndexListAllocator<SchemaObject>,
                HeapSchemaMap<SchemaObject>,
                HeapSchemaMapBuilder<SchemaObject>,
                HeapAllCompleteSchemaMaps,
                HeapAllCompleteSchemaMaps,
                HeapCompleteSchemaMapsBuilder> {

    @Override
    HeapCompleteSchemaMapsBuilder createCompleteSchemaMapsBuilder() {

        return new HeapCompleteSchemaMapsBuilder();
    }
}
