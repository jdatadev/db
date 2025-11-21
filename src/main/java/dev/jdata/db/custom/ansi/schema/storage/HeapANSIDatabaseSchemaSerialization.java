package dev.jdata.db.custom.ansi.schema.storage;

import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.ddl.allocators.DDLSchemaScratchObjects;
import dev.jdata.db.ddl.helpers.buildschema.IDDLSchemaSQLStatementsWorkerObjects;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;

public final class HeapANSIDatabaseSchemaSerialization

                extends ANSIDatabaseSchemaSerialization<
                                IHeapIndexList<BaseSQLStatement>,
                                IHeapIndexListBuilder<BaseSQLStatement>,
                                IHeapIndexListAllocator<BaseSQLStatement>,
                                IHeapAllCompleteSchemaMaps> {

    public HeapANSIDatabaseSchemaSerialization(ISQLAllocator sqlAllocator,
            IDDLSchemaSQLStatementsWorkerObjects<IHeapAllCompleteSchemaMaps, ?, ?> ddlSchemaSQLStatementsWorkerObjects, DDLSchemaScratchObjects ddlSchemaScratchObjects) {
        super(sqlAllocator, ddlSchemaSQLStatementsWorkerObjects, ddlSchemaScratchObjects, IHeapIndexListAllocator::create);
    }

    public HeapANSIDatabaseSchemaSerialization(ISQLAllocator sqlAllocator,
            IDDLSchemaSQLStatementsWorkerObjects<IHeapAllCompleteSchemaMaps, ?, ?> ddlSchemaSQLStatementsWorkerObjects,
            DDLSchemaScratchObjects ddlSchemaScratchObjects, Function<IntFunction<BaseSQLStatement[]>, IHeapIndexListAllocator<BaseSQLStatement>> createIndexListAllocator) {
        super(sqlAllocator, ddlSchemaSQLStatementsWorkerObjects, ddlSchemaScratchObjects, createIndexListAllocator);
    }
}
