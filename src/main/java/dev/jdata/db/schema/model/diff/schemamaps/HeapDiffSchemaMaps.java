package dev.jdata.db.schema.model.diff.schemamaps;

import java.util.Objects;

import dev.jdata.db.schema.model.HeapSchemaMap;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;

public final class HeapDiffSchemaMaps extends DiffSchemaMaps<HeapSchemaMap<?>> {

    public static final class HeapSimpleDiffSchemaMapsBuilderAllocator

            extends SimpleDiffSchemaMapsBuilderAllocator<HeapSchemaMap<?>, HeapDiffSchemaMapsBuilder, HeapDiffSchemaMaps> {

        public static final HeapSimpleDiffSchemaMapsBuilderAllocator INSTANCE = new HeapSimpleDiffSchemaMapsBuilderAllocator();

        private HeapSimpleDiffSchemaMapsBuilderAllocator() {

        }

        @Override
        HeapDiffSchemaMapsBuilder allocateDiffSchemaMapsBuilder() {

            return new HeapDiffSchemaMapsBuilder();
        }

        @Override
        void freeDiffSchemaMapsBuilder(HeapDiffSchemaMapsBuilder builder) {

            Objects.requireNonNull(builder);
        }
    }

    public static final class HeapDiffSchemaMapsBuilder extends SimpleDiffSchemaMapsBuilder<HeapSchemaMap<?>, HeapDiffSchemaMaps, HeapDiffSchemaMapsBuilder> {

        public HeapDiffSchemaMapsBuilder() {
            super(HeapSchemaMap[]::new);
        }

        public HeapDiffSchemaMaps build() {

            checkIsAllocated();

            return new HeapDiffSchemaMaps(mapOrEmpty(DDLObjectType.TABLE), mapOrEmpty(DDLObjectType.VIEW), mapOrEmpty(DDLObjectType.INDEX), mapOrEmpty(DDLObjectType.TRIGGER),
                    mapOrEmpty(DDLObjectType.FUNCTION), mapOrEmpty(DDLObjectType.PROCEDURE));
        }

        @Override
        protected SchemaMap<?, ?, ?> makeEmptySchema() {

            return HeapSchemaMap.empty();
        }
    }

    private static final HeapDiffSchemaMaps emptySchemaMaps = new HeapDiffSchemaMaps(HeapSchemaMap.empty(), HeapSchemaMap.empty(), HeapSchemaMap.empty(), HeapSchemaMap.empty(),
            HeapSchemaMap.empty(), HeapSchemaMap.empty());

    private static HeapDiffSchemaMaps empty() {

        return emptySchemaMaps;
    }

    public HeapDiffSchemaMaps(HeapSchemaMap<Table> tables, HeapSchemaMap<View> views, HeapSchemaMap<Index> indices, HeapSchemaMap<Trigger> triggers,
            HeapSchemaMap<DBFunction> functions, HeapSchemaMap<Procedure> procedures) {
        super(HeapSchemaMap[]::new, tables, views, indices, triggers, functions, procedures);
    }
}
