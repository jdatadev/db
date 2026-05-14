package dev.jdata.db.custom.ansi.schema.storage;

import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.custom.ansi.sql.parser.ANSISQLParserFactory;
import dev.jdata.db.ddl.helpers.sqltoschema.complete.IDDLSchemaSQLStatementsAllocators;
import dev.jdata.db.ddl.helpers.sqltoschema.complete.scratchobjects.DDLSchemaScratchObjects;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMap;
import dev.jdata.db.schema.storage.BaseDatabaseSchemaSerialization;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IIntSetBuilder;

public abstract class ANSIDatabaseSchemaSerialization<

                INT_SET_BUILDER extends IIntSetBuilder<?, ?>,
                COLUMN_INDEX_LIST_BUILDER extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>,
                INDEX_LIST extends IIndexList<BaseSQLStatement>,
                INDEX_LIST_BUILDER extends IIndexListBuilder<BaseSQLStatement, INDEX_LIST, ?>,
                INDEX_LIST_ALLOCATOR extends IIndexListAllocator<BaseSQLStatement, INDEX_LIST, ?, INDEX_LIST_BUILDER>,
                COMPLETE_SCHEMA_MAP extends ICompleteSchemaMap>

        extends BaseDatabaseSchemaSerialization<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, COMPLETE_SCHEMA_MAP> {

    ANSIDatabaseSchemaSerialization(ISQLAllocator sqlAllocator,
            IDDLSchemaSQLStatementsAllocators<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER, COMPLETE_SCHEMA_MAP, ?, ?> ddlSchemaSQLStatementsWorkerObjects,
            DDLSchemaScratchObjects ddlSchemaScratchObjects, Function<IntFunction<BaseSQLStatement[]>, INDEX_LIST_ALLOCATOR> createIndexListAllocator) {
        super(ANSISQLParserFactory.INSTANCE, sqlAllocator, ddlSchemaSQLStatementsWorkerObjects, ddlSchemaScratchObjects, createIndexListAllocator);
    }
}
