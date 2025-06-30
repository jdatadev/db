package dev.jdata.db.custom.ansi.schema.storage;

import dev.jdata.db.custom.ansi.sql.parser.ANSISQLParserFactory;
import dev.jdata.db.ddl.allocators.DDLCompleteSchemaCachedObjects;
import dev.jdata.db.schema.model.schemamaps.HeapSchemaMapBuilders;
import dev.jdata.db.schema.storage.BaseDatabaseSchemaSerializerTest;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.parse.SQLParserFactory;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListAllocator;

public final class ANSIDatabaseSchemaSerializerTest extends BaseDatabaseSchemaSerializerTest<HeapANSIDatabaseSchemaSerializer> {

    @Override
    protected SQLParserFactory getSQLParserFactory() {

        return ANSISQLParserFactory.INSTANCE;
    }

    @Override
    protected HeapANSIDatabaseSchemaSerializer createDatabaseSchemaSerializer(ISQLAllocator sqlAllocator,
            DDLCompleteSchemaCachedObjects<HeapSchemaMapBuilders> ddlCompleteSchemaCachedObjects) {

        return new HeapANSIDatabaseSchemaSerializer(sqlAllocator, ddlCompleteSchemaCachedObjects, HeapIndexListAllocator::new);
    }
}
