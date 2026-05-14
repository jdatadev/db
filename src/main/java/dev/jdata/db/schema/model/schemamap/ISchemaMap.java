package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.ISchemaObjectsByObjectType;
import dev.jdata.db.schema.model.objects.DDLObjectType;

public interface ISchemaMap extends ISchemaObjectsByObjectType {

    boolean containsSchemaObjectName(DDLObjectType ddlObjectType, long hashObjectName);
}
