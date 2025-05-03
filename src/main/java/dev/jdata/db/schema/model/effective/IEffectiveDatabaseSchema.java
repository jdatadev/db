package dev.jdata.db.schema.model.effective;

import dev.jdata.db.schema.model.IDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;

public interface IEffectiveDatabaseSchema extends IDatabaseSchema {

    int computeMaxId(DDLObjectType ddlObjectType, int defaultValue);
}
