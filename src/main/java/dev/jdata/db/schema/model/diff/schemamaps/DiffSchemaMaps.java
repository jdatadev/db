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

public abstract class DiffSchemaMaps<T extends SchemaMap<?, ?, ?, ?, ?>> extends BaseSchemaMaps<T> {

    public static abstract class DiffSchemaMapsBuilderAllocator<T extends SchemaMap<?, ?, ?, ?, ?>, U extends DiffSchemaMapsBuilder<T, V, U>, V extends DiffSchemaMaps<T>> {

        abstract U allocateDiffSchemaMapsBuilder();

        abstract void freeDiffSchemaMapsBuilder(U builder);
    }

    public static <T extends SchemaMap<?, ?, ?, ?, ?>, U extends DiffSchemaMaps<T>, V extends DiffSchemaMapsBuilder<T, U, V>>
    V createBuilder(DiffSchemaMapsBuilderAllocator<T, V, U> allocator) {

        return allocator.allocateDiffSchemaMapsBuilder();
    }

    public static abstract class DiffSchemaMapsBuilder<T extends SchemaMap<?, ?, ?, ?, ?>, U extends DiffSchemaMaps<T>, V extends DiffSchemaMapsBuilder<T, U, V>>

            extends BaseBuilder<T, V> {

        DiffSchemaMapsBuilder(IntFunction<T[]> createSchemaMapsArray) {
            super(createSchemaMapsArray);

            initialize();
        }

        final void initialize() {

            checkIsNotAllocated();
        }
    }

    @SuppressWarnings("unchecked")
    DiffSchemaMaps(IntFunction<T[]> createSchemaMapsArray, SchemaMap<Table, ?, ?, ?, ?> tables, SchemaMap<View, ?, ?, ?, ?> views, SchemaMap<Index, ?, ?, ?, ?> indices,
            SchemaMap<Trigger, ?, ?, ?, ?> triggers, SchemaMap<DBFunction, ?, ?, ?, ?> functions, SchemaMap<Procedure, ?, ?, ?, ?> procedures) {

        super(m -> (T)m, createSchemaMapsArray, m -> m, tables, views, indices, triggers, functions, procedures);
    }
}
