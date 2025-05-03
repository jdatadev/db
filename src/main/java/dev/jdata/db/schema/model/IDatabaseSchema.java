package dev.jdata.db.schema.model;

import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.objects.DDLObjectType;

public interface IDatabaseSchema extends IDatabaseSchemaObjects {

    DatabaseSchemaVersion getVersion();

    boolean containsSchemaObjectName(DDLObjectType ddlObjectType, long hashObjectName);
}
