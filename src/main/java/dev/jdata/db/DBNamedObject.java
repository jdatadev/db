package dev.jdata.db;

import dev.jdata.db.utils.checks.Checks;

public abstract class DBNamedObject {

    private final String name;

    protected DBNamedObject(String name) {

        this.name = Checks.isSchemaName(name);
    }

    public final String getName() {
        return name;
    }
}
