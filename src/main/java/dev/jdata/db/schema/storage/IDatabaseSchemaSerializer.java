package dev.jdata.db.schema.storage;

import java.util.function.Function;

import org.jutils.io.buffers.BaseStringBuffers;
import org.jutils.io.strings.StringResolver;
import org.jutils.parse.ParserException;

import dev.jdata.db.ddl.DDLSchemasHelper.SchemaObjectIdAllocator;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.storage.sqloutputter.ISQLOutputter;

public interface IDatabaseSchemaSerializer {

    <E extends Exception> void serialize(IEffectiveDatabaseSchema databaseSchema, StringResolver stringResolver, ISQLOutputter<E> sqlOutputter) throws E;

    <E extends Exception, BUFFER extends BaseStringBuffers<E>, P> CompleteSchemaMaps deserialize(BUFFER buffer, Function<String, E> createEOFException,
            StringManagement stringManagement, SchemaObjectIdAllocator<P> schemaObjectIdAllocator) throws ParserException, E;
}
