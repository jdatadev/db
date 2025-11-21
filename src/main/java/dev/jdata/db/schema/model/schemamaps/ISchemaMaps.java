package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.ISchemaObjects;
import dev.jdata.db.schema.model.objects.DDLObjectType;

public interface ISchemaMaps extends ISchemaObjects {

    boolean containsSchemaObjectName(DDLObjectType ddlObjectType, long hashObjectName);
}
