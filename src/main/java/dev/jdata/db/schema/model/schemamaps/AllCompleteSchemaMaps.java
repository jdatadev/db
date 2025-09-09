package dev.jdata.db.schema.model.schemamaps;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;

public abstract class AllCompleteSchemaMaps<T extends SchemaMap<? extends SchemaObject, ?, ?>> extends CompleteSchemaMaps<T> implements IAllCompleteSchemaMaps {

    protected AllCompleteSchemaMaps(IntFunction<T[]> createSchemaMapsArray, SchemaMap<Table, ?, ?> tables, SchemaMap<View, ?, ?> views, SchemaMap<Index, ?, ?> indices,
            SchemaMap<Trigger, ?, ?> triggers, SchemaMap<DBFunction, ?, ?> functions, SchemaMap<Procedure, ?, ?> procedures) {
        super(createSchemaMapsArray, tables, views, indices, triggers, functions, procedures);
    }

    protected AllCompleteSchemaMaps(IntFunction<T[]> createSchemaMapsArray) {
        super(createSchemaMapsArray);
    }
}
