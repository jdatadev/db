package dev.jdata.db.schema.model.diff.schemamaps;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemamaps.BaseSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.BaseSimpleSchemaMapsBuilder;

public abstract class DiffSchemaMaps<T extends SchemaMap<?, ?, ?>> extends BaseSchemaMaps<T> {

    public static abstract class SimpleDiffSchemaMapsBuilderAllocator<T extends SchemaMap<?, ?, ?>, U extends SimpleDiffSchemaMapsBuilder<T, V, U>, V extends DiffSchemaMaps<T>> {

        abstract U allocateDiffSchemaMapsBuilder();

        abstract void freeDiffSchemaMapsBuilder(U builder);
    }

    public static <T extends SchemaMap<?, ?, ?>, U extends DiffSchemaMaps<T>, V extends SimpleDiffSchemaMapsBuilder<T, U, V>>
    V createBuilder(SimpleDiffSchemaMapsBuilderAllocator<T, V, U> allocator) {

        return allocator.allocateDiffSchemaMapsBuilder();
    }

    public static abstract class SimpleDiffSchemaMapsBuilder<T extends SchemaMap<?, ?, ?>, U extends DiffSchemaMaps<T>, V extends SimpleDiffSchemaMapsBuilder<T, U, V>>

            extends BaseSimpleSchemaMapsBuilder<T, V> {

        SimpleDiffSchemaMapsBuilder(IntFunction<T[]> createSchemaMapsArray) {
            super(createSchemaMapsArray);

            initialize();
        }

        final void initialize() {

            checkIsNotAllocated();
        }
    }

    @SuppressWarnings("unchecked")
    DiffSchemaMaps(IntFunction<T[]> createSchemaMapsArray, SchemaMap<Table, ?, ?> tables, SchemaMap<View, ?, ?> views, SchemaMap<Index, ?, ?> indices,
            SchemaMap<Trigger, ?, ?> triggers, SchemaMap<DBFunction, ?, ?> functions, SchemaMap<Procedure, ?, ?> procedures) {

        super(m -> (T)m, createSchemaMapsArray, m -> m, tables, views, indices, triggers, functions, procedures);
    }
}
