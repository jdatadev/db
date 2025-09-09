package dev.jdata.db.custom.ansi.schema.storage;

import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.ddl.allocators.DDLSchemaScratchObjects;
import dev.jdata.db.ddl.helpers.buildschema.IDDLSchemaSQLStatementsWorkerObjects;
import dev.jdata.db.schema.model.schemamaps.HeapAllCompleteSchemaMaps;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.utils.adt.lists.HeapIndexList;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListBuilder;

public final class HeapANSIDatabaseSchemaSerialization

                extends ANSIDatabaseSchemaSerialization<
                                HeapIndexList<BaseSQLStatement>,
                                HeapIndexListBuilder<BaseSQLStatement>,
                                HeapIndexListAllocator<BaseSQLStatement>,
                                HeapAllCompleteSchemaMaps> {

    public HeapANSIDatabaseSchemaSerialization(ISQLAllocator sqlAllocator, IDDLSchemaSQLStatementsWorkerObjects<HeapAllCompleteSchemaMaps, ?> ddlSchemaSQLStatementsWorkerObjects,
            DDLSchemaScratchObjects ddlSchemaScratchObjects) {
        super(sqlAllocator, ddlSchemaSQLStatementsWorkerObjects, ddlSchemaScratchObjects, HeapIndexListAllocator::new);
    }

    public HeapANSIDatabaseSchemaSerialization(ISQLAllocator sqlAllocator, IDDLSchemaSQLStatementsWorkerObjects<HeapAllCompleteSchemaMaps, ?> ddlSchemaSQLStatementsWorkerObjects,
            DDLSchemaScratchObjects ddlSchemaScratchObjects, Function<IntFunction<BaseSQLStatement[]>, HeapIndexListAllocator<BaseSQLStatement>> createIndexListAllocator) {
        super(sqlAllocator, ddlSchemaSQLStatementsWorkerObjects, ddlSchemaScratchObjects, createIndexListAllocator);
    }
}
