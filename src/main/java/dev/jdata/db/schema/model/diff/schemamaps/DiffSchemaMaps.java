package dev.jdata.db.schema.model.diff.schemamaps;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamaps.BaseSchemaMaps;

abstract class DiffSchemaMaps<T extends ISchemaMap<?>> extends BaseSchemaMaps<T> implements IDiffSchemaMaps {

    @SuppressWarnings("unchecked")
    DiffSchemaMaps(AllocationType allocationType, IntFunction<T[]> createSchemaMapsArray, ISchemaMap<Table> tables, ISchemaMap<View> views, ISchemaMap<Index> indices,
            ISchemaMap<Trigger> triggers, ISchemaMap<DBFunction> functions, ISchemaMap<Procedure> procedures) {

        super(allocationType, m -> (T)m, createSchemaMapsArray, m -> m, tables, views, indices, triggers, functions, procedures);
    }
}
