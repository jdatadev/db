package dev.jdata.db.schema.storage;

import java.util.function.Function;
import java.util.function.ToIntFunction;

import org.jutils.io.buffers.BaseStringBuffers;
import org.jutils.parse.ParserException;

import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMaps;

public interface IDatabaseSchemaDeserializer<T extends IAllCompleteSchemaMaps> {

    <E extends Exception, BUFFER extends BaseStringBuffers<E>> T deserialize(BUFFER buffer, Function<String, E> createEOFException,
            StringManagement stringManagement, ToIntFunction<DDLObjectType> schemaObjectIdAllocator) throws ParserException, E;
}
