package dev.jdata.db.schema.model.effective;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.model.IDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;

public interface IEffectiveDatabaseSchema extends IDatabaseSchema {

    DatabaseId getDatabaseId();

    int computeMaxId(DDLObjectType ddlObjectType, int defaultValue);
}
