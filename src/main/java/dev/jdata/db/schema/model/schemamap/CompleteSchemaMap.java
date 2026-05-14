package dev.jdata.db.schema.model.schemamap;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;

abstract class CompleteSchemaMap<T extends ISchemaObjects<? extends SchemaObject>> extends NonDiffSchemaMap<T> implements ICompleteSchemaMap {

    CompleteSchemaMap(AllocationType allocationType, IntFunction<T[]> createSchemaObjectsArray, ISchemaObjects<Table>tables, ISchemaObjects<View> views,
            ISchemaObjects<Index> indices, ISchemaObjects<Trigger> triggers, ISchemaObjects<DBFunction> functions, ISchemaObjects<Procedure> procedures) {
        super(allocationType, createSchemaObjectsArray, tables, views, indices, triggers, functions, procedures);
    }
}
