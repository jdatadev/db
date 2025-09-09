package dev.jdata.db.custom.ansi.schema.storage;

import dev.jdata.db.custom.ansi.sql.parser.ANSISQLParserFactory;
import dev.jdata.db.ddl.helpers.buildschema.HeapDDLSchemaSQLStatementsWorkerObjects;
import dev.jdata.db.schema.storage.BaseDatabaseSchemaSerializerTest;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.parse.SQLParserFactory;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListAllocator;

public final class ANSIDatabaseSchemaSerializerTest extends BaseDatabaseSchemaSerializerTest<HeapANSIDatabaseSchemaSerialization> {

    @Override
    protected SQLParserFactory getSQLParserFactory() {

        return ANSISQLParserFactory.INSTANCE;
    }

    @Override
    protected HeapANSIDatabaseSchemaSerialization createDatabaseSchemaSerializer(ISQLAllocator sqlAllocator,
            HeapDDLSchemaSQLStatementsWorkerObjects ddlSchemaSQLStatementsWorkerObjects) {

        return new HeapANSIDatabaseSchemaSerialization(sqlAllocator, ddlSchemaSQLStatementsWorkerObjects, createDDLSchemaScratchObjects(), HeapIndexListAllocator::new);
    }
}
