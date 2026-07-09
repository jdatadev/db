package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;

public interface INonDiffSchemaMapBuilder<

                SCHEMA_OBJECT extends SchemaObject,
                NON_DIFF_SCHEMA_MAP extends INonDiffSchemaMap,
                HEAP_NON_DIFF_SCHEMA_MAP extends INonDiffSchemaMap & IHeapSchemaMapMarker,
                NON_DIFF_SCHEMA_MAP_BUILDER extends INonDiffSchemaMapBuilder<SCHEMA_OBJECT, NON_DIFF_SCHEMA_MAP, HEAP_NON_DIFF_SCHEMA_MAP, NON_DIFF_SCHEMA_MAP_BUILDER>>

        extends ISchemaMapBuilder<SCHEMA_OBJECT, NON_DIFF_SCHEMA_MAP, HEAP_NON_DIFF_SCHEMA_MAP, NON_DIFF_SCHEMA_MAP_BUILDER> {

    NON_DIFF_SCHEMA_MAP_BUILDER addTables(ISchemaObjects<Table> tables);
    NON_DIFF_SCHEMA_MAP_BUILDER addViews(ISchemaObjects<View> views);
    NON_DIFF_SCHEMA_MAP_BUILDER addIndices(ISchemaObjects<Index> indices);
    NON_DIFF_SCHEMA_MAP_BUILDER addTriggers(ISchemaObjects<Trigger> triggers);
    NON_DIFF_SCHEMA_MAP_BUILDER addFunctions(ISchemaObjects<DBFunction> functions);
    NON_DIFF_SCHEMA_MAP_BUILDER addProcedures(ISchemaObjects<Procedure> procedures);
}
