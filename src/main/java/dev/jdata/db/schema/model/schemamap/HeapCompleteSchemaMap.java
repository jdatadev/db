package dev.jdata.db.schema.model.schemamap;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjects;

final class HeapCompleteSchemaMap extends CompleteSchemaMap<IHeapSchemaObjects<SchemaObject>> implements IHeapCompleteSchemaMap {

    private static final HeapCompleteSchemaMap emptySchemaMap = new HeapCompleteSchemaMap(AllocationType.HEAP, IHeapSchemaObjects.empty(), IHeapSchemaObjects.empty(),
            IHeapSchemaObjects.empty(), IHeapSchemaObjects.empty(), IHeapSchemaObjects.empty(), IHeapSchemaObjects.empty());

    static HeapCompleteSchemaMap empty() {

        return emptySchemaMap;
    }

    HeapCompleteSchemaMap(AllocationType allocationType, IHeapSchemaObjects<Table> tables, IHeapSchemaObjects<View> views, IHeapSchemaObjects<Index> indices,
            IHeapSchemaObjects<Trigger> triggers, IHeapSchemaObjects<DBFunction> functions, IHeapSchemaObjects<Procedure> procedures) {
        super(allocationType, IHeapSchemaObjects[]::new, tables, views, indices, triggers, functions, procedures);
    }
}
