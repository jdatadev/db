package dev.jdata.db.schema.model.schemamaps;
import dev.jdata.db.schema.model.HeapSchemaMap;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;

public final class HeapCompleteSchemaMaps extends CompleteSchemaMaps<HeapSchemaMap<?>> {

    public static final class HeapCompleteSchemaMapsBuilder

            extends CompleteSchemaMapsBuilder<HeapSchemaMap<?>, HeapCompleteSchemaMaps, HeapCompleteSchemaMapsBuilder> {

        public static final HeapCompleteSchemaMapsBuilder INSTANCE = new HeapCompleteSchemaMapsBuilder();

        private HeapCompleteSchemaMapsBuilder() {
            super(HeapSchemaMap[]::new);
        }

        @Override
        public HeapCompleteSchemaMaps build() {

            return new HeapCompleteSchemaMaps(mapOrEmpty(DDLObjectType.TABLE), mapOrEmpty(DDLObjectType.VIEW), mapOrEmpty(DDLObjectType.INDEX),
                    mapOrEmpty(DDLObjectType.TRIGGER), mapOrEmpty(DDLObjectType.FUNCTION), mapOrEmpty(DDLObjectType.PROCEDURE));
        }

        @Override
        protected HeapSchemaMap<?> makeEmptySchema() {

            return HeapSchemaMap.empty();
        }
    }

    private static final HeapCompleteSchemaMaps emptySchemaMaps = new HeapCompleteSchemaMaps(HeapSchemaMap.empty(), HeapSchemaMap.empty(), HeapSchemaMap.empty(),
            HeapSchemaMap.empty(), HeapSchemaMap.empty(), HeapSchemaMap.empty());

    public static HeapCompleteSchemaMaps empty() {

        return emptySchemaMaps;
    }

    public HeapCompleteSchemaMaps(HeapSchemaMap<Table> tables, HeapSchemaMap<View> views, HeapSchemaMap<Index> indices, HeapSchemaMap<Trigger> triggers,
            HeapSchemaMap<DBFunction> functions, HeapSchemaMap<Procedure> procedures) {
        super(HeapSchemaMap[]::new, tables, views, indices, triggers, functions, procedures);
    }
}
