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

abstract class CompleteSchemaMaps<T extends ISchemaMap<? extends SchemaObject>> extends BaseSchemaMaps<T> implements ICompleteSchemaMaps {

    @SuppressWarnings("unchecked")
    CompleteSchemaMaps(AllocationType allocationType, IntFunction<T[]> createSchemaMapsArray) {
        super(allocationType, m -> (T)m, createSchemaMapsArray, m -> m);
    }

    @SuppressWarnings("unchecked")
    CompleteSchemaMaps(AllocationType allocationType, IntFunction<T[]> createSchemaMapsArray, ISchemaMap<Table> tables, ISchemaMap<View> views, ISchemaMap<Index> indices,
            ISchemaMap<Trigger> triggers, ISchemaMap<DBFunction> functions, ISchemaMap<Procedure> procedures) {
        super(allocationType, m -> (T)m, createSchemaMapsArray, m -> m, tables, views, indices, triggers, functions, procedures);
    }
}
