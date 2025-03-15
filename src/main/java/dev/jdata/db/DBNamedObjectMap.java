package dev.jdata.db;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import dev.jdata.db.utils.checks.Checks;

public abstract class DBNamedObjectMap<T extends DBNamedObject, M extends DBNamedObjectMap<T, M>> {

    private final Map<String, T> map;

    public abstract M makeCopy();

    @Deprecated
    protected DBNamedObjectMap(int initialCapacity) {

        Checks.isInitialCapacity(initialCapacity);

        this.map = new HashMap<>(initialCapacity);
    }

    protected DBNamedObjectMap(Collection<T> namedObjects) {

        Objects.requireNonNull(namedObjects);

        this.map = new HashMap<>(namedObjects.size());

        for (T namedObject : namedObjects) {

            put(namedObject.getName(), namedObject);
        }
    }

    protected DBNamedObjectMap(M toCopy) {

        Objects.requireNonNull(toCopy);

        this.map = new HashMap<>(((DBNamedObjectMap<T, M>)toCopy).map);
    }

    private void put(String schemaName, T schemaObject) {

        Checks.isSchemaName(schemaName);
        Objects.requireNonNull(schemaObject);

        final String key = makeKey(schemaName);

        if (map.containsKey(key)) {

            throw new IllegalStateException();
        }

        map.put(key, schemaObject);
    }

    protected final Stream<T> namedObjectsStream() {

        return map.values().stream();
    }

    protected final boolean containsNamedObject(String name) {

        Checks.isDBName(name);

        return map.containsKey(makeKey(name));
    }

    protected final T getNamedObject(String name) {

        Checks.isDBName(name);

        return map.get(makeKey(name));
    }

    protected final Iterable<String> getNames() {

        return map.keySet();
    }

    protected final Iterable<T> getNamedObjects() {

        return map.values();
    }

    @Deprecated // object allocation
    private static String makeKey(String schemaName) {

        return schemaName.toLowerCase();
    }

    @Override
    public final String toString() {

        return getClass().getSimpleName() + " [map=" + map + "]";
    }
}
