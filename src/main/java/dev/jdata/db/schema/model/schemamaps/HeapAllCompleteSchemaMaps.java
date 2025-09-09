package dev.jdata.db.schema.model.schemamaps;
import dev.jdata.db.schema.model.HeapSchemaMap;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;

public final class HeapAllCompleteSchemaMaps extends AllCompleteSchemaMaps<HeapSchemaMap<SchemaObject>> implements IHeapAllCompleteSchemaMaps {

    private static final HeapAllCompleteSchemaMaps emptySchemaMaps = new HeapAllCompleteSchemaMaps(HeapSchemaMap.empty(), HeapSchemaMap.empty(), HeapSchemaMap.empty(),
            HeapSchemaMap.empty(), HeapSchemaMap.empty(), HeapSchemaMap.empty());

    public static HeapAllCompleteSchemaMaps empty() {

        return emptySchemaMaps;
    }

    public HeapAllCompleteSchemaMaps(HeapSchemaMap<Table> tables, HeapSchemaMap<View> views, HeapSchemaMap<Index> indices, HeapSchemaMap<Trigger> triggers,
            HeapSchemaMap<DBFunction> functions, HeapSchemaMap<Procedure> procedures) {
        super(HeapSchemaMap[]::new, tables, views, indices, triggers, functions, procedures);
    }
}
