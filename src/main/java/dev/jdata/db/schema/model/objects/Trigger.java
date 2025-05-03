package dev.jdata.db.schema.model.objects;

import dev.jdata.db.utils.checks.Checks;

public final class Trigger extends SchemaObject {

    Trigger(long parsedName, long hashName, int id) {
        super(parsedName, hashName, id);

        Checks.isTriggerId(id);
    }
}
