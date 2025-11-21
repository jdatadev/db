package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMap;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapAllCompleteSchemaMaps extends IAllCompleteSchemaMaps, IHeapSchemaMapsMarker {

    public static IHeapAllCompleteSchemaMaps empty() {

        return HeapAllCompleteSchemaMaps.empty();
    }

    public static IHeapAllCompleteSchemaMaps of(IHeapSchemaMap<Table> tables, IHeapSchemaMap<View> views, IHeapSchemaMap<Index> indices, IHeapSchemaMap<Trigger> triggers,
            IHeapSchemaMap<DBFunction> functions, IHeapSchemaMap<Procedure> procedures) {

        return new HeapAllCompleteSchemaMaps(AllocationType.HEAP, tables, views, indices, triggers, functions, procedures);
    }
}
