package dev.jdata.db.schema.storage;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.storage.sqloutputter.ISQLOutputter;

public interface IDatabaseSchemaSerializer {

    <E extends Exception> void serialize(IEffectiveDatabaseSchema databaseSchema, StringResolver stringResolver, ISQLOutputter<E> sqlOutputter) throws E;
}
