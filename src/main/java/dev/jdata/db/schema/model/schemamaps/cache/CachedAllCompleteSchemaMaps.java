package dev.jdata.db.schema.model.schemamaps.cache;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.cache.CachedSchemaMap;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemamaps.AllCompleteSchemaMaps;

public final class CachedAllCompleteSchemaMaps extends AllCompleteSchemaMaps<CachedSchemaMap<SchemaObject>> {

    private static final CachedAllCompleteSchemaMaps emptySchemaMaps = new CachedAllCompleteSchemaMaps(CachedSchemaMap.empty(), CachedSchemaMap.empty(),
            CachedSchemaMap.empty(), CachedSchemaMap.empty(), CachedSchemaMap.empty(), CachedSchemaMap.empty());

    public static CachedAllCompleteSchemaMaps empty() {

        return emptySchemaMaps;
    }

    CachedAllCompleteSchemaMaps(CachedSchemaMap<Table> tables, CachedSchemaMap<View> views, CachedSchemaMap<Index> indices, CachedSchemaMap<Trigger> triggers,
            CachedSchemaMap<DBFunction> functions, CachedSchemaMap<Procedure> procedures) {
        super(CachedSchemaMap[]::new, tables, views, indices, triggers, functions, procedures);
    }

    CachedAllCompleteSchemaMaps() {
        super(CachedSchemaMap[]::new);
    }

    final void initializeSchemaMaps(SchemaMap<Table, ?, ?> tables, SchemaMap<View, ?, ?> views, SchemaMap<Index, ?, ?> indices, SchemaMap<Trigger, ?, ?> triggers,
            SchemaMap<DBFunction, ?, ?> functions, SchemaMap<Procedure, ?, ?> procedures) {

        initialize(tables, views, indices, triggers, functions, procedures);
    }
}
