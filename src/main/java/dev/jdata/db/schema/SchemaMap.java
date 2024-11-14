package dev.jdata.db.schema;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import dev.jdata.db.DBNamedObjectMap;
import dev.jdata.db.utils.adt.lists.Lists;
import dev.jdata.db.utils.checks.Checks;

public final class SchemaMap<T extends SchemaObject> extends DBNamedObjectMap<T, SchemaMap<T>> implements SchemaMapGetters<T> {

    private final List<T> schemaObjects;

    public static <T extends SchemaObject> SchemaMap<T> of(List<T> schemaObjects) {

        Objects.requireNonNull(schemaObjects);

        return new SchemaMap<>(schemaObjects);
    }

    private SchemaMap(List<T> schemaObjects) {
        super(schemaObjects);

        this.schemaObjects = Lists.unmodifiableCopyOf(schemaObjects);
    }

    private SchemaMap(SchemaMap<T> toCopy) {
        super(toCopy);

        this.schemaObjects = toCopy.schemaObjects;
    }

    @Override
    public SchemaMap<T> makeCopy() {

        return new SchemaMap<>(this);
    }

    @Override
    public T getSchemaObjectByName(String schemaName) {

        Checks.isSchemaName(schemaName);

        return getNamedObject(schemaName);
    }

    @Override
    public T getSchemaObjectById(int id) {

        Checks.isSchemaId(id);

        return schemaObjects.get(id);
    }

    @Override
    public Iterable<String> getSchemaNames() {

        return getNames();
    }

    @Override
    public Iterable<T> getSchemaObjects() {

        return getNamedObjects();
    }

    @Override
    public Stream<T> schemaObjectsStream() {

        return namedObjectsStream();
    }
}
