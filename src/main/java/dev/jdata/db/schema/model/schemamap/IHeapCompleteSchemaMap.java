package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjects;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapCompleteSchemaMap extends ICompleteSchemaMap, IHeapSchemaMapMarker {

    public static IHeapCompleteSchemaMap empty() {

        return HeapCompleteSchemaMap.empty();
    }

    public static IHeapCompleteSchemaMap of(IHeapSchemaObjects<Table> tables, IHeapSchemaObjects<View> views, IHeapSchemaObjects<Index> indices,
            IHeapSchemaObjects<Trigger> triggers, IHeapSchemaObjects<DBFunction> functions, IHeapSchemaObjects<Procedure> procedures) {

        return new HeapCompleteSchemaMap(AllocationType.HEAP, tables, views, indices, triggers, functions, procedures);
    }
}
