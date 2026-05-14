package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.utils.adt.contains.IContains;
import dev.jdata.db.utils.adt.mutability.IInstanceBuilder;

public interface ISchemaMapBuilder<

                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_MAP extends ISchemaMap,
                HEAP_SCHEMA_MAP extends ISchemaMap & IHeapSchemaMapMarker,
                SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, HEAP_SCHEMA_MAP, SCHEMA_MAP_BUILDER>>

        extends IInstanceBuilder<SCHEMA_MAP, HEAP_SCHEMA_MAP>, ISchemaMapBuilders<SCHEMA_OBJECT, SCHEMA_MAP_BUILDER>, IContains {

    SCHEMA_MAP_BUILDER setTables(ISchemaObjects<Table> tables);
    SCHEMA_MAP_BUILDER setViews(ISchemaObjects<View> views);
    SCHEMA_MAP_BUILDER setIndices(ISchemaObjects<Index> indices);
    SCHEMA_MAP_BUILDER setTriggers(ISchemaObjects<Trigger> triggers);
    SCHEMA_MAP_BUILDER setFunctions(ISchemaObjects<DBFunction> functions);
    SCHEMA_MAP_BUILDER setProcedures(ISchemaObjects<Procedure> procedures);

    SCHEMA_MAP_BUILDER setSchemaObjects(DDLObjectType ddlObjectType, ISchemaObjects<?> schemaObjects);
}
