package dev.jdata.db.schema.model.diff.schemamap;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemamap.BaseSchemaMap;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;

abstract class DiffSchemaMap<T extends ISchemaObjects<?>> extends BaseSchemaMap<T> implements IDiffSchemaMap {

    @SuppressWarnings("unchecked")
    DiffSchemaMap(AllocationType allocationType, IntFunction<T[]> createSchemaMapArray, ISchemaObjects<Table> tables, ISchemaObjects<View> views, ISchemaObjects<Index> indices,
            ISchemaObjects<Trigger> triggers, ISchemaObjects<DBFunction> functions, ISchemaObjects<Procedure> procedures) {

        super(allocationType, m -> (T)m, createSchemaMapArray, m -> m, tables, views, indices, triggers, functions, procedures);
    }
}
