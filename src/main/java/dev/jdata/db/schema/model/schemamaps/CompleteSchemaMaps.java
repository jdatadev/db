package dev.jdata.db.schema.model.schemamaps;

import java.util.function.IntFunction;

import dev.jdata.db.schema.allocators.model.schemamaps.ICompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;

public abstract class CompleteSchemaMaps<T extends SchemaMap<? extends SchemaObject, ?, ?>> extends BaseSchemaMaps<T> implements ICompleteSchemaMaps {

    public static <T extends SchemaMap<?, ?, ?>, U extends CompleteSchemaMaps<T>, V extends SimpleCompleteSchemaMapsBuilder<T, U, V>>
    V createBuilder(ICompleteSchemaMapsBuilderAllocator<U, V> allocator) {

        return allocator.allocateCompleteSchemaMapsBuilder();
    }

    @SuppressWarnings("unchecked")
    CompleteSchemaMaps(IntFunction<T[]> createSchemaMapsArray) {
        super(m -> (T)m, createSchemaMapsArray, m -> m);
    }

    @SuppressWarnings("unchecked")
    CompleteSchemaMaps(IntFunction<T[]> createSchemaMapsArray, SchemaMap<Table, ?, ?> tables, SchemaMap<View, ?, ?> views, SchemaMap<Index, ?, ?> indices,
            SchemaMap<Trigger, ?, ?> triggers, SchemaMap<DBFunction, ?, ?> functions, SchemaMap<Procedure, ?, ?> procedures) {
        super(m -> (T)m, createSchemaMapsArray, m -> m, tables, views, indices, triggers, functions, procedures);
    }
}
