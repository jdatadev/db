package dev.jdata.db.schema.storage;

import dev.jdata.db.engine.database.strings.IStringWriter;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.storage.sqloutputter.ISQLOutputter;

public interface IDatabaseSchemaSerializer {

    <E extends Exception> void serialize(IEffectiveDatabaseSchema databaseSchema, IStringWriter stringWriter, ISQLOutputter<E> sqlOutputter) throws E;
}
