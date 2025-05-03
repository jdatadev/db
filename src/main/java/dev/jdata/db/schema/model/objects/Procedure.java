package dev.jdata.db.schema.model.objects;

import dev.jdata.db.utils.checks.Checks;

public final class Procedure extends ExecutableObject {

    Procedure(long parsedName, long hashName, int id) {
        super(parsedName, hashName, id);

        Checks.isProcedureId(id);
    }
}
