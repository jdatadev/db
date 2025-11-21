package dev.jdata.db.schema.model.schemamaps;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMap;

final class HeapAllCompleteSchemaMaps extends AllCompleteSchemaMaps<IHeapSchemaMap<SchemaObject>> implements IHeapAllCompleteSchemaMaps {

    private static final HeapAllCompleteSchemaMaps emptySchemaMaps = new HeapAllCompleteSchemaMaps(AllocationType.HEAP, IHeapSchemaMap.empty(), IHeapSchemaMap.empty(),
            IHeapSchemaMap.empty(), IHeapSchemaMap.empty(), IHeapSchemaMap.empty(), IHeapSchemaMap.empty());

    static HeapAllCompleteSchemaMaps empty() {

        return emptySchemaMaps;
    }

    HeapAllCompleteSchemaMaps(AllocationType allocationType, IHeapSchemaMap<Table> tables, IHeapSchemaMap<View> views, IHeapSchemaMap<Index> indices,
            IHeapSchemaMap<Trigger> triggers, IHeapSchemaMap<DBFunction> functions, IHeapSchemaMap<Procedure> procedures) {
        super(allocationType, IHeapSchemaMap[]::new, tables, views, indices, triggers, functions, procedures);
    }
}
