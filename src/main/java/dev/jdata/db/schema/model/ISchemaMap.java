package dev.jdata.db.schema.model;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.DBNamedObject;
import dev.jdata.db.utils.adt.elements.IObjectIterableElements;
import dev.jdata.db.utils.adt.lists.IndexList;

public interface ISchemaMap<T extends DBNamedObject> extends IObjectIterableElements<T> {

    boolean containsSchemaObjectName(long schemaObjectName);

    T getSchemaObjectByName(long schemaObjectName);

    T getSchemaObjectById(int id);

    IndexList<T> getSchemaObjects();

    boolean isEqualTo(StringResolver thisStringResolver, ISchemaMap<T> other, StringResolver otherStringResolver);
}
