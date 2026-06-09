package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;

public interface ICompleteSchemaMapBuilder<

                T extends INonDiffSchemaMap,
                U extends INonDiffSchemaMap & IHeapSchemaMapMarker,
                V extends INonDiffSchemaMapBuilder<SchemaObject, T, U, V>>

        extends INonDiffSchemaMapBuilder<SchemaObject, T, U, V> {

    V addTables(ISchemaObjects<Table> tables);
    V addViews(ISchemaObjects<View> views);
    V addIndices(ISchemaObjects<Index> indices);
    V addTriggers(ISchemaObjects<Trigger> triggers);
    V addFunctions(ISchemaObjects<DBFunction> functions);
    V addProcedures(ISchemaObjects<Procedure> procedures);
}
