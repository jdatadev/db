package dev.jdata.db.schema.model.schemamaps;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.SchemaMap;

abstract class BaseSchemaObjectState {

    private final SchemaMap<? extends SchemaObject, ?, ?> map;

    BaseSchemaObjectState(SchemaMap<? extends SchemaObject, ?, ?> map) {

        this.map = Objects.requireNonNull(map);
    }

    final SchemaMap<? extends SchemaObject, ?, ?> getMap() {
        return map;
    }
}
