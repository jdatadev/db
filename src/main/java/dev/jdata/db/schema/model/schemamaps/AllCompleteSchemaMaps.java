package dev.jdata.db.schema.model.schemamaps;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;

abstract class AllCompleteSchemaMaps<T extends ISchemaMap<? extends SchemaObject>> extends CompleteSchemaMaps<T> implements IAllCompleteSchemaMaps {

    AllCompleteSchemaMaps(AllocationType allocationType, IntFunction<T[]> createSchemaMapsArray) {
        super(allocationType, createSchemaMapsArray);
    }

    AllCompleteSchemaMaps(AllocationType allocationType, IntFunction<T[]> createSchemaMapsArray, ISchemaMap<Table>tables, ISchemaMap<View> views,
            ISchemaMap<Index> indices, ISchemaMap<Trigger> triggers, ISchemaMap<DBFunction> functions, ISchemaMap<Procedure> procedures) {
        super(allocationType, createSchemaMapsArray, tables, views, indices, triggers, functions, procedures);
    }
}
