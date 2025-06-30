package dev.jdata.db.custom.ansi.schema.storage;

import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.ddl.allocators.DDLCompleteSchemaCachedObjects;
import dev.jdata.db.schema.model.schemamaps.HeapCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.HeapSchemaMapBuilders;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.utils.adt.lists.HeapIndexList;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListBuilder;

public final class HeapANSIDatabaseSchemaSerializer

                extends ANSIDatabaseSchemaSerializer<
                                HeapIndexList<BaseSQLStatement>,
                                HeapIndexListBuilder<BaseSQLStatement>,
                                HeapIndexListAllocator<BaseSQLStatement>,
                                HeapCompleteSchemaMaps,
                                HeapSchemaMapBuilders> {

    public HeapANSIDatabaseSchemaSerializer(ISQLAllocator sqlAllocator, DDLCompleteSchemaCachedObjects<HeapSchemaMapBuilders> ddlCompleteSchemaCachedObjects,
            Function<IntFunction<BaseSQLStatement[]>, HeapIndexListAllocator<BaseSQLStatement>> createIndexListAllocator) {
        super(sqlAllocator, ddlCompleteSchemaCachedObjects, createIndexListAllocator);
    }
}
