package dev.jdata.db.custom.ansi.schema.storage;

import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.custom.ansi.sql.parser.ANSISQLParserFactory;
import dev.jdata.db.ddl.allocators.DDLCompleteSchemaCachedObjects;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.SchemaMapBuilders;
import dev.jdata.db.schema.storage.BaseDatabaseSchemaSerializer;
import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;

public abstract class ANSIDatabaseSchemaSerializer<

                INDEX_LIST extends IndexList<BaseSQLStatement>,
                INDEX_LIST_BUILDER extends IndexList.IndexListBuilder<BaseSQLStatement, INDEX_LIST, INDEX_LIST_BUILDER>,
                INDEX_LIST_ALLOCATOR extends IndexListAllocator<BaseSQLStatement, INDEX_LIST, INDEX_LIST_BUILDER, ?>,
                COMPLETE_SCHEMA_MAPS extends CompleteSchemaMaps<?>,
                SCHEMA_MAP_BUILDERS extends SchemaMapBuilders<?, ?, ?, ?, ?, ?, COMPLETE_SCHEMA_MAPS>>

        extends BaseDatabaseSchemaSerializer<INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, COMPLETE_SCHEMA_MAPS, SCHEMA_MAP_BUILDERS> {

    protected ANSIDatabaseSchemaSerializer(ISQLAllocator sqlAllocator, DDLCompleteSchemaCachedObjects<SCHEMA_MAP_BUILDERS> ddlCompleteSchemaCachedObjects,
            Function<IntFunction<BaseSQLStatement[]>, INDEX_LIST_ALLOCATOR> createIndexListAllocator) {
        super(ANSISQLParserFactory.INSTANCE, sqlAllocator, ddlCompleteSchemaCachedObjects, createIndexListAllocator);
    }
}
