package dev.jdata.db.custom.ansi.schema.storage;

import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.custom.ansi.sql.parser.ANSISQLParserFactory;
import dev.jdata.db.ddl.allocators.DDLSchemaScratchObjects;
import dev.jdata.db.ddl.helpers.buildschema.IDDLSchemaSQLStatementsWorkerObjects;
import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMaps;
import dev.jdata.db.schema.storage.BaseDatabaseSchemaSerialization;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;

public abstract class ANSIDatabaseSchemaSerialization<

                INDEX_LIST extends IIndexList<BaseSQLStatement>,
                INDEX_LIST_BUILDER extends IIndexListBuilder<BaseSQLStatement, INDEX_LIST, ?>,
                INDEX_LIST_ALLOCATOR extends IIndexListAllocator<BaseSQLStatement, INDEX_LIST, ?, INDEX_LIST_BUILDER>,
                COMPLETE_SCHEMA_MAPS extends IAllCompleteSchemaMaps>

        extends BaseDatabaseSchemaSerialization<INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, COMPLETE_SCHEMA_MAPS> {

    protected ANSIDatabaseSchemaSerialization(ISQLAllocator sqlAllocator, IDDLSchemaSQLStatementsWorkerObjects<COMPLETE_SCHEMA_MAPS, ?, ?> ddlSchemaSQLStatementsWorkerObjects,
            DDLSchemaScratchObjects ddlSchemaScratchObjects, Function<IntFunction<BaseSQLStatement[]>, INDEX_LIST_ALLOCATOR> createIndexListAllocator) {
        super(ANSISQLParserFactory.INSTANCE, sqlAllocator, ddlSchemaSQLStatementsWorkerObjects, ddlSchemaScratchObjects, createIndexListAllocator);
    }
}
