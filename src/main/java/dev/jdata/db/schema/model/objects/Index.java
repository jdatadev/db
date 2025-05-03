package dev.jdata.db.schema.model.objects;

import dev.jdata.db.utils.checks.Checks;

public final class Index extends SchemaObject {

    Index(long parsedName, long hashName, int id) {
        super(parsedName, hashName, id);

        Checks.isIndexId(id);
    }
}
