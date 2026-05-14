package dev.jdata.db.custom.ansi.schema.storage;

import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.ddl.helpers.sqltoschema.complete.IDDLSchemaSQLStatementsAllocators;
import dev.jdata.db.ddl.helpers.sqltoschema.complete.scratchobjects.DDLSchemaScratchObjects;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IHeapIntSetBuilder;

public final class HeapANSIDatabaseSchemaSerialization

                extends ANSIDatabaseSchemaSerialization<
                                IHeapIntSetBuilder,
                                IHeapIndexListBuilder<Column>,
                                IHeapIndexList<BaseSQLStatement>,
                                IHeapIndexListBuilder<BaseSQLStatement>,
                                IHeapIndexListAllocator<BaseSQLStatement>,
                                IHeapCompleteSchemaMap> {

    public HeapANSIDatabaseSchemaSerialization(ISQLAllocator sqlAllocator,
            IDDLSchemaSQLStatementsAllocators<IHeapIntSetBuilder, IHeapIndexListBuilder<Column>, IHeapCompleteSchemaMap, ?, ?> ddlSchemaSQLStatementsWorkerObjects,
            DDLSchemaScratchObjects<IHeapIntSetBuilder, IHeapIndexListBuilder<Column>> ddlSchemaScratchObjects) {
        super(sqlAllocator, ddlSchemaSQLStatementsWorkerObjects, ddlSchemaScratchObjects, IHeapIndexListAllocator::create);
    }

    public HeapANSIDatabaseSchemaSerialization(ISQLAllocator sqlAllocator,
            IDDLSchemaSQLStatementsAllocators<IHeapIntSetBuilder, IHeapIndexListBuilder<Column>, IHeapCompleteSchemaMap, ?, ?> ddlSchemaSQLStatementsWorkerObjects,
            DDLSchemaScratchObjects ddlSchemaScratchObjects, Function<IntFunction<BaseSQLStatement[]>, IHeapIndexListAllocator<BaseSQLStatement>> createIndexListAllocator) {
        super(sqlAllocator, ddlSchemaSQLStatementsWorkerObjects, ddlSchemaScratchObjects, createIndexListAllocator);
    }
}
