package dev.jdata.db.schema.model;

import dev.jdata.db.DBNamedObject;
import dev.jdata.db.utils.adt.elements.IIterableElements;
import dev.jdata.db.utils.adt.lists.IIndexList;

public interface ISchemaMap<T extends DBNamedObject> extends IIterableElements<T> {

    boolean containsSchemaObjectName(long schemaObjectName);

    T getSchemaObjectByName(long schemaObjectName);

    T getSchemaObjectById(int id);

    IIndexList<T> getSchemaObjects();
}
