package dev.jdata.db.custom.ansi.schema.storage;

import dev.jdata.db.custom.ansi.sql.parser.ANSISQLParserFactory;
import dev.jdata.db.ddl.DDLCompleteSchemasHelper.DDLCompleteSchemaCachedObjects;
import dev.jdata.db.schema.storage.BaseDatabaseSchemaSerializer;
import dev.jdata.db.sql.ast.ISQLAllocator;

public final class ANSIDatabaseSchemaSerializer extends BaseDatabaseSchemaSerializer {

    public ANSIDatabaseSchemaSerializer(ISQLAllocator sqlAllocator, DDLCompleteSchemaCachedObjects ddlCompleteSchemaCachedObjects) {
        super(ANSISQLParserFactory.INSTANCE, sqlAllocator, ddlCompleteSchemaCachedObjects);
    }
}
