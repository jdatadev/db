package dev.jdata.db.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import dev.jdata.db.DBNamedObjectMap;
import dev.jdata.db.utils.adt.lists.Lists;
import dev.jdata.db.utils.checks.Checks;

public final class SchemaMap<T extends SchemaObject> extends DBNamedObjectMap<T, SchemaMap<T>> implements SchemaMapGetters<T> {

    private final List<T> schemaObjects;

    public static <T extends SchemaObject> SchemaMap<T> empty() {

        return of(Collections.emptyList());
    }

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

    private SchemaMap(SchemaMap<T> toCopy, T additionalObject) {
        super(toCopy);

        Objects.requireNonNull(additionalObject);

        final List<T> toCopySchemaObjects = toCopy.schemaObjects;
        final int numToCopySchemaObjects = toCopySchemaObjects.size();

        final List<T> schemaObjectsCopy = new ArrayList<>(numToCopySchemaObjects + 1);

        schemaObjectsCopy.addAll(toCopySchemaObjects);
        schemaObjectsCopy.add(additionalObject);

        this.schemaObjects = Lists.unmodifiableOf(schemaObjectsCopy);
    }

    @Override
    public SchemaMap<T> makeCopy() {

        return new SchemaMap<>(this);
    }

    @Override
    public boolean containsSchemaObjectName(String schemaObjectName) {

        Checks.isSchemaName(schemaObjectName);

        return containsNamedObject(schemaObjectName) ;
    }

    @Override
    public T getSchemaObjectByName(String schemaObjectName) {

        Checks.isSchemaName(schemaObjectName);

        return getNamedObject(schemaObjectName);
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

    SchemaMap<T> add(T toAdd) {

        Objects.requireNonNull(toAdd);

        return new SchemaMap<>(this, toAdd);
    }
}
