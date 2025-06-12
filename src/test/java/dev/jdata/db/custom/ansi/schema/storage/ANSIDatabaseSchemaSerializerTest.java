package dev.jdata.db.custom.ansi.schema.storage;

import dev.jdata.db.custom.ansi.sql.parser.ANSISQLParserFactory;
import dev.jdata.db.ddl.DDLCompleteSchemasHelper.DDLCompleteSchemaCachedObjects;
import dev.jdata.db.schema.storage.BaseDatabaseSchemaSerializerTest;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.parse.SQLParserFactory;

public final class ANSIDatabaseSchemaSerializerTest extends BaseDatabaseSchemaSerializerTest<ANSIDatabaseSchemaSerializer> {

    @Override
    protected SQLParserFactory getSQLParserFactory() {

        return ANSISQLParserFactory.INSTANCE;
    }

    @Override
    protected ANSIDatabaseSchemaSerializer createDatabaseSchemaSerializer(ISQLAllocator sqlAllocator, DDLCompleteSchemaCachedObjects ddlCompleteSchemaCachedObjects) {

        return new ANSIDatabaseSchemaSerializer(sqlAllocator, ddlCompleteSchemaCachedObjects);
    }
}
