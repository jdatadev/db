package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.cache.CachedSchemaMap;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;

public final class CachedCompleteSchemaMaps extends CompleteSchemaMaps<CachedSchemaMap<?>> {

    public static final class CachedCompleteSchemaBuilder extends CompleteSchemaMapsBuilder<CachedSchemaMap<?>, CachedCompleteSchemaMaps, CachedCompleteSchemaBuilder> {

        public CachedCompleteSchemaBuilder() {
            super(CachedSchemaMap[]::new);
        }

        @Override
        public CachedCompleteSchemaMaps build() {

            return new CachedCompleteSchemaMaps(mapOrEmpty(DDLObjectType.TABLE), mapOrEmpty(DDLObjectType.VIEW), mapOrEmpty(DDLObjectType.INDEX), mapOrEmpty(DDLObjectType.TRIGGER),
                    mapOrEmpty(DDLObjectType.FUNCTION), mapOrEmpty(DDLObjectType.PROCEDURE));
        }

        @Override
        protected CachedSchemaMap<?> makeEmptySchema() {

            return CachedSchemaMap.empty();
        }
    }

    private static final CachedCompleteSchemaMaps emptySchemaMaps = new CachedCompleteSchemaMaps(CachedSchemaMap.empty(), CachedSchemaMap.empty(),
            CachedSchemaMap.empty(), CachedSchemaMap.empty(), CachedSchemaMap.empty(), CachedSchemaMap.empty());

    public static CachedCompleteSchemaMaps empty() {

        return emptySchemaMaps;
    }

    CachedCompleteSchemaMaps() {
        super(CachedSchemaMap[]::new);
    }

    private CachedCompleteSchemaMaps(CachedSchemaMap<Table> tables, CachedSchemaMap<View> views, CachedSchemaMap<Index> indices,
            CachedSchemaMap<Trigger> triggers, CachedSchemaMap<DBFunction> functions, CachedSchemaMap<Procedure> procedures) {
        super(CachedSchemaMap[]::new, tables, views, indices, triggers, functions, procedures);
    }
}
