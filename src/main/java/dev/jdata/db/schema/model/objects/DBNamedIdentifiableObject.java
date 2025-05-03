package dev.jdata.db.schema.model.objects;

import dev.jdata.db.DBNamedObject;
import dev.jdata.db.utils.checks.Checks;

public abstract class DBNamedIdentifiableObject extends DBNamedObject {

    private final int id;

    DBNamedIdentifiableObject(long parsedName, long hashName, int id) {
        super(parsedName, hashName);

        this.id = Checks.isSchemaObjectId(id);
    }

    DBNamedIdentifiableObject(DBNamedIdentifiableObject toCopy) {
        super(toCopy);

        this.id = toCopy.id;
    }

    public final int getId() {
        return id;
    }
}
