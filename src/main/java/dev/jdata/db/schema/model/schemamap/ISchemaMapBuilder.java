package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.ISchemaObjectsByObjectType;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.utils.adt.contains.IContains;
import dev.jdata.db.utils.adt.mutability.IInstanceBuilder;

public interface ISchemaMapBuilder<

                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_MAP extends ISchemaMap,
                HEAP_SCHEMA_MAP extends ISchemaMap & IHeapSchemaMapMarker,
                SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, HEAP_SCHEMA_MAP, SCHEMA_MAP_BUILDER>>

        extends IInstanceBuilder<SCHEMA_MAP, HEAP_SCHEMA_MAP>, IContains {

    SCHEMA_MAP_BUILDER addSchemaObject(DDLObjectType ddlObjectType, SCHEMA_OBJECT schemaObject);
    SCHEMA_MAP_BUILDER addSchemaObjects(ISchemaObjectsByObjectType schemaObjects);

    SCHEMA_MAP_BUILDER addSchemaObjects(DDLObjectType ddlObjectType, ISchemaObjects<SCHEMA_OBJECT> schemaObjects);
}
