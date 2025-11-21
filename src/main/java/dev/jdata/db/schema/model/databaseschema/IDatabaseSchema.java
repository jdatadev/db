package dev.jdata.db.schema.model.databaseschema;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.schema.model.objects.DDLObjectType;

public interface IDatabaseSchema extends IBaseDatabaseSchema  {

    boolean containsSchemaObjectName(DDLObjectType ddlObjectType, long hashObjectName);

    boolean isEqualTo(StringResolver thisStringResolver, IDatabaseSchema other, StringResolver otherStringResolver);
}
