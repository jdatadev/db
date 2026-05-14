package dev.jdata.db.schema.model.diff.schemamap;

import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjects;

final class HeapDiffSchemaMap<T extends SchemaObject> extends DiffSchemaMap<IHeapSchemaObjects<T>> implements IHeapDiffSchemaMap {

    private static final HeapDiffSchemaMap<?> emptySchemaMap = new HeapDiffSchemaMap<>(AllocationType.HEAP, IHeapSchemaObjects.empty(), IHeapSchemaObjects.empty(),
            IHeapSchemaObjects.empty(), IHeapSchemaObjects.empty(), IHeapSchemaObjects.empty(), IHeapSchemaObjects.empty());

    static HeapDiffSchemaMap<?> empty() {

        return emptySchemaMap;
    }

    HeapDiffSchemaMap(AllocationType allocationType, IHeapSchemaObjects<Table> tables, IHeapSchemaObjects<View> views, IHeapSchemaObjects<Index> indices,
            IHeapSchemaObjects<Trigger> triggers, IHeapSchemaObjects<DBFunction> functions, IHeapSchemaObjects<Procedure> procedures) {
        super(allocationType, IHeapSchemaObjects[]::new, tables, views, indices, triggers, functions, procedures);
    }
}
