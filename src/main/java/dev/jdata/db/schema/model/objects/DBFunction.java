package dev.jdata.db.schema.model.objects;

import dev.jdata.db.utils.checks.Checks;

public final class DBFunction extends ExecutableObject {

    DBFunction(long parsedName, long hashName, int id) {
        super(parsedName, hashName, id);

        Checks.isDBFunctionId(id);
    }
}
