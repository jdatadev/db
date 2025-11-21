package dev.jdata.db.ddl.helpers.buildschema;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMap;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMapsBuilder;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;

public final class HeapDDLSchemaSQLStatementsWorkerObjects extends DDLSchemaSQLStatementsWorkerObjects<

                IHeapIndexList<SchemaObject>,
                IHeapIndexListBuilder<SchemaObject>,
                IHeapIndexListAllocator<SchemaObject>,
                IHeapSchemaMap<SchemaObject>,
                IHeapAllCompleteSchemaMaps,
                IHeapAllCompleteSchemaMaps,
                IHeapAllCompleteSchemaMapsBuilder> {

    public HeapDDLSchemaSQLStatementsWorkerObjects() {
        super(IHeapAllCompleteSchemaMapsBuilder[]::new);
    }

    @Override
    IHeapAllCompleteSchemaMapsBuilder createCompleteSchemaMapsBuilder() {

        return IHeapAllCompleteSchemaMapsBuilder.create();
    }
}
