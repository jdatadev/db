package dev.jdata.db.schema;

import dev.jdata.db.DBNamedObject;
import dev.jdata.db.utils.checks.Checks;

abstract class SchemaObject extends DBNamedObject {

    private final int id;

    SchemaObject(String name, int id) {
        super(name);

        this.id = Checks.isSchemaId(id);
    }

    public final int getId() {
        return id;
    }
}
