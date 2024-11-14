package dev.jdata.db.schema;

import java.util.stream.Stream;

import dev.jdata.db.DBNamedObject;

public interface SchemaMapGetters<T extends DBNamedObject> {

    Iterable<String> getSchemaNames();

    T getSchemaObjectByName(String schemaName);

    T getSchemaObjectById(int id);

    Iterable<T> getSchemaObjects();

    @Deprecated
    Stream<T> schemaObjectsStream();
}
